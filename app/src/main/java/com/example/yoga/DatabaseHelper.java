package com.example.yoga;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String databaseName = "SignLog.db";

    public DatabaseHelper(@Nullable Context context) {
        super(context, "SignLog.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase MyDatabase) {
        //Create users table
        MyDatabase.execSQL("CREATE TABLE users(" +
                "email TEXT PRIMARY KEY, " +
                "password TEXT, " +
                "lastName TEXT DEFAULT NULL, " +
                "firstName TEXT DEFAULT NULL, " +
                "birthday TEXT DEFAULT NULL, " +
                "gender TEXT DEFAULT NULL, " +
                "avatar TEXT DEFAULT NULL)");
        //Create yoga table
        MyDatabase.execSQL("CREATE TABLE yoga(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_email TEXT, " +  // foreign key
                "name TEXT, " +
                "location TEXT, " +
                "date TEXT, " +
                "parking_available TEXT, " +
                "length INTEGER, " +
                "difficulty TEXT, " +
                "description TEXT DEFAULT NULL, " +
                "image TEXT, " +
                "partner TEXT, " +
                "addedTime TEXT DEFAULT CURRENT_TIMESTAMP, " +
                "updateTime TEXT DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY(user_email) REFERENCES users(email))");

        //Create yoga manager
        MyDatabase.execSQL("CREATE TABLE observation(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "hike_id INTEGER, " +  // foreign key
                "comment TEXT DEFAULT NULL, " +
                "image TEXT DEFAULT NULL, " +
                "addedTime TEXT DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY(hike_id) REFERENCES hike(id))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int i, int i1) {
        MyDB.execSQL("drop Table if exists users");
    }

    public Boolean insertData(String email, String password) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", email);
        contentValues.put("password", password);

        // Add other filed with values null
        contentValues.put("lastName", (String) null);
        contentValues.put("firstName", (String) null);
        contentValues.put("birthday", (String) null);
        contentValues.put("gender", (String) null);
        contentValues.put("avatar", (String) null);

        long result = MyDatabase.insert("users", null, contentValues);

        return result != -1;
    }

    public Boolean checkEmail(String email) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        Cursor cursor = MyDatabase.rawQuery("Select * from users where email = ?", new String[]{email});
        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public Boolean checkEmailPassword(String email, String password) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        Cursor cursor = MyDatabase.rawQuery("Select * from users where email = ? and password = ?", new String[]{email, password});
        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public List<String> getAllUserEmails() {
        List<String> emails = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT email FROM users", null);

        if (cursor.moveToFirst()) {
            do {
                String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
                emails.add(email);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return emails;
    }

    public long insertYogaData(String userEmail, String name, String location, String date,
                               String parkingAvailable, String length, String difficulty,
                               String description, String image, String partners, String addedTime, String updatedTime) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("user_email", userEmail);
        contentValues.put("name", name);
        contentValues.put("location", location);
        contentValues.put("date", date);
        contentValues.put("parking_available", parkingAvailable);
        contentValues.put("length", length);
        contentValues.put("difficulty", difficulty);
        contentValues.put("description", description);
        contentValues.put("image", image);
        contentValues.put("partner", partners);
        contentValues.put("addedTime", addedTime);
        contentValues.put("updateTime", updatedTime);

        return MyDatabase.insert("hike", null, contentValues);
    }

    public ArrayList<ModelYoga> getAllYogas(String userEmail) {
        ArrayList<ModelYoga> hikeList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM hike WHERE user_email = ? AND user_email IS NOT NULL";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{userEmail});

        try {
            if (cursor.moveToFirst()) {
                do {
                    ModelYoga hike = new ModelYoga();

                    hike.setId(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow("id"))));
                    hike.setUserEmail(cursor.getString(cursor.getColumnIndexOrThrow("user_email")));
                    hike.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                    hike.setLocation(cursor.getString(cursor.getColumnIndexOrThrow("location")));
                    hike.setDate(cursor.getString(cursor.getColumnIndexOrThrow("date")));
                    hike.setParkingAvailable(cursor.getString(cursor.getColumnIndexOrThrow("parking_available")));
                    hike.setLength(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow("length"))));
                    hike.setDifficulty(cursor.getString(cursor.getColumnIndexOrThrow("difficulty")));
                    hike.setDescription(cursor.getString(cursor.getColumnIndexOrThrow("description")));
                    hike.setImage(cursor.getString(cursor.getColumnIndexOrThrow("image")));
                    hike.setPartner(cursor.getString(cursor.getColumnIndexOrThrow("partner")));
                    hike.setAddedTime(cursor.getString(cursor.getColumnIndexOrThrow("addedTime")));
                    hike.setUpdateTime(cursor.getString(cursor.getColumnIndexOrThrow("updateTime")));

                    hikeList.add(hike);
                } while (cursor.moveToNext());
            }
        } finally {
            cursor.close();
            db.close();
        }
        return hikeList;
    }


    public ArrayList<ModelYoga> getSearchYoga(String query) {
        ArrayList<ModelYoga> searchResultList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM hike WHERE name LIKE ? OR location LIKE ?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{"%" + query + "%", "%" + query + "%"});

        if (cursor.moveToFirst()) {
            do {
                ModelYoga hike = new ModelYoga();

                hike.setId(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow("id"))));
                hike.setUserEmail(cursor.getString(cursor.getColumnIndexOrThrow("user_email")));
                hike.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                hike.setLocation(cursor.getString(cursor.getColumnIndexOrThrow("location")));
                hike.setDate(cursor.getString(cursor.getColumnIndexOrThrow("date")));
                hike.setParkingAvailable(cursor.getString(cursor.getColumnIndexOrThrow("parking_available")));
                hike.setLength(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow("length"))));
                hike.setDifficulty(cursor.getString(cursor.getColumnIndexOrThrow("difficulty")));
                hike.setDescription(cursor.getString(cursor.getColumnIndexOrThrow("description")));
                hike.setImage(cursor.getString(cursor.getColumnIndexOrThrow("image")));
                hike.setPartner(cursor.getString(cursor.getColumnIndexOrThrow("partner")));
                hike.setAddedTime(cursor.getString(cursor.getColumnIndexOrThrow("addedTime")));
                hike.setUpdateTime(cursor.getString(cursor.getColumnIndexOrThrow("updateTime")));

                searchResultList.add(hike);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return searchResultList;
    }

    public void deleteAllYoga() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("hike", null, null);
        db.close();
    }

    public void deleteHike(String hikeId) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("hike", "id=?", new String[]{hikeId});
        db.close();
    }

    public ModelYoga getYogaById(String hikeId) {
        SQLiteDatabase db = this.getReadableDatabase();
        ModelYoga hike = null;

        Cursor cursor = db.query(
                "hike",
                null,
                "id=?",
                new String[]{hikeId},
                null,
                null,
                null
        );

        try {
            if (cursor.moveToFirst()) {
                hike = new ModelYoga();

                hike.setId(cursor.getString(cursor.getColumnIndexOrThrow("id")));
                hike.setUserEmail(cursor.getString(cursor.getColumnIndexOrThrow("user_email")));
                hike.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                hike.setLocation(cursor.getString(cursor.getColumnIndexOrThrow("location")));
                hike.setDate(cursor.getString(cursor.getColumnIndexOrThrow("date")));
                hike.setParkingAvailable(cursor.getString(cursor.getColumnIndexOrThrow("parking_available")));
                hike.setLength(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow("length"))));
                hike.setDifficulty(cursor.getString(cursor.getColumnIndexOrThrow("difficulty")));
                hike.setDescription(cursor.getString(cursor.getColumnIndexOrThrow("description")));
                hike.setImage(cursor.getString(cursor.getColumnIndexOrThrow("image")));
                hike.setPartner(cursor.getString(cursor.getColumnIndexOrThrow("partner")));
                hike.setAddedTime(cursor.getString(cursor.getColumnIndexOrThrow("addedTime")));
                hike.setUpdateTime(cursor.getString(cursor.getColumnIndexOrThrow("updateTime")));
            }
        } finally {
            cursor.close();
            db.close();
        }
        return hike;
    }

    public boolean updateYogaData(String id, String name, String location, String date,
                                  String parkingAvailable, String length, String difficulty,
                                  String description, String image, String partners, String addedTime, String updateTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("name", name);
        contentValues.put("location", location);
        contentValues.put("date", date);
        contentValues.put("parking_available", parkingAvailable);
        contentValues.put("length", length);
        contentValues.put("difficulty", difficulty);
        contentValues.put("description", description);
        contentValues.put("image", image);
        contentValues.put("partner", partners);
        contentValues.put("addedTime", addedTime);
        contentValues.put("updateTime", updateTime);

        int rowsAffected = db.update("hike", contentValues, "id=?", new String[]{id});
        db.close();

        return rowsAffected > 0;
    }

    // Trong class DatabaseHelper

    public long insertManagerData(String hikeId, String name, String comment, String image, String timeStamp) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("hike_id", hikeId);
        contentValues.put("name", name);
        contentValues.put("comment", comment);
        contentValues.put("image", image);
        contentValues.put("addedTime", timeStamp);

        return MyDatabase.insert("observation", null, contentValues);
    }

    public ArrayList<ModelManager> getAllManagers(String hikeId) {
        ArrayList<ModelManager> observationList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM observation WHERE hike_id = ?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{hikeId});

        try {
            if (cursor.moveToFirst()) {
                do {
                    ModelManager observation = new ModelManager();

                    observation.setId(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow("id"))));
                    observation.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                    observation.setyogaId(cursor.getString(cursor.getColumnIndexOrThrow("hike_id")));
                    observation.setComment(cursor.getString(cursor.getColumnIndexOrThrow("comment")));
                    observation.setImage(cursor.getString(cursor.getColumnIndexOrThrow("image")));
                    observation.setAddedTime(cursor.getString(cursor.getColumnIndexOrThrow("addedTime")));

                    observationList.add(observation);
                } while (cursor.moveToNext());
            }
        } finally {
            cursor.close();
            db.close();
        }

        return observationList;
    }


    public void deleteManager(String observationId) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("observation", "id=?", new String[]{observationId});
        db.close();
    }


    public void deleteAllManager() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("observation", null, null);
        db.close();
    }

    public ArrayList<ModelManager> getSearchManager(String query) {
        ArrayList<ModelManager> searchResultList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM observation WHERE name LIKE ? OR comment LIKE ?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{"%" + query + "%", "%" + query + "%"});

        if (cursor.moveToFirst()) {
            do {
                ModelManager observation = new ModelManager();

                observation.setId(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow("id"))));
                observation.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                observation.setyogaId(cursor.getString(cursor.getColumnIndexOrThrow("hike_id")));
                observation.setComment(cursor.getString(cursor.getColumnIndexOrThrow("comment")));
                observation.setImage(cursor.getString(cursor.getColumnIndexOrThrow("image")));
                observation.setAddedTime(cursor.getString(cursor.getColumnIndexOrThrow("addedTime")));

                searchResultList.add(observation);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return searchResultList;
    }

    public ModelManager getManagerById(String observationId) {
        SQLiteDatabase db = this.getReadableDatabase();
        ModelManager observation = null;

        Cursor cursor = db.query(
                "observation",
                null,
                "id=?",
                new String[]{observationId},
                null,
                null,
                null
        );

        try {
            if (cursor.moveToFirst()) {
                observation = new ModelManager();

                observation.setId(cursor.getString(cursor.getColumnIndexOrThrow("id")));
                observation.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                observation.setyogaId(cursor.getString(cursor.getColumnIndexOrThrow("hike_id")));
                observation.setComment(cursor.getString(cursor.getColumnIndexOrThrow("comment")));
                observation.setImage(cursor.getString(cursor.getColumnIndexOrThrow("image")));
                observation.setAddedTime(cursor.getString(cursor.getColumnIndexOrThrow("addedTime")));
            }
        } finally {
            cursor.close();
            db.close();
        }

        return observation;
    }

    public boolean updateManagerData(String observationId, String name, String comment, String image, String timeStamp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("name", name);
        contentValues.put("comment", comment);
        contentValues.put("image", image);
        contentValues.put("addedTime", timeStamp);

        int rowsAffected = db.update("observation", contentValues, "id=?", new String[]{observationId});
        db.close();

        return rowsAffected > 0;
    }
}

