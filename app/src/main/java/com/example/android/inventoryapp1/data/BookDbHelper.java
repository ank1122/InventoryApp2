package com.example.android.inventoryapp1.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BookDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = BookDbHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "bookstore.db";


    private static final int DATABASE_VERSION = 1;


    public BookDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the pets table
        String SQL_CREATE_BOOKS_TABLE = "CREATE TABLE " + BookContract.BookEntry.TABLE_NAME + " ("
                + BookContract.BookEntry.COLUMN_Product_NAME + " TEXT PRIMARY KEY , "
                + BookContract.BookEntry.COLUMN_Price + " INTEGER NOT NULL, "
                + BookContract.BookEntry.COLUMN_Quantity + " INTEGER NOT NULL, "
                + BookContract.BookEntry.COLUMN_Supplier_Name + " TEXT NOT NULL, "
                + BookContract.BookEntry.COLUMN_Supplier_phNo + " INTEGER NOT NULL DEFAULT 0);";

        db.execSQL(SQL_CREATE_BOOKS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // The database is still at version 1, so there's nothing to do be done here.
        final String DELETE_PRODUCT_TABLE =
                "DROP TABLE IF EXISTS " + BookContract.BookEntry.TABLE_NAME;
        db.execSQL(DELETE_PRODUCT_TABLE);
        onCreate(db);
    }
}
