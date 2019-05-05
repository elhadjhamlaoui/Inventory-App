package com.app_republic.inventoryapp.utils;

import android.content.ContentValues;
import android.content.Context;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.app_republic.inventoryapp.database.Contract;

import java.util.Random;

public class ProductUtils implements ConstantsUtils {

    private final Random random = new Random();

    public ProductUtils() {

    }

    public String[] getDummyStringValues() {

        String[] dummyValues = new String[COLUMNS_COUNT];
        dummyValues[ZERO] = DUMMY_PRODUCT_NAME + getRandomValue();
        dummyValues[ONE] = DUMMY_PRODUCT_CODE + getRandomValue();
        dummyValues[TWO] = DUMMY_PRODUCT_CATEGORY + getRandomValue();
        dummyValues[THREE] = String.valueOf(getRandomValue());
        dummyValues[FOUR] = String.valueOf(getRandomValue());
        dummyValues[FIVE] = DUMMY_SUPPLIER_NAME + getRandomValue();
        dummyValues[SIX] = DUMMY_SUPPLIER_PHONE + getRandomValue();
        dummyValues[SEVEN] = DUMMY_PRODUCT_DESCRIPTION;
        dummyValues[EIGHT] = String.valueOf(ONE);
        dummyValues[NINE] = String.valueOf(ZERO);
        return dummyValues;
    }

    public ContentValues getDummyContentValues() {
        String[] dummyValues = getDummyStringValues();

        ContentValues values = new ContentValues();
        values.put(Contract.ProductsEntry.COLUMN_PRODUCT_NAME, dummyValues[ZERO]);
        values.put(Contract.ProductsEntry.COLUMN_PRODUCT_CODE, dummyValues[ONE]);
        values.put(Contract.ProductsEntry.COLUMN_PRODUCT_CATEGORY, dummyValues[TWO]);
        values.put(Contract.ProductsEntry.COLUMN_PRICE, dummyValues[THREE]);
        values.put(Contract.ProductsEntry.COLUMN_QUANTITY, dummyValues[FOUR]);
        values.put(Contract.ProductsEntry.COLUMN_SUPPLIER_NAME, dummyValues[FIVE]);
        values.put(Contract.ProductsEntry.COLUMN_SUPPLIER_PHONE_NUMBER, dummyValues[SIX]);
        values.put(Contract.ProductsEntry.COLUMN_PRODUCT_DESCRIPTION, dummyValues[SEVEN]);
        values.put(Contract.ProductsEntry.COLUMN_PRODUCT_FAVORED, dummyValues[NINE]);
        return values;
    }

    public void showToastMsg(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public int getRandomValue() {
        return random.nextInt(LIMIT_RANDOM_VALUE);
    }

    private boolean textHasWhiteSpace(String text) {
        return text.matches(WHITE_SPACE_REGEX);
    }

    private String[] checkEnteredText(String name) {
        String flag = TRUE;
        String nameButNotMatched = EMPTY_STRING;
        String checkedValue = EMPTY_STRING;
        if (!TextUtils.isEmpty(name) && !textHasWhiteSpace(name)) {
            if (name.length() >= THREE) {
                if (name.matches(TEXT_REGEX)) {
                    flag = FALSE;
                    checkedValue = name;
                } else nameButNotMatched = name;
            }
        }
        return new String[]{flag, checkedValue, nameButNotMatched};
    }

    private String[] checkEnteredLongText(String longText) {
        String invalid = TRUE;
        String nameButNotMatched = EMPTY_STRING;
        String checkedValue = EMPTY_STRING;
        if (!TextUtils.isEmpty(longText)) {
            if (longText.length() < LIMIT_DESCRIPTION_LENGTH) {
                invalid = FALSE;
                checkedValue = longText;
            } else nameButNotMatched = longText;
        }
        return new String[]{invalid, checkedValue, nameButNotMatched};
    }

    private String[] checkEnteredNumbers(String number) {
        String invalid = TRUE;
        String checkedValue = EMPTY_STRING;
        if (!TextUtils.isEmpty(number)) {
            try {
                int numberButNotMatched = (int) Math.floor(Integer.parseInt(number));
                if (numberButNotMatched >= ZERO) {
                    invalid = FALSE;
                    checkedValue = String.valueOf(numberButNotMatched);
                }
            } catch (NumberFormatException e) {
                checkedValue = String.valueOf(INVALID);
            }

        }
        return new String[]{invalid, checkedValue, number};
    }

    private String[] checkEnteredPhoneNumber(String phone) {
        String invalid = TRUE;
        String phoneButNotMatched = EMPTY_STRING;
        String checkedValue = EMPTY_STRING;
        if (!TextUtils.isEmpty(phone)) {
            if (phone.length() >= TEN) {
                invalid = FALSE;
                checkedValue = phone;
            } else phoneButNotMatched = phone;
        }
        return new String[]{invalid, checkedValue, phoneButNotMatched};
    }

    public String[] checkIfFieldIsNull(EditText editText, String type) {
        String[] flagAndValues;
        switch (type) {
            case TEXT:
                flagAndValues = checkEnteredText(getTextEditText(editText));
                break;
            case NUMERIC:
                flagAndValues = checkEnteredNumbers(getTextEditText(editText));
                break;
            case PHONE:
                flagAndValues = checkEnteredPhoneNumber(getTextEditText(editText));
                break;
            case LONG_TEXT:
            default:
                flagAndValues = checkEnteredLongText(getTextEditText(editText));
        }
        return flagAndValues;
    }

    private String getTextEditText(EditText editText) {
        return editText.getText().toString().trim();
    }

    public boolean noNullValues(ContentValues values) {
        return (values.containsKey(Contract.ProductsEntry.COLUMN_PRODUCT_NAME)) &&
                (values.containsKey(Contract.ProductsEntry.COLUMN_PRODUCT_CODE)) &&
                (values.containsKey(Contract.ProductsEntry.COLUMN_PRODUCT_CATEGORY)) &&
                (values.containsKey(Contract.ProductsEntry.COLUMN_PRICE)) &&
                (values.containsKey(Contract.ProductsEntry.COLUMN_QUANTITY)) &&
                (values.containsKey(Contract.ProductsEntry.COLUMN_SUPPLIER_NAME)) &&
                (values.containsKey(Contract.ProductsEntry.COLUMN_SUPPLIER_PHONE_NUMBER));
    }

    public int refuseNegativeNumbers(int num) {
        if (num > ZERO) return num;
        else return ZERO;
    }

}

