package com.example.findblood;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "findblood.db";
    private static final int DATABASE_VERSION = 6; // Increment this version
    private static final String TABLE_NAME_USERS = "users";
    private static final String TABLE_NAME_DONOR = "donor"; // New donor table
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_AGE = "age"; // New column for age
    private static final String COLUMN_GENDER = "gender"; // New column for gender
    private static final String COLUMN_BLOOD_TYPE = "blood_type"; // New column for blood type
    private static final String COLUMN_REGISTER_FOR_OTHERS = "register_for_others"; // New column for registration type
    private static final String COLUMN_USER_ID = "user_id"; // New column for user ID
    private static final String COLUMN_DATE = "registration_date"; // New column for registration date

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("DatabaseHelper", "Creating tables");
        // Create users table with avatar column
        String createUsersTable = "CREATE TABLE " + TABLE_NAME_USERS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_PHONE + " TEXT, " +
                COLUMN_PASSWORD + " TEXT)";
        db.execSQL(createUsersTable);

        // Create donor table
        String createDonorTable = "CREATE TABLE " + TABLE_NAME_DONOR + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_PHONE + " TEXT, " +
                COLUMN_AGE + " INTEGER, " +
                COLUMN_GENDER + " TEXT, " +
                COLUMN_BLOOD_TYPE + " TEXT, " +
                COLUMN_REGISTER_FOR_OTHERS + " INTEGER, " +
                COLUMN_USER_ID + " INTEGER, " +
                COLUMN_DATE + " TEXT)"; // Add registration date column
        db.execSQL(createDonorTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("DatabaseHelper", "Upgrading database from version " + oldVersion + " to " + newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_DONOR); // Drop donor table if exists
        onCreate(db);
    }

    // Thêm người dùng
    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, user.getName());
        values.put(COLUMN_PHONE, user.getPhone());
        values.put(COLUMN_PASSWORD, SecurityUtils.hashPassword(user.getPassword())); // Mã hóa mật khẩu
        db.insert(TABLE_NAME_USERS, null, values);
        db.close();
    }

    // Add donor method
    public boolean addDonor(Context context, String name, String phone, int age, String gender, String bloodType, boolean registerForOthers, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Check if the phone number has already registered today
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        
        String query = "SELECT * FROM " + TABLE_NAME_DONOR + " WHERE " + COLUMN_PHONE + " = ? AND " + COLUMN_DATE + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{phone, today});

        if (cursor.getCount() > 0) {
            Toast.makeText(context, "Số điện thoại này đã được đăng ký hiến máu hôm nay.", Toast.LENGTH_SHORT).show();
            cursor.close();
            return false;
        } else {
            cursor.close();
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME, name);
            values.put(COLUMN_PHONE, phone);
            values.put(COLUMN_AGE, age);
            values.put(COLUMN_GENDER, gender);
            values.put(COLUMN_BLOOD_TYPE, bloodType);
            values.put(COLUMN_REGISTER_FOR_OTHERS, registerForOthers ? 1 : 0);
            values.put(COLUMN_USER_ID, userId);
            values.put(COLUMN_DATE, today); // Store the registration date
            db.insert(TABLE_NAME_DONOR, null, values);
            db.close();

            return true;
        }
    }

    // Get all donors
    public Cursor getAllDonors() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME_DONOR, null);
    }

    // Kiểm tra thông tin đăng nhập
    public boolean checkUser(String phone, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME_USERS + " WHERE phone = ? AND password = ?";
        Cursor cursor = db.rawQuery(query, new String[]{phone, password});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public int getUserIdByPhone(String phone) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_ID + " FROM " + TABLE_NAME_USERS + " WHERE " + COLUMN_PHONE + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{phone});
        int userId = -1; // Default value if not found

        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    // Check if the column index is valid
                    int columnIndex = cursor.getColumnIndex(COLUMN_ID);
                    if (columnIndex != -1) {
                        userId = cursor.getInt(columnIndex);
                    } else {
                        Log.e("DatabaseHelper", "Column " + COLUMN_ID + " not found in the cursor.");
                    }
                }
            } finally {
                cursor.close(); // Ensure the cursor is closed to avoid memory leaks
            }
        } else {
            Log.e("DatabaseHelper", "Cursor is null.");
        }

        return userId;
    }

    public List<Donor> getRecentDonors(int limit) {
        List<Donor> recentDonors = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_NAME + ", " + COLUMN_PHONE + ", " + COLUMN_AGE + ", " + COLUMN_BLOOD_TYPE + " FROM " + TABLE_NAME_DONOR + " ORDER BY " + COLUMN_ID + " DESC LIMIT ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(limit)});

        if (cursor != null) {
            try {
                while (cursor.moveToNext()) {
                    int nameIndex = cursor.getColumnIndex(COLUMN_NAME);
                    int phoneIndex = cursor.getColumnIndex(COLUMN_PHONE);
                    int ageIndex = cursor.getColumnIndex(COLUMN_AGE);
                    int bloodTypeIndex = cursor.getColumnIndex(COLUMN_BLOOD_TYPE);

                    // Check if indices are valid
                    if (nameIndex != -1 && phoneIndex != -1 && ageIndex != -1 && bloodTypeIndex != -1) {
                        String name = cursor.getString(nameIndex);
                        String phone = cursor.getString(phoneIndex);
                        String age = cursor.getString(ageIndex);
                        String bloodType = cursor.getString(bloodTypeIndex);
                        recentDonors.add(new Donor(name, phone, age, bloodType));
                    } else {
                        Log.e("DatabaseHelper", "One or more column indices are invalid.");
                    }
                }
            } finally {
                cursor.close();
            }
        }
        return recentDonors;
    }

    public List<Donor> getMyRecentDonors(int userId) {
        List<Donor> recentDonors = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_NAME + ", " + COLUMN_PHONE + ", " + COLUMN_AGE + ", " + COLUMN_BLOOD_TYPE + " FROM " + TABLE_NAME_DONOR + " WHERE " + COLUMN_USER_ID + " = ? " + " ORDER BY " + COLUMN_ID + " DESC";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});
        Log.d("2123", "getMyRecentDonors:  " + query);
        if (cursor != null) {
            try {
                while (cursor.moveToNext()) {
                    int nameIndex = cursor.getColumnIndex(COLUMN_NAME);
                    int phoneIndex = cursor.getColumnIndex(COLUMN_PHONE);
                    int ageIndex = cursor.getColumnIndex(COLUMN_AGE);
                    int bloodTypeIndex = cursor.getColumnIndex(COLUMN_BLOOD_TYPE);

                    // Check if indices are valid
                    if (nameIndex != -1 && phoneIndex != -1 && ageIndex != -1 && bloodTypeIndex != -1) {
                        String name = cursor.getString(nameIndex);
                        String phone = cursor.getString(phoneIndex);
                        String age = cursor.getString(ageIndex);
                        String bloodType = cursor.getString(bloodTypeIndex);
                        recentDonors.add(new Donor(name, phone, age, bloodType));
                    } else {
                        Log.e("DatabaseHelper", "One or more column indices are invalid.");
                    }
                }
            } finally {
                cursor.close();
            }
        }
        return recentDonors;
    }

    public String getUserNameByPhone(String phone) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_NAME + " FROM " + TABLE_NAME_USERS + " WHERE " + COLUMN_PHONE + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{phone});
        String userName = "";

        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndex(COLUMN_NAME);
                    if (columnIndex != -1) { // Ensure the column index is valid
                        userName = cursor.getString(columnIndex);
                    } else {
                        Log.e("DatabaseHelper", "Column " + COLUMN_NAME + " not found.");
                    }
                }
            } finally {
                cursor.close();
            }
        }
        return userName;
    }

    public boolean updateUser(int userId, String name, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_PHONE, phone);

        // Update the user in the database
        int rowsAffected = db.update(TABLE_NAME_USERS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(userId)});
        db.close();

        return rowsAffected > 0; // Return true if the update was successful
    }
}