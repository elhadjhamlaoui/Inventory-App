package com.app_republic.inventoryapp;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app_republic.inventoryapp.adapter.ProductAdapter;
import com.app_republic.inventoryapp.database.Contract;
import com.app_republic.inventoryapp.presenter.ProductDbQuery;
import com.app_republic.inventoryapp.sync.ProductCursorLoader;
import com.app_republic.inventoryapp.utils.ConstantsUtils;
import com.app_republic.inventoryapp.utils.ProductUtils;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.SearchManager.QUERY;

public class ItemFragment extends Fragment implements ConstantsUtils, ProductAdapter.ProductItemOnClickListener {

    @BindView(R.id.frg_recycler_view)
    RecyclerView frg_recycler_view;
    @BindView(R.id.frg_tv_count_msg)
    TextView frg_tv_count_msg;
    @BindView(R.id.frg_tv_error_msg)
    TextView frg_tv_error_msg;
    @BindView(R.id.frg_iv_error_image)
    ImageView frg_iv_error_image;
    @BindView(R.id.frg_fab)
    FloatingActionButton fab;
    @BindString(R.string.empty_text_msg)
    String noDataMsg;
    @BindString(R.string.count_msg)
    String countMsg;
    @BindString(R.string.invalid_input_user_catalog_activity)
    String invalidInput;
    @BindString(R.string.empty_text_msg)
    String emptyTextMsg;
    @BindString(R.string.put_value_text_msg)
    String putValueMsg;
    @BindString(R.string.no_data_for_input_value_msg)
    String noDataForInputValue;
    @BindString(R.string.action_delete_all_items)
    String deleteAllItems;
    @BindString(R.string.all_items_deleted)
    String allItemsDeletedMsg;
    @BindString(R.string.action_insert_dummy_item)
    String insertDummyItem;
    @BindString(R.string.discard)
    String discardMsg;
    @BindString(R.string.delete_all_items_dialog_msg)
    String deleteAllItemsMsg;
    private int fragmentItemPosition;
    private final Boolean BOOLEAN_TRUE = true;
    private final Boolean BOOLEAN_FALSE = false;

    private ProductAdapter productRecyclerAdapter;
    private Context context;
    private ContentResolver contentResolver;
    private Cursor cursor;
    private Bundle bundle;
    private ProductDbQuery productDbQuery;
    private ContentValues values;
    private ProductUtils productUtils;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(BOOLEAN_TRUE);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = getLayoutInflater().inflate(R.layout.fragment_item, container, BOOLEAN_FALSE);
        ButterKnife.bind(this, root);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getContext();
        contentResolver = null != context ? context.getContentResolver() : null;
        productDbQuery = new ProductDbQuery(context);
        values = new ContentValues();
        productUtils = new ProductUtils();
        if (null != savedInstanceState) {
            bundle = savedInstanceState.getBundle(BUNDLE);
        } else {
            bundle = new Bundle();
            bundle.putInt(FRAGMENT_ITEM_POSITION, fragmentItemPosition);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle(BUNDLE, bundle);
    }

    private void runCode() {
        setRecyclerAdapter();
        getAppropriateCursor(bundle, QUERY);
        setRecyclerView();
    }

    @Override
    public void onResume() {
        super.onResume();
        runCode();
    }

    private void getAppropriateCursor(Bundle bundle, String mode) {
        bundle.putString(MODE, mode);
        cursor = new ProductCursorLoader(context, contentResolver, bundle).loadInBackground();

        if (null == cursor) {
            showData(BOOLEAN_FALSE, noDataMsg);
            frg_tv_count_msg.setVisibility(View.GONE);
        } else if (cursor.getCount() <= ZERO) {
            showData(BOOLEAN_FALSE, noDataForInputValue);
        } else {
            productRecyclerAdapter.swapCursor(cursor);
            String msg = String.format(countMsg, cursor.getCount());
            showData(BOOLEAN_TRUE, msg);
        }
    }

    private void setRecyclerAdapter() {
        productRecyclerAdapter = new ProductAdapter(this);
        productRecyclerAdapter.notifyDataSetChanged();
    }

    private void setRecyclerView() {
        frg_recycler_view.setHasFixedSize(BOOLEAN_TRUE);
        frg_recycler_view.setLayoutManager(new LinearLayoutManager(getContext()));
        frg_recycler_view.setAdapter(productRecyclerAdapter);
    }

    @OnClick(R.id.frg_fab)
    void onClickFab() {
        startActivity(new Intent(context, EditorActivity.class));
    }

    public void setFragmentItemPosition(int position) {
        this.fragmentItemPosition = position;
    }

    private void showData(boolean value, String text) {

        String msg = "";
        if (value) {
            msg = text;
            frg_iv_error_image.setVisibility(View.GONE);
            frg_tv_error_msg.setVisibility(View.GONE);
            frg_recycler_view.setVisibility(View.VISIBLE);
            frg_tv_count_msg.setTextColor(context.getResources().getColor(R.color.default_text_color));
        } else {
            frg_recycler_view.setVisibility(View.GONE);
            frg_tv_error_msg.setText(text);
            frg_iv_error_image.setVisibility(View.VISIBLE);
            frg_tv_error_msg.setVisibility(View.VISIBLE);
            frg_tv_count_msg.setTextColor(context.getResources().getColor(R.color.error_msg_text_color));
        }

        frg_tv_count_msg.setText(msg);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_delete_all_data:
                showDiscardMsg(deleteAllItemsMsg, discardMsg, deleteAllItems);
                break;
            case R.id.action_insert_dummy_data:
                getAppropriateCursor(bundle, INSERT_DUMMY_ITEM);
                break;
            case R.id.action_reload_data:
                reloadData();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void showDiscardMsg(String msg, String discard, String doSomeThing) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(msg);
        builder.setPositiveButton(discard, getDialogInterfaceOnClickListener(discard));
        builder.setNegativeButton(doSomeThing, getDialogInterfaceOnClickListener(doSomeThing));

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private DialogInterface.OnClickListener getDialogInterfaceOnClickListener(String usage) {
        if (usage.equals(discardMsg)) {
            return new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                }
            };
        } else if (usage.equals(deleteAllItems)) {
            return new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    getAppropriateCursor(bundle, DELETE_ALL_DATA);
                }
            };
        } else {
            return new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            };
        }
    }


    @Override
    public void onClick(int id) {
        startActivity(new Intent(context, EditorActivity.class).
                setData(ContentUris.withAppendedId(Contract.ProductsEntry.CONTENT_URI, id)));
    }

    @Override
    public void add_sale(int id, int quantity) {
        values.put(Contract.ProductsEntry.COLUMN_QUANTITY, productUtils.refuseNegativeNumbers(--quantity));
        productDbQuery.update(values,
                Contract.ProductsEntry._ID + SIGN_ID, id);
        reloadData();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (this.isVisible()) {

            if (!isVisibleToUser) {
                closeCursor();
                setAdapterByNull();
            } else {


                getAppropriateCursor(bundle, QUERY);
            }
        }
    }

    private void reloadData() {
        setAdapterByNull();
        getAppropriateCursor(bundle, QUERY);
    }

    private void closeCursor() {
        if (null != cursor) cursor.close();
    }

    private void setAdapterByNull() {
        productRecyclerAdapter.swapCursor(null);
    }

    @Override
    public void onStop() {
        super.onStop();
        closeCursor();
    }

}

