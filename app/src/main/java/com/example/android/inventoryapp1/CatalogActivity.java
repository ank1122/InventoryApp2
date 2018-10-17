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
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);

                Uri mCurrentUri = ContentUris.withAppendedId(BookEntry.CONTENT_URI, id);

                intent.setData(mCurrentUri);

                startActivity(intent);

               // startActivity(new Intent(getApplicationContext(), EditorActivity.class)
                 //       .setData(ContentUris.withAppendedId(BookEntry.CONTENT_URI, id)));
            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] projection ={
                BookEntry.ID,
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
