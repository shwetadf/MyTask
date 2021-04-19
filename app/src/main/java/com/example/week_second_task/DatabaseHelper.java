package com.example.week_second_task;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(@Nullable Context context) {
        super(context, "user.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE User_info(NAME,EMAIL,NUMBER,PASSWORD,GENDER)";

        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table User_info");

    }

    public boolean insert(String Name, String Email, String Number, String Password,String Gender) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("NAME", Name);
        contentValues.put("EMAIL", Email);
        contentValues.put("NUMBER", Number);
        contentValues.put("PASSWORD",Password);
        contentValues.put("GENDER",Gender);
        long result = sqLiteDatabase.insert("User_info", null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }

    }

   Cursor readAllData() {
        Cursor cursor;
        SQLiteDatabase db = this.getReadableDatabase();

        cursor = db.rawQuery("SELECT NAME,EMAIL,NUMBER,GENDER FROM User_info", null);

        return cursor;
    }

    public boolean checkUserExist(String email, String password){
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {"email"};
        String selection = "email=? and password = ?";
        String[] selectionArgs = {email, password};

        Cursor cursor = db.query("User_info", columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();

        cursor.close();
        close();

        if(count > 0){
            return true;
        } else {
            return false;
        }
    }

    public  void updateData(String newname,String oldname,String newemail,String oldemail,String newnumber,String oldnumber,String newpassword,String oldpassword,String newgender,String oldgender)
    {

        SQLiteDatabase db = this.getReadableDatabase();



        db.execSQL("UPDATE User_info SET NAME='"+newname+"',EMAIL='"+newemail+"',NUMBER='"+newnumber+"',PASSWORD='"+newpassword+"',GENDER='"+newgender+"' WHERE NAME='"+oldname+"'");


    }


}

