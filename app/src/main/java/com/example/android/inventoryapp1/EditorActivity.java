package com.example.android.inventoryapp1;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.inventoryapp1.data.BookContract;
import com.example.android.inventoryapp1.data.BookDbHelper;

public class EditorActivity extends AppCompatActivity {

    private EditText mNameEditText;

    private EditText mpriceEditText;

    private EditText mquantiyEditText;

    private EditText msuppplierNameEditText;

    private EditText msupplierphnEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        // Find all relevant views that we will need to read user input from
        mNameEditText = (EditText) findViewById(R.id.edit_product_name);
        mpriceEditText = (EditText) findViewById(R.id.edit_price);
        mquantiyEditText = (EditText) findViewById(R.id.edit_quantity);
        msuppplierNameEditText = (EditText) findViewById(R.id.edit_Sname);
        msupplierphnEditText = (EditText) findViewById(R.id.edit_phnno);
    }


    private void insertBook() {
        // Read from input fields
        // Use trim to eliminate leading or trailing white space
        String nameString = mNameEditText.getText().toString().trim();
        String priceString = mpriceEditText.getText().toString().trim();
        int price = Integer.parseInt(priceString);
        String quantityString = mquantiyEditText.getText().toString().trim();
        int quantity = Integer.parseInt(quantityString);
        String supplierString = msuppplierNameEditText.getText().toString().trim();
        String phnString = msupplierphnEditText.getText().toString().trim();
        int phone = Integer.parseInt(phnString);

        // Create database helper
        BookDbHelper mDbHelper = new BookDbHelper(this);


        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(BookContract.BookEntry.COLUMN_Product_NAME, nameString);
        values.put(BookContract.BookEntry.COLUMN_Price, price);
        values.put(BookContract.BookEntry.COLUMN_Quantity, quantity);
        values.put(BookContract.BookEntry.COLUMN_Supplier_Name, supplierString);
        values.put(BookContract.BookEntry.COLUMN_Supplier_phNo, phone);

        long newRowId = db.insert(BookContract.BookEntry.TABLE_NAME, null, values);


        if (newRowId == -1) {
            // If the row ID is -1, then there was an error with insertion.
            Toast.makeText(this, "Error with saving pet", Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the insertion was successful and we can display a toast with the row ID.
            Toast.makeText(this, "Book saved with row id: " + newRowId, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                insertBook();
                finish();
                return true;
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
