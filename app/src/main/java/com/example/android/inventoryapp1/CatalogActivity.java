package com.example.android.inventoryapp1;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.inventoryapp1.data.BookContract;
import com.example.android.inventoryapp1.data.BookContract.BookEntry;
import com.example.android.inventoryapp1.data.BookDbHelper;

public class CatalogActivity extends AppCompatActivity  implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int Book_LOADER = 0;
    private BookCursorAdapter mCursorAdapter ;
    private BookDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);
        start();
    }

        public void start(){
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        ListView BookListView = (ListView) findViewById(R.id.list);
        View emptyView = findViewById(R.id.empty_view);
        BookListView.setEmptyView(emptyView);

        mCursorAdapter =new BookCursorAdapter(this,null);
        BookListView.setAdapter(mCursorAdapter);
        getLoaderManager().initLoader(Book_LOADER,null,this);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        BookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {


                startActivity(new Intent(getApplicationContext(), EditorActivity.class)
                        .setData(ContentUris.withAppendedId(BookEntry.CONTENT_URI, id)));
            }
        });
    }

    /**private void insertBook() {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(BookContract.BookEntry.COLUMN_Product_NAME, "Book");
        values.put(BookContract.BookEntry.COLUMN_Price, 200);
        values.put(BookContract.BookEntry.COLUMN_Quantity, 100);
        values.put(BookContract.BookEntry.COLUMN_Supplier_Name, "Kumar");
        values.put(BookContract.BookEntry.COLUMN_Supplier_phNo, 123456789);

        Uri newUri = getContentResolver().insert(BookEntry.CONTENT_URI, values);
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
                insertBook();
                return true;
            case R.id.action_delete_all_entries:
                // Do nothing for now
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
*/
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] projection ={
                BookEntry._ID,
                BookEntry.COLUMN_Product_NAME,
                BookEntry.COLUMN_Price,
                BookEntry.COLUMN_Quantity,
                BookEntry.COLUMN_Supplier_Name,
                BookEntry.COLUMN_Supplier_phNo};

        return new CursorLoader(this,
                BookEntry.CONTENT_URI,
                projection,
                null,
                null,
                null );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }
}
