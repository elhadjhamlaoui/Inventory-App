package com.app_republic.inventoryapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.app_republic.inventoryapp.database.Contract;
import com.app_republic.inventoryapp.presenter.ProductDbQuery;
import com.app_republic.inventoryapp.utils.ConstantsUtils;
import com.app_republic.inventoryapp.utils.ImageUtils;
import com.app_republic.inventoryapp.utils.ProductUtils;

import butterknife.BindDimen;
import butterknife.BindInt;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class EditorActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>, ConstantsUtils {

    private static final int INTENT_IMAGE = 1;
    @BindView(R.id.error_msg_name)
    TextView productNameMsg;
    @BindView(R.id.error_msg_code)
    TextView productCodeMsg;
    @BindView(R.id.error_msg_category)
    TextView productCategoryMsg;
    @BindView(R.id.error_msg_price)
    TextView productPriceMsg;
    @BindView(R.id.error_msg_quantity)
    TextView productQuantityMsg;
    @BindView(R.id.error_msg_supplier_name)
    TextView productSupplierNameMsg;
    @BindView(R.id.error_msg_supplier_phone)
    TextView productSupplierPhoneMsg;
    @BindView(R.id.error_msg_description)
    TextView productDescriptionMsg;

    @BindView(R.id.input_name)
    EditText productName;
    @BindView(R.id.input_code)
    EditText productCode;
    @BindView(R.id.input_category)
    EditText productCategory;
    @BindView(R.id.input_price)
    EditText productPrice;
    @BindView(R.id.input_quantity)
    EditText productQuantity;
    @BindView(R.id.input_supplier_name)
    EditText productSupplierName;
    @BindView(R.id.input_supplier_phone)
    EditText productSupplierPhone;
    @BindView(R.id.input_description)
    EditText productDescription;
    @BindView(R.id.image)
    ImageView productImage;
    @BindView(R.id.item_favoured_icon)
    ImageView favouredIcon;
    private final int ONE = 1;
    private final int TWO = 2;
    private final int THREE = 3;

    @BindString(R.string.details_product)
    String detailsProduct;
    @BindString(R.string.update_product)
    String updateProduct;
    @BindString(R.string.add_product)
    String addProduct;
    @BindString(R.string.action_delete_last_row)
    String deleteProduct;
    @BindString(R.string.unsaved_changes_dialog_msg)
    String unsavedChangesMsg;
    @BindString(R.string.save_changes_dialog_msg)
    String saveChangesMsg;
    @BindString(R.string.delete_one_item_dialog_msg)
    String deleteItemMsg;
    @BindString(R.string.discard)
    String discardMsg;
    @BindString(R.string.keep_editing)
    String keepMsg;
    @BindString(R.string.save_changes)
    String saveMsg;

    @BindString(R.string.error_text)
    String errorText;
    @BindString(R.string.error_numeric_value)
    String errorNumericValue;
    @BindString(R.string.error_supp_phone)
    String errorSuppPhone;
    @BindString(R.string.error_description)
    String errorDescription;
    @BindString(R.string.error_operation)
    String errorOperation;

    @BindString(R.string.tag_Icons_off)
    String tagOff;
    @BindString(R.string.tag_Icons_on)
    String tagOn;

    @BindDimen(R.dimen.item_image_size)
    int IMAGE_SIZE;
    @BindInt(R.integer.text_min_length)
    int textMinLength;
    @BindInt(R.integer.text_max_length)
    int textMaxLength;
    @BindInt(R.integer.phone_number_max_length)
    int phoneMaxLength;

    private ContentValues values;
    private ProductDbQuery productDbQuery;
    private ProductUtils productUtils;
    private Bitmap bitmapImage;
    private int updatingId;
    private boolean productTouched;
    private MenuItem saveOrEdit;
    private MenuItem deleteThisItem;
    @BindView(R.id.quantity_up_icon)
    ImageView arrowUpIcon;
    private MenuItem fillWithDummyData;
    private String mode;
    @BindView(R.id.quantity_down_icon)
    ImageView arrowDownIcon;
    private MenuItem shareMenuItem;
    private int quantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        ButterKnife.bind(this);
        initiateValues();
        setOnTouchListenerForEditTextViews();

    }


    private void initiateValues() {
        setQuantity(productUtils.ZERO);
        bitmapImage = null;
        productDbQuery = new ProductDbQuery(this);
        productUtils = new ProductUtils();
        values = new ContentValues();
    }

    private void setQuantity(int num) {
        quantity = num;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setOnTouchListenerForEditTextViews() {
        productName.setOnTouchListener(changeTagToTrueIfClicked());
        productCode.setOnTouchListener(changeTagToTrueIfClicked());
        productCategory.setOnTouchListener(changeTagToTrueIfClicked());
        productPrice.setOnTouchListener(changeTagToTrueIfClicked());
        productQuantity.setOnTouchListener(changeTagToTrueIfClicked());
        productSupplierName.setOnTouchListener(changeTagToTrueIfClicked());
        productSupplierPhone.setOnTouchListener(changeTagToTrueIfClicked());
        productDescription.setOnTouchListener(changeTagToTrueIfClicked());
        productImage.setOnTouchListener(changeTagToTrueIfClicked());
        favouredIcon.setOnTouchListener(changeTagToTrueIfClicked());
        arrowDownIcon.setOnTouchListener(changeTagToTrueIfClicked());
        arrowUpIcon.setOnTouchListener(changeTagToTrueIfClicked());
    }


    private View.OnTouchListener changeTagToTrueIfClicked() {

        return new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                productTouched = true;
                return false;
            }
        };
    }

    private void showDiscardMsg(String msg, String discard, String doSomeThing) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(msg);
        builder.setPositiveButton(discard, getDialogInterfaceOnClickListener(discard));
        builder.setNegativeButton(doSomeThing, getDialogInterfaceOnClickListener(doSomeThing));

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    @Override
    public void onBackPressed() {
        if (!productTouched) {
            super.onBackPressed();
            return;
        }

        showDiscardMsg(unsavedChangesMsg, discardMsg, keepMsg);
    }

    private DialogInterface.OnClickListener getDialogInterfaceOnClickListener(String usage) {
        if (usage.equals(saveMsg)) {

            return new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ContentValues enteredValues = getEnteredData();
                    int done = productDbQuery.update(enteredValues,
                            Contract.ProductsEntry._ID + SIGN_ID, updatingId);
                    if (done != INVALID) {
                        restartLoaderOnQueryBundleChange();
                    }
                }
            };
        } else if (usage.equals(deleteProduct)) {
            return new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (dialog != null) {
                        mode = deleteProduct;
                        restartLoaderOnQueryBundleChange();
                    }
                }
            };
        } else if (usage.equals(keepMsg)) {
            return new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                }
            };
        } else {
            return new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            };
        }
    }

    private void getComingIntents() {
        Intent comingIntent = getIntent();
        if (comingIntent.getData() != null) {
            Uri uri = comingIntent.getData();
            updatingId = (int) ContentUris.parseId(uri);
            if (mode == null) {
                setMode(detailsProduct);
                getLoaderManager().initLoader(ZERO, null, this);
            }
        } else {
            setMode(addProduct);

        }
    }


    private void setViewsAsMode(String title, int iconResourceId, boolean enableEditing, boolean showItemDummy) {
        mode = title;
        if (addProduct.equals(mode)) setTitle(mode);
        saveOrEdit.setIcon(iconResourceId);
        enableViews(enableEditing);
        fillWithDummyData.setVisible(showItemDummy);
    }

    private void setMode(String modeResourceId) {
        if (modeResourceId.equals(detailsProduct)) {
            setViewsAsMode(detailsProduct, android.R.drawable.ic_menu_edit, false, false);
        } else if (modeResourceId.equals(updateProduct)) {
            setViewsAsMode(updateProduct, android.R.drawable.ic_menu_save, true, false);
            productImage.setVisibility(View.VISIBLE);
        } else {
            setViewsAsMode(addProduct, android.R.drawable.ic_menu_save, true, true);
            deleteThisItem.setVisible(false);
            shareMenuItem.setVisible(false);
        }
    }


    private void enableViews(boolean enable) {
        productName.setEnabled(enable);
        productCode.setEnabled(enable);
        productCategory.setEnabled(enable);
        productPrice.setEnabled(enable);
        productQuantity.setEnabled(enable);
        productSupplierName.setEnabled(enable);
        productSupplierPhone.setEnabled(enable);
        productDescription.setEnabled(enable);
        productImage.setEnabled(enable);
        favouredIcon.setEnabled(enable);
        arrowDownIcon.setEnabled(enable);
        arrowUpIcon.setEnabled(enable);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        saveOrEdit = menu.findItem(R.id.action_save);
        deleteThisItem = menu.findItem(R.id.action_delete_this_item);
        fillWithDummyData = menu.findItem(R.id.action_insert_dummy_data);
        shareMenuItem = menu.findItem(R.id.action_share_product);
        getComingIntents();
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_share_product:
                shareProductDetails();
                break;
            case R.id.action_save:
                if (mode.equals(detailsProduct)) {
                    setMode(updateProduct);
                } else if (mode.equals(updateProduct)) {
                    if (productUtils.noNullValues(getEnteredData())) {
                        if (productTouched) {
                            showDiscardMsg(saveChangesMsg, keepMsg, saveMsg);
                        } else {
                            finish();
                        }
                    }
                } else if (mode.equals(addProduct)) {
                    restartLoaderOnQueryBundleChange();
                }
                break;
            case R.id.action_insert_dummy_data:
                fillFieldsWithDummyValues();
                break;
            case R.id.action_delete_this_item:
                showDiscardMsg(deleteItemMsg, keepMsg, deleteProduct);
                break;
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    private void shareProductDetails() {
        final String titleMsg = getTitle() + WHITE_SPACE + updatingId;
        final String msg =
                Contract.ProductsEntry.COLUMN_PRODUCT_NAME + WHITE_SPACE +
                        productName.getText().toString() + NEW_LINE_STRING +
                        Contract.ProductsEntry.COLUMN_PRODUCT_CODE + WHITE_SPACE +
                        productCode.getText().toString() + NEW_LINE_STRING +
                        Contract.ProductsEntry.COLUMN_PRODUCT_CATEGORY + WHITE_SPACE +
                        productCategory.getText().toString() + NEW_LINE_STRING +
                        Contract.ProductsEntry.COLUMN_PRICE + WHITE_SPACE +
                        productPrice.getText().toString() + NEW_LINE_STRING +
                        Contract.ProductsEntry.COLUMN_QUANTITY + WHITE_SPACE +
                        productQuantity.getText().toString() + NEW_LINE_STRING +
                        Contract.ProductsEntry.COLUMN_SUPPLIER_NAME + WHITE_SPACE +
                        productSupplierName.getText().toString() + NEW_LINE_STRING +
                        Contract.ProductsEntry.COLUMN_SUPPLIER_PHONE_NUMBER + WHITE_SPACE +
                        productSupplierPhone.getText().toString();
        ShareCompat.IntentBuilder.from(this).
                setChooserTitle(titleMsg).
                setText(msg).setType(INTENT_TYPE_TEXT).
                startChooser();
    }


    private void fillFieldsWithDummyValues() {
        String[] dummyValues = productUtils.getDummyStringValues();
        String text;
        String[] result = productUtils.checkIfFieldIsNull(productName, TEXT);
        if (result[ZERO].equals(TRUE)) {
            text = result[TWO] + dummyValues[ZERO];
            productName.setText(text);
        }
        result = productUtils.checkIfFieldIsNull(productCode, TEXT);
        if (result[ZERO].equals(TRUE)) {
            text = result[TWO] + dummyValues[ONE];
            productCode.setText(text);
        }
        result = productUtils.checkIfFieldIsNull(productCategory, TEXT);
        if (result[ZERO].equals(TRUE)) {
            text = result[TWO] + dummyValues[TWO];
            productCategory.setText(text);
        }
        result = productUtils.checkIfFieldIsNull(productPrice, NUMERIC);
        if (result[ZERO].equals(TRUE)) {
            text = result[TWO] + dummyValues[THREE];
            productPrice.setText(text);
        }
        result = productUtils.checkIfFieldIsNull(productQuantity, NUMERIC);
        if (result[ZERO].equals(TRUE)) {
            text = result[TWO] + dummyValues[FOUR];
            productQuantity.setText(text);
        }
        result = productUtils.checkIfFieldIsNull(productSupplierName, TEXT);
        if (result[ZERO].equals(TRUE)) {
            text = result[TWO] + dummyValues[FIVE];
            productSupplierName.setText(text);
        }
        result = productUtils.checkIfFieldIsNull(productSupplierPhone, PHONE);
        if (result[ZERO].equals(TRUE)) {
            text = result[TWO] + dummyValues[SIX];
            productSupplierPhone.setText(text);
        }
        result = productUtils.checkIfFieldIsNull(productDescription, LONG_TEXT);
        text = result[TWO] + dummyValues[SEVEN];
        productDescription.setText(text);
    }

    private ContentValues getEnteredData() {
        getValueOrShowErrorMsgIfNull(productName, productNameMsg,
                TEXT, Contract.ProductsEntry.COLUMN_PRODUCT_NAME);

        getValueOrShowErrorMsgIfNull(productCode, productCodeMsg,
                TEXT, Contract.ProductsEntry.COLUMN_PRODUCT_CODE);

        getValueOrShowErrorMsgIfNull(productCategory, productCategoryMsg,
                TEXT, Contract.ProductsEntry.COLUMN_PRODUCT_CATEGORY);

        getValueOrShowErrorMsgIfNull(productPrice, productPriceMsg,
                NUMERIC, Contract.ProductsEntry.COLUMN_PRICE);

        values.put(Contract.ProductsEntry.COLUMN_QUANTITY, quantity);

        getValueOrShowErrorMsgIfNull(productSupplierName, productSupplierNameMsg,
                TEXT, Contract.ProductsEntry.COLUMN_SUPPLIER_NAME);

        getValueOrShowErrorMsgIfNull(productSupplierPhone, productSupplierPhoneMsg,
                PHONE, Contract.ProductsEntry.COLUMN_SUPPLIER_PHONE_NUMBER);

        getValueOrShowErrorMsgIfNull(productDescription, productDescriptionMsg,
                LONG_TEXT, Contract.ProductsEntry.COLUMN_PRODUCT_DESCRIPTION);

        byte[] blobImage;
        blobImage = ImageUtils.bitmapToBytes(bitmapImage);
        values.put(Contract.ProductsEntry.COLUMN_PRODUCT_IMAGE, blobImage);
        int favouredValue = getIconTagBinaryValue(favouredIcon);
        values.put(Contract.ProductsEntry.COLUMN_PRODUCT_FAVORED, favouredValue);

        return values;
    }


    private int getIconTagBinaryValue(ImageView icon) {
        String tagFavouredIcon = icon.getTag().toString();
        int binaryValue;
        if (tagOff.equals(tagFavouredIcon)) {
            binaryValue = ZERO;
        } else {
            binaryValue = ONE;
        }
        return binaryValue;
    }

    private void setFavouredTagAndIcon(int binaryValue) {
        if (ZERO == binaryValue) {
            favouredIcon.setTag(tagOff);
            favouredIcon.setImageResource(R.drawable.ic_heart_off);
        } else {
            favouredIcon.setTag(tagOn);
            favouredIcon.setImageResource(R.drawable.ic_heart_on);
        }
    }

    private void getValueOrShowErrorMsgIfNull(EditText editText, TextView errorLabel, String type, String columnName) {
        String errorMsg;
        if (type.equals(NUMERIC)) {
            errorMsg = String.format(errorNumericValue, columnName);

        } else if (type.equals(PHONE)) {
            errorMsg = String.format(errorSuppPhone, TEN, phoneMaxLength);

        } else if (type.equals(LONG_TEXT)) {
            errorMsg = errorDescription;

        } else {
            errorMsg = String.format(errorText, columnName, textMinLength, textMaxLength);
        }

        String[] result = productUtils.checkIfFieldIsNull(editText, type);
        if (result[ZERO].equals(FALSE)) {
            values.put(columnName, result[ONE]);
            errorLabel.setVisibility(View.GONE);
        } else {
            errorLabel.setVisibility(View.VISIBLE);
            errorLabel.setText(errorMsg);
        }
    }


    private void intentToPickImageFromGallery() {
        Intent pickImageFromGallery = new Intent(Intent.ACTION_PICK);
        pickImageFromGallery.setType(INTENT_TYPE_IMAGE);
        startActivityForResult(pickImageFromGallery, INTENT_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == INTENT_IMAGE)
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                Bitmap imageBitmap = ImageUtils.uriToBitmap(this, uri, IMAGE_SIZE);
                setClickedImageView(imageBitmap);
            }
    }

    private void setClickedImageView(Bitmap imageBitmap) {
        ImageView imageView;
        imageView = productImage;
        bitmapImage = imageBitmap;
        imageView.setImageBitmap(imageBitmap);
    }

    private String getStringFromCursor(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }


    private byte[] getBlobFromCursor(Cursor cursor, String columnName) {
        return cursor.getBlob(cursor.getColumnIndex(columnName));
    }


    private int getIntegerFromCursor(Cursor cursor, String columnName) {
        return cursor.getInt(cursor.getColumnIndex(columnName));
    }

    private void showImagesIfNotNull(Cursor updatingRow) {
        byte[] imageBytes = getBlobFromCursor(updatingRow, Contract.ProductsEntry.COLUMN_PRODUCT_IMAGE);
        int imageResourceId = R.drawable.add_photo;
        if (imageBytes != null) {
            bitmapImage = ImageUtils.byteArrayToBitmap(imageBytes);
            productImage.setImageBitmap(bitmapImage);
            productImage.setVisibility(View.VISIBLE);
        } else {
            productImage.setImageResource(imageResourceId);
            productImage.setVisibility(View.INVISIBLE);
        }
    }

    @OnClick(R.id.item_favoured_icon)
    void onClickFavouredIcon() {
        int binaryValue = getIconTagBinaryValue(favouredIcon);
        int newBinaryValue = (binaryValue + ONE) % TWO;
        setFavouredTagAndIcon(newBinaryValue);
    }

    @OnClick(R.id.image)
    void onClickImage() {
        intentToPickImageFromGallery();
    }

    @OnClick(R.id.quantity_down_icon)
    void onClickArrowDownIcon() {
        if (ZERO != quantity)
            productQuantity.setText(String.valueOf(productUtils.refuseNegativeNumbers(--quantity)));
    }

    @OnClick(R.id.quantity_up_icon)
    void onClickArrowUpIcon() {
        productQuantity.setText(String.valueOf(++quantity));
    }

    @OnClick(R.id.phone_icon)
    void onClickPhoneIcon() {
        String uri = INTENT_TYPE_TEL + productSupplierPhone.getText().toString().trim();
        startActivity(new Intent(Intent.ACTION_DIAL).setData(Uri.parse(uri)));
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        int done = INVALID;
        if (mode.equals(updateProduct)) {
            done = productDbQuery.update(getEnteredData(),
                    Contract.ProductsEntry._ID + SIGN_ID, updatingId);

        } else if (mode.equals(addProduct)) {
            if (productDbQuery.insertData(getEnteredData())) done = VALID;
        } else if (mode.equals(deleteProduct)) {
            done = productDbQuery.deleteById(updatingId);
        }
        if (done != INVALID) {
            finish();
        }
        return new CursorLoader(
                this,
                Contract.ProductsEntry.CONTENT_URI,
                null,
                Contract.ProductsEntry._ID + SIGN_ID,
                new String[]{String.valueOf(updatingId)},
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (mode.equals(detailsProduct)) {
            Cursor updatingRow = productDbQuery.getRowById(updatingId);

            if (updatingRow.moveToFirst()) {

                productName.setText(getStringFromCursor(
                        updatingRow, Contract.ProductsEntry.COLUMN_PRODUCT_NAME));
                productCode.setText(getStringFromCursor(
                        updatingRow, Contract.ProductsEntry.COLUMN_PRODUCT_CODE));
                productCategory.setText(getStringFromCursor(
                        updatingRow, Contract.ProductsEntry.COLUMN_PRODUCT_CATEGORY));
                productPrice.setText(getStringFromCursor(
                        updatingRow, Contract.ProductsEntry.COLUMN_PRICE));
                String quantity = getStringFromCursor(
                        updatingRow, Contract.ProductsEntry.COLUMN_QUANTITY);
                productQuantity.setText(quantity);
                setQuantity(Integer.parseInt(quantity));
                productSupplierName.setText(getStringFromCursor(
                        updatingRow, Contract.ProductsEntry.COLUMN_SUPPLIER_NAME));
                productDescription.setText(getStringFromCursor(
                        updatingRow, Contract.ProductsEntry.COLUMN_PRODUCT_DESCRIPTION));
                productSupplierPhone.setText(getStringFromCursor(
                        updatingRow, Contract.ProductsEntry.COLUMN_SUPPLIER_PHONE_NUMBER));
                showImagesIfNotNull(updatingRow);
                setFavouredTagAndIcon(getIntegerFromCursor(
                        updatingRow, Contract.ProductsEntry.COLUMN_PRODUCT_FAVORED));
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    private void restartLoaderOnQueryBundleChange() {
        getLoaderManager().restartLoader(ZERO, null, this);
    }
}

