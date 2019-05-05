package com.app_republic.inventoryapp.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.app_republic.inventoryapp.R;
import com.app_republic.inventoryapp.utils.ProductUtils;


public class ProductProvider extends ContentProvider {

    private static final String LOG_TAG = ProductProvider.class.getSimpleName() + " : ";
    private static final int PRODUCT = 100;
    private static final int PRODUCT_ID = 101;

    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        MATCHER.addURI(Contract.CONTENT_AUTHORITY, Contract.PRODUCT_PATH, PRODUCT);

        MATCHER.addURI(Contract.CONTENT_AUTHORITY, Contract.PRODUCT_PATH + "/#", PRODUCT_ID);
    }

    private ProductUtils productUtils;
    private DatabaseHelper dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = new DatabaseHelper(getContext());
        productUtils = new ProductUtils();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        int match = MATCHER.match(uri);
        Cursor cursor;
        switch (match) {
            case PRODUCT:
                cursor = db.query(
                        Contract.ProductsEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null, null, sortOrder);
                break;
            case PRODUCT_ID:
                cursor = db.query(
                        Contract.ProductsEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException(LOG_TAG + getContext().getString(R.string.error_displaying_data) + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        int match = MATCHER.match(uri);

        if (match == PRODUCT) {
            return Contract.ProductsEntry.PRODUCT_LIST_TYPE;
        } else {
            return Contract.ProductsEntry.PRODUCT_ITEM_TYPE;
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        int match = MATCHER.match(uri);

        if (match == PRODUCT_ID) {
            throw new IllegalArgumentException(LOG_TAG + getContext().getString(R.string.error_inserting_data) + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return insertData(uri, values);
    }


    private Uri insertData(Uri uri, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int newRowId = (int) db.insert(Contract.ProductsEntry.TABLE_NAME, null, values);
        if (newRowId == productUtils.INVALID) {
            Log.e(LOG_TAG, getContext().getString(R.string.error_inserting_data) + uri);
            return null;

        }

        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.withAppendedPath(uri, String.valueOf(newRowId));
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int match = MATCHER.match(uri);
        int deletedRows;
        switch (match) {
            case PRODUCT:
                deletedRows = db.delete(Contract.ProductsEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case PRODUCT_ID:
                deletedRows = db.delete(Contract.ProductsEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                Log.e(LOG_TAG, getContext().getString(R.string.error_deleting_data) + uri);
                deletedRows = productUtils.INVALID;

        }
        getContext().getContentResolver().notifyChange(uri, null);
        return deletedRows;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int match = MATCHER.match(uri);

        if (match == PRODUCT) {

            Log.e(LOG_TAG, getContext().getString(R.string.error_updating_data) + uri);
            return productUtils.INVALID;

        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int affectRows = db.update(Contract.ProductsEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);

        getContext().getContentResolver().notifyChange(uri, null);
        return affectRows;
    }

}
