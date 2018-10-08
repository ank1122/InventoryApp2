package com.example.android.inventoryapp1.data;

import android.provider.BaseColumns;

public final class BookContract {

    private BookContract() {
    }

    public static final class BookEntry implements BaseColumns {

        public final static String TABLE_NAME = "BookEntry";

        public final static String _ID = BaseColumns._ID;

        public final static String COLUMN_Product_NAME = "name";

        public final static String COLUMN_Price = "Price";

        public final static String COLUMN_Quantity = "Quantity";

        public final static String COLUMN_Supplier_Name = "Supplier_Name";
        public final static String COLUMN_Supplier_phNo = "Supplier_phone_Number";

    }

}
