package com.example.week_second_task;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static android.content.Intent.*;


public class ProfileActivity extends AppCompatActivity {
   public TextView name, email, phone,gender;
    FloatingActionButton edit;
    DatabaseHelper myDB;
    ImageView imageView;
    String s1,s2,s3,s4;
    static  final int REQUEST_CODE=123;
    private static final int PICK_IMAGE = 100;
    Uri imageUri;
    private PrefManager prefManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        prefManager = new PrefManager(getApplicationContext());
        if (prefManager.isFirstTimeLaunch()) {
            prefManager.setFirstTimeLaunch(false);
            startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
            finish();
        }

        name = (TextView) findViewById(R.id.name);
        email = (TextView) findViewById(R.id.email);
        phone = (TextView) findViewById(R.id.phone);
        gender=(TextView)findViewById(R.id.gender);
        imageView=(ImageView) findViewById(R.id.profile);
        edit=(FloatingActionButton) findViewById(R.id.back);
        myDB= new DatabaseHelper(ProfileActivity.this);
        display();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.CAMERA)
                        + ContextCompat.checkSelfPermission(ProfileActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    if(ActivityCompat.shouldShowRequestPermissionRationale(ProfileActivity.this,Manifest.permission.CAMERA)||
                            ActivityCompat.shouldShowRequestPermissionRationale(ProfileActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)){
                        AlertDialog.Builder builder=new AlertDialog.Builder(ProfileActivity.this);
                        builder.setTitle("Grant those permission..!");
                        builder.setMessage("camera and read storage");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(ProfileActivity.this, new String[]{
                                                Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE
                                        }, REQUEST_CODE
                                );
                            }



                        });

                        builder.setNegativeButton("Cancel",null);
                        AlertDialog alertDialog=builder.create();
                        alertDialog.show();

                    }
                    else
                    {
                        ActivityCompat.requestPermissions(ProfileActivity.this, new String[]{
                                        Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE
                                }, REQUEST_CODE
                        );
                    }
                }else
                {
                    Toast.makeText(getApplicationContext(),"Permission already granted",Toast.LENGTH_SHORT).show();
                }

                Intent gallery = new Intent(ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery, PICK_IMAGE);

            }




        });
        edit.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                s1=name.getText().toString();
                s2=email.getText().toString();
                s3=phone.getText().toString();
                s4=gender.getText().toString();
                Intent intent=new Intent(ProfileActivity.this,EditActivity.class);

                intent.putExtra("name",s1);
                intent.putExtra("email",s2);
                intent.putExtra("phone",s3);
                intent.putExtra("gender",s4);
               intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP);
               intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==REQUEST_CODE)
        {
            if((grantResults.length>0)&& (grantResults[0]+grantResults[1]==PackageManager.PERMISSION_GRANTED))
            {
                Toast.makeText(getApplicationContext(),"Permission Granted",Toast.LENGTH_SHORT).show();

            }else
            {
                Toast.makeText(getApplicationContext(),"Permission Denied",Toast.LENGTH_SHORT).show();
            }

        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            imageUri = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(imageUri,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            ImageView imageView = (ImageView) findViewById(R.id.profile);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));


        }
    }


    void display()
    {
        Cursor cursor=myDB.readAllData();
        if(cursor.getCount()==0)
        {
            Toast.makeText(this,"No Data", Toast.LENGTH_LONG).show();
        }
        else {
            while (cursor.moveToNext())
            {
                name.setText(cursor.getString(0));
                email.setText(cursor.getString(1));
                phone.setText(cursor.getString(2));
                gender.setText(cursor.getString(3));


            }
        }

    }

}