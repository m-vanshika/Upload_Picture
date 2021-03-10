package com.example.uploadpicture;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.gridlayout.widget.GridLayout;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button button;
    private Uri imageuri;
    GridLayout gv;
    ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         button=(Button)findViewById(R.id.button);
         constraintLayout=(ConstraintLayout)findViewById(R.id.Constraint);
         gv=(GridLayout) findViewById(R.id.grid);
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
            ImageView pp=new ImageView(MainActivity.this);

            pp.setImageURI(imageuri);
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

    void choosePic()
    {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }
}
