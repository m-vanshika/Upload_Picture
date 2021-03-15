package com.example.uploadpicture;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.gridlayout.widget.GridLayout;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;

import com.bumptech.glide.Glide;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button button;
    private Uri imageuri;
    GridLayout gv;
    ConstraintLayout constraintLayout;
    private FirebaseStorage storage;
   private StorageReference storageRef ;

   private List<Upload> mUploads;
   private DatabaseReference databaseReference,mDatabaseRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        storage=FirebaseStorage.getInstance();
        storageRef=storage.getReference();
         button=(Button)findViewById(R.id.button);
         storageRef=FirebaseStorage.getInstance().getReference("uploads");
         databaseReference= FirebaseDatabase.getInstance().getReference("uploads");
         constraintLayout=(ConstraintLayout)findViewById(R.id.Constraint);
         gv=(GridLayout) findViewById(R.id.grid);
        mUploads =new ArrayList<>();
        mDatabaseRef=FirebaseDatabase.getInstance().getReference("uploads");
        gv.removeAllViews();
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot:snapshot.getChildren()){
                    Upload upload=postSnapshot.getValue(Upload.class);
                    //  mUploads.add(upload);
                    String mimageuri= (upload.getMimageurl());
                    ImageView pp=new ImageView(MainActivity.this);
                    Glide.with(MainActivity.this).load(mimageuri).into(pp);
                    // pp.setImageURI(mimageuri);
                    pp.setPadding(1,1,1,1);
                    Display display = getWindowManager().getDefaultDisplay();
                    int width = ((display.getWidth()*33)/100);
                    int height =((display.getHeight()*20)/100);

                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);
                    pp.setLayoutParams(layoutParams);
                    gv.setColumnCount(3);
                    gv.addView(pp);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 choosePic();
             }
         });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1&&resultCode==RESULT_OK&&data!=null&&data.getData()!=null)
        {
            imageuri=data.getData();
           /* ImageView pp=new ImageView(MainActivity.this);

            pp.setImageURI(imageuri);
            pp.setPadding(1,1,1,1);
            Display display = getWindowManager().getDefaultDisplay();
            int width = ((display.getWidth()*33)/100);
            int height =((display.getHeight()*20)/100);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);
            pp.setLayoutParams(layoutParams);
            gv.setColumnCount(3);
            gv.addView(pp);*/

            uploadPic();
            gv.removeAllViews();
        }
    }

    void choosePic()
    {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);

    }
private  String getFileExtension(Uri uri)
{
    ContentResolver cR=getContentResolver();
    MimeTypeMap mime=MimeTypeMap.getSingleton();
    return mime.getExtensionFromMimeType(cR.getType(uri));
}
    private void uploadPic() {
        if(imageuri!=null)
        {
            StorageReference fileReference =storageRef.child((System.currentTimeMillis()+"."+getFileExtension((imageuri))));
    fileReference.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
        @Override
        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
            Toast.makeText(MainActivity.this,"Uploaded",Toast.LENGTH_LONG).show();
            Upload upload=new Upload(imageuri.toString());
            String uploadid=databaseReference.push().getKey();
            databaseReference.child(uploadid).setValue(upload);
        }
    }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    });
        }
        else
        {
            Toast.makeText(this,"No file selected",Toast.LENGTH_LONG).show();
        }

    }
    protected void onStart() {

        super.onStart();

    }
}
