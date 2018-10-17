package com.example.android.inventoryapp1.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.example.android.inventoryapp1.data.BookContract.BookEntry;

public class BookProvider extends ContentProvider {

    public static final String LOG_TAG = BookProvider.class.getSimpleName();

    private static final int BOOKS = 100;
    private static final int BOOK_ID = 101;
    private BookDbHelper mDbHelper;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(BookContract.CONTENT_AUTHORITY, BookContract.PATH_BOOKS, BOOKS);
        sUriMatcher.addURI(BookContract.CONTENT_AUTHORITY, BookContract.PATH_BOOKS + "/#", BOOK_ID);
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new BookDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase database = mDbHelper.getReadableDatabase();
        Cursor cursor;
        int match = sUriMatcher.match(uri);

        switch (match) {
            case BOOKS:
                cursor = database.query(BookEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case BOOK_ID:
                selection = BookEntry.ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(BookEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        if (getContext() != null) cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }


    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case BOOKS:
                return insertBook(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }


    private Uri insertBook(Uri uri, ContentValues values) {

        String name = values.getAsString(BookEntry.COLUMN_Product_NAME);
        if (name == null) {
            throw new IllegalArgumentException("requires a name");
        }

        Integer price = values.getAsInteger(BookEntry.COLUMN_Price);
        if (price != null && price < 0) {
            throw new IllegalArgumentException(" requires valid price");
        }

        Integer  quantity = values.getAsInteger(BookEntry.COLUMN_Quantity);
        if (quantity != null && quantity < 0) {
            throw new IllegalArgumentException(" requires valid quantity");
        }

        String supplierName = values.getAsString(BookEntry.COLUMN_Supplier_Name);
        if (supplierName == null || TextUtils.isEmpty(supplierName)){
            throw new IllegalArgumentException("Require Supplier Name");}

        String supplierContactNumber = values.getAsString(BookEntry.COLUMN_Supplier_phNo);
        if (supplierContactNumber == null || TextUtils.isEmpty(supplierContactNumber)){
            throw new IllegalArgumentException("Requires phone number");
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        long id = database.insert(BookEntry.TABLE_NAME, null, values);
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case BOOKS:
                return updateBook(uri, contentValues, selection, selectionArgs);
            case BOOK_ID:
                selection = BookEntry.ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateBook(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updateBook(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (values.containsKey(BookEntry.COLUMN_Product_NAME)) {
            String name = values.getAsString(BookEntry.COLUMN_Product_NAME);
            if (name == null) {
                throw new IllegalArgumentException(" requires a name");
            }
        }

        if (values.containsKey(BookEntry.COLUMN_Price)) {
            Integer price = values.getAsInteger(BookEntry.COLUMN_Price);
            if (price != null && price < 0) {
                throw new IllegalArgumentException(" requires valid price");
            }
        }

        if (values.containsKey(BookEntry.COLUMN_Quantity)) {
            Integer quantity = values.getAsInteger(BookEntry.COLUMN_Quantity);
            if (quantity != null && quantity < 0) {
                throw new IllegalArgumentException(" requires valid price");
            }
        }

        if(values.containsKey(BookEntry.COLUMN_Supplier_Name)){
        String supplierName =values.getAsString(BookEntry.COLUMN_Supplier_Name);
         if(supplierName == null ){
            throw new IllegalArgumentException("Require Supplier Name");}}

        if(values.containsKey(BookEntry.COLUMN_Supplier_phNo)){
        String supplierphn = values.getAsString(BookEntry.COLUMN_Supplier_phNo);
        if (supplierphn == null){
            throw new IllegalArgumentException("Requires phone number");}}

        if (values.size() == 0) {
            return 0;
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        int rowsUpdated = database.update(BookEntry.TABLE_NAME, values, selection, selectionArgs);
        if (rowsUpdated != 0  && getContext() != null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        int rowsDeleted;
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case BOOKS:
                rowsDeleted = database.delete(BookEntry.TABLE_NAME, selection, selectionArgs);
                break;

            case BOOK_ID:
                selection = BookEntry.ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(BookEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }


    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case BOOKS:
                return BookEntry.CONTENT_LIST_TYPE;
            case BOOK_ID:
                return BookEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }
}
