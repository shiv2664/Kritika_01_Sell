package com.shivam.kritika_01_sell;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private ImageButton imageButton1,imageButton2;

    FirebaseAuth firebaseAuth;
    Toolbar toolbar;
    String TAG="MyTag";
    String Colour1="";
    String Colour2="";
    String Colour3="";
    String Colour4="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth=FirebaseAuth.getInstance();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Dashboard");

        imageButton1=findViewById(R.id.img_button1);
        imageButton2=findViewById(R.id.img_button2);

        /*
        String GAM = "Google,Apple,Microsoft";
        String[] values = GAM.split(",");


        Log.d(TAG,Arrays.toString(values));
        Log.d(TAG,Colour1+Colour2+Colour3+Colour4);

         */


        imageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,profileActivity.class);
                startActivity(intent);
            }
        });

        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,Upload_New_Product.class);
                startActivity(intent);


            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu1, menu);


        return super.onCreateOptionsMenu(menu);


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.SignOut) {

            firebaseAuth.signOut();
            Intent intent =new Intent(MainActivity.this,RegisterActivity.class);
            startActivity(intent);
            finish();

        }

        return super.onOptionsItemSelected(item);


    }
}