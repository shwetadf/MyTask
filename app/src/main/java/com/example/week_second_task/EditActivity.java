package com.example.week_second_task;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

public class EditActivity extends AppCompatActivity {


    EditText name, email, phone, password;
    private Button Submit;
    String Gender="";
    DatabaseHelper myDB;
    String s1,s2,s3,s4;
     RadioGroup radioSexGroup;
     RadioButton rmale,rfemale;
    boolean isNameValid, isEmailValid, isPhoneValid, isPasswordValid;
    TextInputLayout nameError, emailError, phoneError, passError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name = (EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.email);
        phone = (EditText) findViewById(R.id.phone);
        password = (EditText) findViewById(R.id.password);

        nameError = (TextInputLayout) findViewById(R.id.nameError);
        emailError = (TextInputLayout) findViewById(R.id.emailError);
        phoneError = (TextInputLayout) findViewById(R.id.phoneError);
        passError = (TextInputLayout) findViewById(R.id.passError);
        radioSexGroup=(RadioGroup)findViewById(R.id.radioGroup1);
        rmale=(RadioButton)findViewById(R.id.radioButton1);
        rfemale=(RadioButton)findViewById(R.id.radioButton2);
         myDB= new DatabaseHelper(EditActivity.this);
        Intent rc=getIntent();
        s1=rc.getStringExtra("name");
        s2=rc.getStringExtra("email");
        s3=rc.getStringExtra("phone");
        s4=rc.getStringExtra("gender");
        name.setText(s1);
        email.setText(s2);
        phone.setText(s3);

        radioSexGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                if(i==R.id.radioButton1){
                    Gender="Male";
                }
                else if(i==R.id.radioButton2){
                    Gender="Female";
                }
            }
        });
        Submit = (Button)findViewById(R.id.s);
        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newname=name.getText().toString();
                String newemail=email.getText().toString();
                String newphone=phone.getText().toString();
                String newpassword=password.getText().toString();
                String newgender=Gender.toString();


                if(newname.equals("")||newemail.equals("")||newphone.equals("")||newpassword.equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Enter please",Toast.LENGTH_LONG).show();

                }
                else
                {
                    SetValidation();
                    myDB.updateData(newname,s1,newemail,s2,newphone,s3,newpassword,s4,newgender,Gender);
                    Toast.makeText(getApplicationContext(),"Update Successfully",Toast.LENGTH_LONG).show();
                }
                name.setText(null);
                email.setText(null);
                phone.setText(null);
                password.setText(null);


            }
        });


    }

    public void SetValidation() {
        final DatabaseHelper databaseHelper = new DatabaseHelper(this);
        String s1 = name.getText().toString();
        String s2 = email.getText().toString();
        String s3 = phone.getText().toString();
        String s4 = password.getText().toString();
        String s5= Gender.toString();

        // Check for a valid name.
        if (s1.isEmpty()) {
            nameError.setError(getResources().getString(R.string.name_error));
            isNameValid = false;
        } else  {
            isNameValid = true;
            nameError.setErrorEnabled(false);
        }

        // Check for a valid email address.
        if (s2.isEmpty()) {
            emailError.setError(getResources().getString(R.string.email_error));
            isEmailValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
            emailError.setError(getResources().getString(R.string.error_invalid_email));
            isEmailValid = false;
        } else  {
            isEmailValid = true;
            emailError.setErrorEnabled(false);
        }

        // Check for a valid phone number.
        if (s3.isEmpty()) {
            phoneError.setError(getResources().getString(R.string.phone_error));
            isPhoneValid = false;
        } else  {
            isPhoneValid = true;
            phoneError.setErrorEnabled(false);
        }

        // Check for a valid password.
        if (s4.isEmpty()) {
            passError.setError(getResources().getString(R.string.password_error));
            isPasswordValid = false;
        } else if (password.getText().length() < 6) {
            passError.setError(getResources().getString(R.string.error_invalid_password));
            isPasswordValid = false;
        } else  {
            isPasswordValid = true;
            passError.setErrorEnabled(false);
        }

        if (isNameValid && isEmailValid && isPhoneValid && isPasswordValid) {
            Toast.makeText(getApplicationContext(), "Successfully Update", Toast.LENGTH_SHORT).show();

        }

    }






}
