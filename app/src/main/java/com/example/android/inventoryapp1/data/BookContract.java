package com.example.android.inventoryapp1.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class BookContract {

    private BookContract() { }

    public static final String CONTENT_AUTHORITY = "com.example.android.inventoryapp1";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_BOOKS = "BookEntry";


    public static final class BookEntry implements BaseColumns {


        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_BOOKS);


        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BOOKS;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BOOKS;

        public final static String TABLE_NAME = "BookEntry";

        public final static String ID = BaseColumns._ID;

        public final static String COLUMN_Product_NAME = "name";

        public final static String COLUMN_Price = "Price";

        public final static String COLUMN_Quantity = "Quantity";

        public final static String COLUMN_Supplier_Name = "Supplier_Name";
        public final static String COLUMN_Supplier_phNo = "Supplier_phone_Number";

    }

}
