package com.example.android.inventoryapp1;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.inventoryapp1.data.BookContract;

public class BookCursorAdapter extends CursorAdapter {


    public BookCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }


    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        TextView nameTextView = (TextView) view.findViewById(R.id.product_name);
        TextView priceTextView = (TextView) view.findViewById(R.id.price);
        TextView quantityTextView = (TextView) view.findViewById(R.id.Quantity);
        TextView suppliernameView =(TextView) view.findViewById(R.id.supplier_name);

        Button saleButton = view.findViewById(R.id.sale_button);
        Button orderButton = view.findViewById(R.id.order_button);

        int productColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_Product_NAME);
        int priceColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_Price);
        int quantityColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_Quantity);
        int SupplierName = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_Supplier_Name);
        final String SupplierPhone = cursor.getString(cursor.getColumnIndexOrThrow(BookContract.BookEntry.COLUMN_Supplier_phNo));
        final int rowId = cursor.getInt(cursor.getColumnIndexOrThrow(BookContract.BookEntry.ID));

        String productName = cursor.getString(productColumnIndex);
        String price = cursor.getString(priceColumnIndex);
        final String quantity = cursor.getString(quantityColumnIndex);
        String supplierName =cursor.getString(SupplierName);

        saleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri newUri = ContentUris.withAppendedId(BookContract.BookEntry.CONTENT_URI, rowId);
                if (Integer.parseInt(quantity) > 0) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(BookContract.BookEntry.COLUMN_Quantity, Integer.parseInt(quantity) - 1);
                    context.getContentResolver().update(newUri, contentValues, null, null);
                } else {
                    Toast.makeText(context, R.string.last_sale_warning, Toast.LENGTH_SHORT).show();
                }
            }
        });

        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + SupplierPhone)));
            }
        });



        nameTextView.setText(productName);
        priceTextView.setText(price);
        quantityTextView.setText(quantity);
        suppliernameView.setText(supplierName);
    }
}
