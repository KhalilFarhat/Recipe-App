package com.example.recipeapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class dbHelper extends SQLiteOpenHelper {


    private final Context context;
    private static final String DATABASE_NAME = "MyDataBase.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "Users";
    private static final String COLUMN_NAME = "user_name";
    private static final String COLUMN_EMAIL = "user_email";
    private static final String COLUMN_PASSWORD = "user_password";
    private static final String BOOKMARK1 = "BOOKMARK1";
    private static final String BOOKMARK2 = "BOOKMARK2";
    private static final String BOOKMARK3 = "BOOKMARK3";
    private static final String BOOKMARK4 = "BOOKMARK4";
    private static final String BOOKMARK5 = "BOOKMARK5";

    public dbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String COLUMN_id = "user_id";
        String query =
                "CREATE TABLE " + TABLE_NAME +
                        " ("+ COLUMN_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_NAME + " TEXT, " +
                        COLUMN_EMAIL + " TEXT UNIQUE, "+
                        COLUMN_PASSWORD + " TEXT, " +
                        BOOKMARK1 + " INTEGER, " +
                        BOOKMARK2 + " INTEGER, " +
                        BOOKMARK3 + " INTEGER, " +
                        BOOKMARK4 + " INTEGER, " +
                        BOOKMARK5 + " INTEGER);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    void addUser(String name, String email, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_NAME, name);
        cv.put(COLUMN_EMAIL, email);
        cv.put(COLUMN_PASSWORD, password);
        cv.put(BOOKMARK1, 0);
        cv.put(BOOKMARK2, 0);
        cv.put(BOOKMARK3, 0);
        cv.put(BOOKMARK4, 0);
        cv.put(BOOKMARK5, 0);
        long result = db.insert(TABLE_NAME, null, cv);
        if (result == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(context, "Added Successfully", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isEmailAlreadyExist(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_EMAIL + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email});
        boolean emailExists = cursor.moveToFirst();
        cursor.close();
        return emailExists;
    }
    public boolean isPasswordCorrect(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_EMAIL + " = ? AND " + COLUMN_PASSWORD + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email,password});
        boolean isCorrect = cursor.moveToFirst();
        cursor.close();
        return isCorrect;
    }
    public String getUsername(String email){
        String Username;
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_EMAIL + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email});
        cursor.moveToFirst();
        Username = cursor.getString(1);
        return Username;
    }
    public void addBookmark(int bm, String email){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_EMAIL + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email});
        cursor.moveToFirst();
        if(cursor.getInt(4) == 0){
            cv.put(BOOKMARK1, bm);
            Toast.makeText(context, "Bookmarked Successfully", Toast.LENGTH_SHORT).show();
        }
        else if (cursor.getInt(5) == 0){
            cv.put(BOOKMARK2, bm);
            Toast.makeText(context, "Bookmarked Successfully", Toast.LENGTH_SHORT).show();
        }
        else if (cursor.getInt(6) == 0) {
            cv.put(BOOKMARK3, bm);
            Toast.makeText(context, "Bookmarked Successfully", Toast.LENGTH_SHORT).show();
        }
        else if (cursor.getInt(7) == 0) {
            cv.put(BOOKMARK4, bm);
            Toast.makeText(context, "Bookmarked Successfully", Toast.LENGTH_SHORT).show();

        }
        else if (cursor.getInt(8) == 0) {
            cv.put(BOOKMARK5, bm);
            Toast.makeText(context, "Bookmarked Successfully", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(context, "Cannot add more than 5 favorites.", Toast.LENGTH_SHORT).show();
        db.update(TABLE_NAME, cv, COLUMN_EMAIL + " = ?", new String[]{email});
    }
    public void removeBookmark(int bm, String email){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_EMAIL + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email});
        cursor.moveToFirst();
        if(cursor.getInt(4) == bm){
            cv.put(BOOKMARK1, 0);
            Toast.makeText(context, "Removed Bookmark Successfully", Toast.LENGTH_SHORT).show();
        }
        else if (cursor.getInt(5) == bm){
            cv.put(BOOKMARK2, 0);
            Toast.makeText(context, "Removed Bookmark Successfully", Toast.LENGTH_SHORT).show();
        }
        else if (cursor.getInt(6) == bm) {
            cv.put(BOOKMARK3, 0);
            Toast.makeText(context, "Removed Bookmark Successfully", Toast.LENGTH_SHORT).show();
        }
        else if (cursor.getInt(7) == bm) {
            cv.put(BOOKMARK4, 0);
            Toast.makeText(context, "Removed Bookmark Successfully", Toast.LENGTH_SHORT).show();
        }
        else if (cursor.getInt(8) == bm) {
            cv.put(BOOKMARK5, 0);
            Toast.makeText(context, "Removed Bookmark Successfully", Toast.LENGTH_SHORT).show();
        }
            db.update(TABLE_NAME, cv, COLUMN_EMAIL + " = ?", new String[]{email});
    }
    public ArrayList<Integer> getBookmarks(String email){
        ArrayList<Integer> Favorites = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_EMAIL + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email});
        cursor.moveToFirst();
        Favorites.add(cursor.getInt(4));
        Favorites.add(cursor.getInt(5));
        Favorites.add(cursor.getInt(6));
        Favorites.add(cursor.getInt(7));
        Favorites.add(cursor.getInt(8));
        Favorites.removeAll(Collections.singleton(0));
        return Favorites;
    }
    public boolean changeEmail(String oldEmail, String newEmail) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("email", newEmail);

        // updating row
        int result = db.update(TABLE_NAME, values, COLUMN_EMAIL + " = ?", new String[] { oldEmail });
        db.close();

        return result > 0;

    }


    public boolean isBookmarked(int bm, String email){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_EMAIL + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email});
        cursor.moveToFirst();
        for(int i = 4; i < 9; i++){
            if(cursor.getInt(i) == bm)
                return true;
        }
        return false;
    }
    public boolean areBookmarksFull(String email){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_EMAIL + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email});
        cursor.moveToFirst();
        for(int i = 4; i < 9; i++){
            if(cursor.getInt(i) == 0)
                return false;
        }
        return true;
    }
    public boolean changePassword(String email, String oldPassword, String newPassword){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_EMAIL + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email});
        cursor.moveToFirst();
        if (!Objects.equals(oldPassword, cursor.getString(3))){
//            Toast.makeText(context, oldPassword, Toast.LENGTH_SHORT).show();
//            Toast.makeText(context, cursor.getString(3), Toast.LENGTH_SHORT).show();
            return false;}
        cv.put(COLUMN_PASSWORD, newPassword);
        db.update(TABLE_NAME, cv, COLUMN_EMAIL + " = ?", new String[]{email});
        return true;
    }
    public void deleteAccount(String email){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_EMAIL + " = ?", new String[]{email});
    }
}
