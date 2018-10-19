package com.example.android.inventoryapp1;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.inventoryapp1.data.BookContract;
import com.example.android.inventoryapp1.data.BookContract.BookEntry;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private EditText mNameEditText;

    private EditText mpriceEditText;

    private EditText mquantiyEditText;

    private EditText msuppplierNameEditText;

    private EditText msupplierphnEditText;

    private Uri mCurrentUri;

    private String currentBookName;
    private int currentBookQuantity;
    private int currentBookPrice;
    private String currentSupplierName;
    private String currentSupplierPhone;

    private boolean isBookChanged = false;
    private boolean proceed = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem addUpdateMenuItem = menu.findItem(R.id.action_save);

        if (mCurrentUri == null) {
            MenuItem deleteMenuItem = menu.findItem(R.id.action_delete);
            deleteMenuItem.setVisible(false);
            addUpdateMenuItem.setTitle(R.string.menu_item_add);
        } else {
            addUpdateMenuItem.setTitle(R.string.menu_item_update);
        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                insertBook();
                if(proceed)
                finish();
                break;
            case android.R.id.home:
                onBackPressed();
                break;

            case R.id.action_delete:
                showDeleteConfirmationDialog();
                break;
        }
        return true;
    }

    private void start(){

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mCurrentUri = getIntent().getData();
        if (mCurrentUri == null) {
            setTitle(R.string.add_product_title);
            invalidateOptionsMenu();
        } else {
            setTitle(R.string.edit_product_title);
            getLoaderManager().initLoader(1, null, this);
        }

       Button increaseQuantityButton = (Button) findViewById(R.id.increase_quantity);
        Button decreaseQuantityButton = (Button) findViewById(R.id.decrease_quantity);
        Button orderButton = (Button) findViewById(R.id.contact_supplier);

        mNameEditText = (EditText) findViewById(R.id.edit_product_name);
        mpriceEditText = (EditText) findViewById(R.id.edit_price);
        mquantiyEditText = (EditText) findViewById(R.id.edit_quantity);
        msuppplierNameEditText = (EditText) findViewById(R.id.edit_Sname);
        msupplierphnEditText = (EditText) findViewById(R.id.edit_phnno);
        mquantiyEditText.setText("0");

        increaseQuantityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(mquantiyEditText.getText())) {
                    mquantiyEditText.setText(String.valueOf(Integer.parseInt(mquantiyEditText.getText().toString()) + 1));
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.quantity_toast), Toast.LENGTH_SHORT).show();
                }
            }
        });

        decreaseQuantityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mquantiyEditText.getText().toString().equals("0") && !TextUtils.isEmpty(mquantiyEditText.getText())) {
                    mquantiyEditText.setText(String.valueOf(Integer.parseInt(mquantiyEditText.getText().toString()) - 1));
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.quantity_toast), Toast.LENGTH_SHORT).show();
                }
            }
        });

        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + currentSupplierPhone)));
            }
        });

        mNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (mCurrentUri != null) {
                    isBookChanged = (!currentBookName.equals(charSequence.toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mpriceEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (mCurrentUri != null) {
                    isBookChanged = (!String.valueOf(currentBookPrice).equals(charSequence.toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mquantiyEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (mCurrentUri != null) {
                    isBookChanged = (!String.valueOf(currentBookQuantity).equals(charSequence.toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

     msuppplierNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (mCurrentUri != null) {
                    isBookChanged = (!currentSupplierName.equals(charSequence.toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        msupplierphnEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (mCurrentUri != null) {
                    isBookChanged = (!currentSupplierPhone.equals(charSequence.toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void insertBook() {
        String nameString = mNameEditText.getText().toString().trim();
        String price = mpriceEditText.getText().toString().trim();
        String quantity = mquantiyEditText.getText().toString().trim();
        String supplierString = msuppplierNameEditText.getText().toString().trim();
        String phone = msupplierphnEditText.getText().toString().trim();

        if (mCurrentUri == null || TextUtils.isEmpty(nameString) || TextUtils.isEmpty(price) ||
                TextUtils.isEmpty(quantity) || TextUtils.isEmpty(supplierString) || TextUtils.isEmpty(phone) ) {
            if (TextUtils.isEmpty(nameString)) {
                Toast.makeText(this, R.string.name_toast, Toast.LENGTH_SHORT).show();
                proceed = false;
                return;
            } else {
                proceed = true;
            }

            if (TextUtils.isEmpty(String.valueOf(price))) {
                Toast.makeText(this, R.string.price_toast, Toast.LENGTH_SHORT).show();
                proceed = false;
                return;
            } else {
                proceed = true;
            }

            if (TextUtils.isEmpty(String.valueOf(quantity))) {
                Toast.makeText(this, R.string.quantity_toast, Toast.LENGTH_SHORT).show();
                proceed = false;
                return;
            } else {
                proceed = true;
            }


            if (TextUtils.isEmpty(supplierString)) {
                Toast.makeText(this, R.string.supplier_name_toast, Toast.LENGTH_SHORT).show();
                proceed = false;
                return;
            } else {
                proceed = true;
            }

            if(TextUtils.isEmpty(phone)){
                Toast.makeText(this,R.string.supplier_phone_toast,Toast.LENGTH_SHORT).show();
                proceed =false;
                return;
            }else
                proceed =true;
        }

            ContentValues values = new ContentValues();
            values.put(BookContract.BookEntry.COLUMN_Product_NAME, nameString);
            values.put(BookContract.BookEntry.COLUMN_Price, price);
            values.put(BookContract.BookEntry.COLUMN_Quantity, quantity);
            values.put(BookContract.BookEntry.COLUMN_Supplier_Name, supplierString);
            values.put(BookContract.BookEntry.COLUMN_Supplier_phNo, phone);


            if (mCurrentUri == null) {
                Uri newUri = getContentResolver().insert(BookEntry.CONTENT_URI, values);

                if (newUri == null) {
                    Toast.makeText(this, getString(R.string.editor_update_failed),
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, getString(R.string.editor_update_successful),
                            Toast.LENGTH_SHORT).show();
                }
            } else {

                int rowsAffected = getContentResolver().update(mCurrentUri, values, null, null);

                if (rowsAffected == 0) {
                    Toast.makeText(this, getString(R.string.editor_update_failed),
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, getString(R.string.editor_update_successful),
                            Toast.LENGTH_SHORT).show();
                }
            }
        }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                BookEntry.ID,
                BookEntry.COLUMN_Product_NAME,
                BookEntry.COLUMN_Price,
                BookEntry.COLUMN_Quantity,
                BookEntry.COLUMN_Supplier_Name,
                BookEntry.COLUMN_Supplier_phNo,
        };

        return new CursorLoader(this, mCurrentUri, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }
        if (cursor.moveToFirst()) {
            currentBookName = cursor.getString(cursor.getColumnIndexOrThrow(BookEntry.COLUMN_Product_NAME));
            currentBookPrice = cursor.getInt(cursor.getColumnIndexOrThrow(BookEntry.COLUMN_Price));
            currentBookQuantity = cursor.getInt(cursor.getColumnIndexOrThrow(BookEntry.COLUMN_Quantity));
            currentSupplierName = cursor.getString(cursor.getColumnIndexOrThrow(BookEntry.COLUMN_Supplier_Name));
            currentSupplierPhone = cursor.getString(cursor.getColumnIndexOrThrow(BookEntry.COLUMN_Supplier_phNo));

            mNameEditText.setText(currentBookName);
            mpriceEditText.setText(String.valueOf(currentBookPrice));
            mquantiyEditText.setText(String.valueOf(currentBookQuantity));
            msuppplierNameEditText.setText(currentSupplierName);
            msupplierphnEditText.setText(currentSupplierPhone);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mNameEditText.setText("");
        mpriceEditText.setText("");
        mquantiyEditText.setText("");
        msuppplierNameEditText.setText("");
        msupplierphnEditText.setText("");
    }

    private void showUnsavedChangesDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.dialog_unsave_changes);
        builder.setNegativeButton(R.string.dialog_response_keep_editing, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (dialogInterface != null) {
                    dialogInterface.dismiss();
                }
            }
        });

        builder.setPositiveButton(R.string.dialog_response_discard, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                isBookChanged = false;
                finish();
            }
        });

        builder.create().show();
    }

    private void deleteProduct() {
        if (mCurrentUri != null) {
            int rowsDeleted = getContentResolver().delete(mCurrentUri, null, null);
            if (rowsDeleted == 0) {
                Toast.makeText(this, R.string.toast_error_message_delete, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, R.string.toast_success_message_delete, Toast.LENGTH_SHORT).show();
            }
        }

        finish();
    }

    private void showDeleteConfirmationDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.dialog_delete_);
        builder.setPositiveButton(R.string.dialog_delete_response_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                deleteProduct();
            }
        });

        builder.setNegativeButton(R.string.dialog_delete_response_no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if (dialog != null)
                    dialog.dismiss();
            }
        });

        builder.create().show();
    }

    @Override
    public void onBackPressed() {
        if (isBookChanged) {
            showUnsavedChangesDialog();
        } else {
            super.onBackPressed();
        }
    }

    }




