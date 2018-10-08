package com.example.android.inventoryapp1;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.android.inventoryapp1.data.BookContract;
import com.example.android.inventoryapp1.data.BookDbHelper;

public class CatalogActivity extends AppCompatActivity {


    /**
     * Database helper that will provide us access to the database
     */
    private BookDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        // To access our database, we instantiate our subclass of SQLiteOpenHelper
        // and pass the context, which is the current activity.
        mDbHelper = new BookDbHelper(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }


    private void displayDatabaseInfo() {

        BookDbHelper mDbHelper = new BookDbHelper(this);
        // Create and/or open a database to read from it
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = new String[]{
                BookContract.BookEntry.COLUMN_Product_NAME,
                BookContract.BookEntry.COLUMN_Price,
                BookContract.BookEntry.COLUMN_Quantity,
                BookContract.BookEntry.COLUMN_Supplier_Name,
                BookContract.BookEntry.COLUMN_Supplier_phNo
        };

        Cursor cursor = db.query(
                BookContract.BookEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        TextView displayView = (TextView) findViewById(R.id.text_book);

        try {

            displayView.setText("The Books table contains " + cursor.getCount() + " Books.\n\n");
            displayView.append(BookContract.BookEntry.COLUMN_Product_NAME + " - " +
                    BookContract.BookEntry.COLUMN_Price + " - " +
                    BookContract.BookEntry.COLUMN_Quantity + " - " +
                    BookContract.BookEntry.COLUMN_Supplier_Name + " - " +
                    BookContract.BookEntry.COLUMN_Supplier_phNo + "\n");

            // Figure out the index of each column
            int productColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_Product_NAME);
            int priceColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_Price);
            int quantityColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_Quantity);
            int SupplierColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_Supplier_Name);
            int phoneColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_Supplier_phNo);

            while (cursor.moveToNext()) {

                String currentName = cursor.getString(productColumnIndex);
                int currentPrice = cursor.getInt(priceColumnIndex);
                int currentQuantity = cursor.getInt(quantityColumnIndex);
                String currentSupplier = cursor.getString(SupplierColumnIndex);
                String currentPhone_No = cursor.getString(phoneColumnIndex);

                displayView.append(("\n" +
                        currentName + " - " +
                        currentPrice + " - " +
                        currentQuantity + " - " +
                        currentSupplier + " - " +
                        currentPhone_No + " - "));
            }
        } finally {
            cursor.close();
        }
    }

    private void insertPet() {
        // Gets the database in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(BookContract.BookEntry.COLUMN_Product_NAME, "Book");
        values.put(BookContract.BookEntry.COLUMN_Price, 200);
        values.put(BookContract.BookEntry.COLUMN_Quantity, 100);
        values.put(BookContract.BookEntry.COLUMN_Supplier_Name, "Kumar");
        values.put(BookContract.BookEntry.COLUMN_Supplier_phNo, 123456789);

        long newRowId = db.insert(BookContract.BookEntry.TABLE_NAME, null, values);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_insert_dummy_data:
                insertPet();
                displayDatabaseInfo();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
