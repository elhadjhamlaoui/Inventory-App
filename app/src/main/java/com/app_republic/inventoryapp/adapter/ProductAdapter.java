package com.app_republic.inventoryapp.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.app_republic.inventoryapp.R;
import com.app_republic.inventoryapp.database.Contract;
import com.app_republic.inventoryapp.utils.ImageUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private static final String DOLLAR_SIGN = "$";
    private static final int DUMMY_PRICE = 121;
    private final ProductItemOnClickListener productItemOnClickListener;
    private Context context;
    private Cursor cursorProducts;

    public ProductAdapter(ProductItemOnClickListener productItemOnClickListener) {
        this.productItemOnClickListener = productItemOnClickListener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View root = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        return new ProductViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        if (!cursorProducts.isClosed() && null != cursorProducts) {
            if (cursorProducts.moveToPosition(position))
                bindViewsWithCursorValues(holder, cursorProducts);
        }
    }

    @Override
    public int getItemCount() {
        if (null == cursorProducts) return 0;
        return cursorProducts.getCount();
    }


    private void setQuantityTextView(Context context, TextView textView, int quantity) {
        if (quantity == 0) {
            textView.setText(context.getString(R.string.out_of_stock));
        } else {
            textView.setText(context.getString(R.string.quantity_label) + String.valueOf(quantity));
        }
    }


    private void setPriceTextView(Context context, TextView textView, int price) {
        if (price == 0) {
            textView.setText(context.getString(R.string.free_item));
        } else {
            String productPrice = String.valueOf(price + DUMMY_PRICE) + DOLLAR_SIGN;
            textView.setText(productPrice);
        }
    }

    private void setItemImage(ImageView imageView, byte[] imageBytes) {
        if (imageBytes == null) {
            imageView.setImageResource(R.drawable.photo);
        } else {
            imageView.setImageBitmap(ImageUtils.byteArrayToBitmap(imageBytes));
        }
    }

    private void bindViewsWithCursorValues(ProductViewHolder itemViewHolder, Cursor cursor) {
        int indexProductId = cursor.getColumnIndex(Contract.ProductsEntry._ID);
        int indexProductName = cursor.getColumnIndex(Contract.ProductsEntry.COLUMN_PRODUCT_NAME);
        int indexProductCode = cursor.getColumnIndex(Contract.ProductsEntry.COLUMN_PRODUCT_CODE);
        int indexProductCategory = cursor.getColumnIndex(Contract.ProductsEntry.COLUMN_PRODUCT_CATEGORY);
        int indexProductPrice = cursor.getColumnIndex(Contract.ProductsEntry.COLUMN_PRICE);
        int indexQuantity = cursor.getColumnIndex(Contract.ProductsEntry.COLUMN_QUANTITY);
        int indexImage = cursor.getColumnIndex(Contract.ProductsEntry.COLUMN_PRODUCT_IMAGE);

        int index = cursor.getInt(indexProductId);
        String productName = cursor.getString(indexProductName).trim();
        String productCode = cursor.getString(indexProductCode).trim();
        String productCategory = cursor.getString(indexProductCategory).trim();
        int productPrice = Integer.parseInt(cursor.getString(indexProductPrice));
        int quantity = cursor.getInt(indexQuantity);
        byte[] imageBytes = cursor.getBlob(indexImage);
        itemViewHolder.productName.setText(productName);
        itemViewHolder.productCode.setText(productCode);
        itemViewHolder.productCategory.setText(productCategory);
        setPriceTextView(context, itemViewHolder.productPrice, productPrice);
        setQuantityTextView(context, itemViewHolder.productQuantity, quantity);
        setItemImage(itemViewHolder.productImage, imageBytes);

        itemViewHolder.itemView.setTag(index);
        itemViewHolder.Sale.setTag(index);
        itemViewHolder.setQuantity(quantity);
    }

    public void swapCursor(Cursor cursor) {
        this.cursorProducts = cursor;
        notifyDataSetChanged();
    }

    public interface ProductItemOnClickListener {
        void onClick(int id);

        void add_sale(int id, int quantity);
    }

    class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private int quantity;

        @BindView(R.id.item_name)
        TextView productName;
        @BindView(R.id.item_code)
        TextView productCode;
        @BindView(R.id.item_category)
        TextView productCategory;
        @BindView(R.id.item_price)
        TextView productPrice;
        @BindView(R.id.item_quantity)
        TextView productQuantity;
        @BindView(R.id.item_image)
        ImageView productImage;
        @BindView(R.id.add_sale)
        Button Sale;

        private void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        private ProductViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            Sale.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int id = (int) view.getTag();
                    productItemOnClickListener.add_sale(id, quantity);
                }
            });
        }

        @Override
        public void onClick(View v) {
            int tag = (int) v.getTag();
            productItemOnClickListener.onClick(tag);

        }
    }
}