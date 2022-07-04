package com.example.chrisabel_greenflag;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "accounts.db";
    public static final String TABLE_NAME = "accounts";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table if not exists accounts " +
                        "(Email text primary key, password text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS accounts");
        onCreate(db);
    }

    public void insertAccount(String email, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Email", email);
        contentValues.put("Password", password);
        db.insert("accounts", null, contentValues);
    }

    public Account getAccount(String email){
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {
                "Email",
                "Password"
        };
        String[] inputs = {
                email
        };
        Account account;
        Cursor res =  db.query( TABLE_NAME, columns, "Email = ?", inputs, null, null, null);
        if(res.getCount() > 0) {
            res.moveToFirst();
            account = new Account(res.getString(0), res.getString(1));
        } else{
            account = new Account(null, null);
        }
        res.close();
        return account;
    }

    public boolean isEmailTiedToAnAccount(String email){
        Account account = getAccount(email);
        return (account.getEmail() != null);
    }
}
