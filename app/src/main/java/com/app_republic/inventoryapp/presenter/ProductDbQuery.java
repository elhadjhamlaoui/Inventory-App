package com.app_republic.inventoryapp.presenter;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.app_republic.inventoryapp.R;
import com.app_republic.inventoryapp.database.Contract;
import com.app_republic.inventoryapp.utils.ProductUtils;

public class ProductDbQuery {

    private final Context context;
    private final ProductUtils productUtils;

    public ProductDbQuery(Context context) {
        this.context = context;
        productUtils = new ProductUtils();
    }


    public Cursor getRowById(int id) {
        return context.getContentResolver().query(
                ContentUris.withAppendedId(Contract.ProductsEntry.CONTENT_URI, id)
                , null,
                Contract.ProductsEntry._ID + productUtils.SIGN_ID, new String[]{String.valueOf(id)}, null);
    }


    private Cursor queryDataByColumnName(String[] projection) {
        Uri uri;
        uri = Contract.ProductsEntry.CONTENT_URI;
        return context.getContentResolver().query(uri, projection, null, null, null);
    }


    private int ifNameOrCodeRepeatedShowToast(ContentValues values, int id) {

        int matchedId = nameOrCodeRepeated(values, Contract.ProductsEntry.COLUMN_PRODUCT_CODE);
        if (matchedId != productUtils.INVALID && matchedId != id) {
            productUtils.showToastMsg(context,
                    context.getString(R.string.repeated_input_value, Contract.ProductsEntry.COLUMN_PRODUCT_CODE, matchedId));
            return productUtils.INVALID;
        }
        matchedId = nameOrCodeRepeated(values, Contract.ProductsEntry.COLUMN_PRODUCT_NAME);
        if (matchedId != productUtils.INVALID && matchedId != id) {
            productUtils.showToastMsg(context,
                    context.getString(R.string.repeated_input_value, Contract.ProductsEntry.COLUMN_PRODUCT_NAME, matchedId));
            return productUtils.INVALID;
        }
        return productUtils.VALID;
    }


    private int nameOrCodeRepeated(ContentValues values, String columnName) {
        return (DatabaseContainsText(
                queryDataByColumnName(new String[]{Contract.ProductsEntry._ID, columnName}),
                columnName,
                values.getAsString(columnName)));

    }


    private String getUniqueNameOrCode(String columnName, String type) {
        String text = type + productUtils.getRandomValue();
        int count = productUtils.ZERO;
        while (count < productUtils.LIMIT_RANDOM_VALUE && DatabaseContainsText(
                queryDataByColumnName(new String[]{columnName}),
                columnName, text) == productUtils.INVALID) {
            text = type + productUtils.getRandomValue();
            count++;
        }
        if (count == productUtils.LIMIT_RANDOM_VALUE) {
            text = productUtils.SIGN_ID;
        }
        return text;
    }


    private ContentValues getNewDummyValues() {

        ContentValues values = productUtils.getDummyContentValues();
        String text;
        if (nameOrCodeRepeated(values, Contract.ProductsEntry.COLUMN_PRODUCT_CODE) != productUtils.INVALID) {
            text = getUniqueNameOrCode(Contract.ProductsEntry.COLUMN_PRODUCT_CODE, values.getAsString(Contract.ProductsEntry.COLUMN_PRODUCT_CODE));
            if (!text.equals(productUtils.SIGN_ID))
                values.put(Contract.ProductsEntry.COLUMN_PRODUCT_CODE, text);
        }
        if (nameOrCodeRepeated(values, Contract.ProductsEntry.COLUMN_PRODUCT_NAME) != productUtils.INVALID) {
            text = getUniqueNameOrCode(Contract.ProductsEntry.COLUMN_PRODUCT_NAME,
                    values.getAsString(Contract.ProductsEntry.COLUMN_PRODUCT_NAME));
            if (!text.equals(productUtils.SIGN_ID))
                values.put(Contract.ProductsEntry.COLUMN_PRODUCT_NAME, text);
        }
        return values;
    }


    public boolean insertData(ContentValues values) {
        if (values == null) values = getNewDummyValues();

        if (values.size() < productUtils.NULLABLE_COLUMNS_COUNT) return false;

        if (ifNameOrCodeRepeatedShowToast(values, productUtils.INVALID) == productUtils.INVALID)
            return false;

        Uri newRowUri = context.getContentResolver().insert(Contract.ProductsEntry.CONTENT_URI, values);


        if (newRowUri == null) {

            productUtils.showToastMsg(context,
                    context.getResources().getString(R.string.error_with_saving_product));
            return false;
        } else {

            productUtils.showToastMsg(context,
                    context.getResources().getString(R.string.product_saved_with_row) + ContentUris.parseId(newRowUri));
            return true;
        }
    }

    public int update(ContentValues values, String selection, int id) {

        if (ifNameOrCodeRepeatedShowToast(values, id) == productUtils.INVALID) {
            return productUtils.INVALID;
        }
        Uri uri = ContentUris.withAppendedId(Contract.ProductsEntry.CONTENT_URI, id);
        return context.getContentResolver().update(uri, values, selection, new String[]{String.valueOf(id)});
    }

    public int deleteById(int id) {
        int deletedRow = context.getContentResolver().delete(ContentUris.withAppendedId(Contract.ProductsEntry.CONTENT_URI, id)
                , Contract.ProductsEntry._ID + productUtils.SIGN_ID, new String[]{String.valueOf(id)});
        productUtils.showToastMsg(context,
                context.getResources().getString(R.string.product_deleted_with_row));
        return deletedRow;

    }


    public String[] projection() {
        return new String[]{Contract.ProductsEntry._ID,
                Contract.ProductsEntry.COLUMN_PRODUCT_NAME,
                Contract.ProductsEntry.COLUMN_PRODUCT_CODE,
                Contract.ProductsEntry.COLUMN_PRODUCT_CATEGORY,
                Contract.ProductsEntry.COLUMN_PRICE,
                Contract.ProductsEntry.COLUMN_QUANTITY,
                Contract.ProductsEntry.COLUMN_PRODUCT_IMAGE,
                Contract.ProductsEntry.COLUMN_PRODUCT_FAVORED};
    }


    private int DatabaseContainsText(Cursor cursor, String columnName, String text) {
        if (null != cursor) {
            while (cursor.moveToNext()) {

                if (text != null && text.equals(cursor.getString(cursor.getColumnIndex(columnName)))) {
                    return cursor.getInt(cursor.getColumnIndex(Contract.ProductsEntry._ID));
                }
            }
        }
        return productUtils.INVALID;
    }
}
