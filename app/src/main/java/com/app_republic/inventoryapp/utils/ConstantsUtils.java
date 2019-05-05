package com.app_republic.inventoryapp.utils;

public interface ConstantsUtils {
    String DUMMY_SUPPLIER_PHONE = "0776976416";
    String WHITE_SPACE_REGEX = ".*\\s.*";
    String EMPTY_STRING = "";
    String WHITE_SPACE = " ";
    String NEW_LINE_STRING = "\n";
    String TEXT_REGEX = "^[A-Za-z0-9][A-Za-z0-9]*(?:_[A-Za-z0-9]+)*$";
    String REGEX_TO_GET_INTEGER_ONLY_FROM_STRING = "[\\D]";

    String TEXT = "TEXT";
    String NUMERIC = "NUMERIC";
    String LONG_TEXT = "LONG_TEXT";
    String PHONE = "PHONE";
    String TRUE = "TRUE";
    String FALSE = "FALSE";

    String MODE = "mode";
    String DELETE_ITEM = " delete item";
    String DELETE_ALL_DATA = "delete all data";
    String INSERT_DUMMY_ITEM = "insert dummy item";
    String FRAGMENT_ITEM_POSITION = "fragmentItemPosition";
    String SIGN_ID = " =?";
    String INTENT_TYPE_IMAGE = "image/*";
    String INTENT_TYPE_TEXT = "text/plain";
    String INTENT_TYPE_TEL = "tel:";

    String DUMMY_PRODUCT_NAME = "product";
    String DUMMY_PRODUCT_CODE = "code";
    String DUMMY_SUPPLIER_NAME = "john";
    String DUMMY_PRODUCT_DESCRIPTION = "no description found";
    String DUMMY_PRODUCT_CATEGORY = "category";
    String BUNDLE = " bundle: ";

    int LIMIT_DESCRIPTION_LENGTH = 1000;
    int LIMIT_RANDOM_VALUE = 100;

    int INVALID = -1;
    int ZERO = 0;
    int ONE = 1;
    int TWO = 2;
    int THREE = 3;
    int FOUR = 4;
    int FIVE = 5;
    int SIX = 6;
    int SEVEN = 7;
    int EIGHT = 8;
    int NINE = 9;
    int TEN = 10;

    int VALID = ONE;
    int NULLABLE_COLUMNS_COUNT = NINE;
    int COLUMNS_COUNT = TEN;
}
