package com.app_republic.inventoryapp.sync;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.app_republic.inventoryapp.database.Contract;
import com.app_republic.inventoryapp.presenter.ProductDbQuery;
import com.app_republic.inventoryapp.utils.ConstantsUtils;

public class ProductCursorLoader extends AsyncTaskLoader<Cursor> implements ConstantsUtils {

    private static final String TAG = ProductCursorLoader.class.getSimpleName();
    private final ContentResolver contentResolver;
    private final ProductDbQuery productDbQuery;

    private final Bundle bundle;

    public ProductCursorLoader(Context context, ContentResolver contentResolver, Bundle bundle) {
        super(context);
        this.contentResolver = contentResolver;
        this.bundle = bundle;
        productDbQuery = new ProductDbQuery(context);
    }

    @Nullable
    @Override
    public Cursor loadInBackground() {
        int fragmentItemPosition = bundle.getInt(FRAGMENT_ITEM_POSITION);
        String mode = bundle.getString(MODE);
        String[] projection = productDbQuery.projection();
        String selection;
        String[] selectionArgs;

        final int ZERO = 0;
        final int ONE = 1;
        try {
            String args1stElement;
            String args2ndElement = null;

            switch (fragmentItemPosition) {
                case ZERO:
                    selection = null;
                    args1stElement = null;
                    break;
                default:
                    selection = Contract.ProductsEntry.COLUMN_PRODUCT_FAVORED + SIGN_ID;
                    args1stElement = String.valueOf(ONE);
            }

            if (null == args1stElement && null != args2ndElement) {
                selectionArgs = new String[]{args2ndElement};
            } else if (null != args1stElement && null == args2ndElement) {
                selectionArgs = new String[]{args1stElement};
            } else if (null != args1stElement) {
                selectionArgs = new String[]{args1stElement, args2ndElement};
            } else {
                selectionArgs = null;
            }

            if (null != mode) {
                if (INSERT_DUMMY_ITEM.equals(mode)) {
                    productDbQuery.insertData(null);
                } else if (DELETE_ALL_DATA.equals(mode)) {
                    contentResolver.delete(Contract.ProductsEntry.CONTENT_URI, selection, selectionArgs);
                } else if (mode.contains(DELETE_ITEM)) {
                    int id = Integer.parseInt(mode.replaceAll(
                            REGEX_TO_GET_INTEGER_ONLY_FROM_STRING, EMPTY_STRING));
                    productDbQuery.deleteById(id);
                }
            }
        } catch (Exception e) {
            Log.d(TAG, e.toString() + e.getMessage() + e.getCause() + e.getLocalizedMessage());
            return null;
        }
        return contentResolver.query(Contract.ProductsEntry.CONTENT_URI,
                projection, selection, selectionArgs, Contract.ProductsEntry.COLUMN_PRODUCT_NAME);
    }
}
