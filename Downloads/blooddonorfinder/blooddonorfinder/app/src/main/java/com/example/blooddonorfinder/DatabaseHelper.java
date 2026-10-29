
package com.example.blooddonorfinder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "BloodDonor.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "donors";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE donors (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "address TEXT NOT NULL, " +
                "blood_group TEXT NOT NULL, " +
                "district TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,
                          int oldVersion, int newVersion) {
        // Add database migration code here if needed.
    }

    public boolean addDonor(String name, String address,
                            String bloodGroup, String district) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("name", name);
        values.put("address", address);
        values.put("blood_group", bloodGroup);
        values.put("district", district);

        return db.insert(TABLE_NAME, null, values) != -1;
    }

    public Cursor getDonors(String keyword) {
        SQLiteDatabase db = getReadableDatabase();
        String search = "%" + keyword.trim() + "%";

        return db.rawQuery(
                "SELECT * FROM donors WHERE name LIKE ? " +
                        "OR address LIKE ? OR blood_group LIKE ? " +
                        "OR district LIKE ? ORDER BY name ASC",
                new String[]{search, search, search, search}
        );
    }

    public boolean updateDonor(int id, String name, String address,
                               String bloodGroup, String district) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("name", name);
        values.put("address", address);
        values.put("blood_group", bloodGroup);
        values.put("district", district);

        return db.update(TABLE_NAME, values, "id = ?",
                new String[]{String.valueOf(id)}) > 0;
    }

    public boolean deleteDonor(int id) {
        SQLiteDatabase db = getWritableDatabase();

        return db.delete(TABLE_NAME, "id = ?",
                new String[]{String.valueOf(id)}) > 0;
    }
}
