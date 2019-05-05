package com.app_republic.inventoryapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.app_republic.inventoryapp.utils.ConstantsUtils;


public class DatabaseHelper extends SQLiteOpenHelper implements ConstantsUtils {

    private static final String DATABASE_NAME = "inventory.db";
    private static final int DATABASE_VERSION = 1;
    private static final String Db_NAME = "shelter.db";
    private static final int Db_VERSION = 1;

    private static final String CREATE_TABLE = "CREATE TABLE ";
    private static final String PRIMARY_KEY = " PRIMARY KEY";
    private static final String AUTOINCREMENT = " AUTOINCREMENT";
    private static final String TEXT = " TEXT";
    private static final String INTEGER = " INTEGER";
    private static final String BLOB = " BLOB";
    private static final String NOT_NULL = " NOT NULL";
    private static final String DEFAULT = " DEFAULT ";
    private static final String OPEN_BRACKET = "(";
    private static final String CLOSE_BRACKET = ")";
    private static final String COMA = ",";
    private static final String SEMI_COLUMN = ";";
    private static final int ZERO = 0;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String CREATE_TABLE_PRODUCT =
                CREATE_TABLE + Contract.ProductsEntry.TABLE_NAME + OPEN_BRACKET +
                        Contract.ProductsEntry._ID + INTEGER + PRIMARY_KEY + AUTOINCREMENT + COMA +
                        Contract.ProductsEntry.COLUMN_PRODUCT_NAME + TEXT + NOT_NULL + COMA +
                        Contract.ProductsEntry.COLUMN_PRODUCT_CODE + TEXT + NOT_NULL + COMA +
                        Contract.ProductsEntry.COLUMN_PRODUCT_CATEGORY + TEXT + NOT_NULL + COMA +
                        Contract.ProductsEntry.COLUMN_PRICE + INTEGER + NOT_NULL + COMA +
                        Contract.ProductsEntry.COLUMN_QUANTITY + INTEGER + NOT_NULL + DEFAULT + ZERO + COMA +
                        Contract.ProductsEntry.COLUMN_SUPPLIER_NAME + TEXT + NOT_NULL + COMA +
                        Contract.ProductsEntry.COLUMN_SUPPLIER_PHONE_NUMBER + TEXT + NOT_NULL + COMA +
                        Contract.ProductsEntry.COLUMN_PRODUCT_DESCRIPTION + TEXT + COMA +
                        Contract.ProductsEntry.COLUMN_PRODUCT_IMAGE + BLOB + COMA +
                        Contract.ProductsEntry.COLUMN_PRODUCT_FAVORED + INTEGER + NOT_NULL + DEFAULT + ZERO + CLOSE_BRACKET + SEMI_COLUMN;

        sqLiteDatabase.execSQL(CREATE_TABLE_PRODUCT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}