package com.app_republic.inventoryapp.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;


public class ImageUtils implements ConstantsUtils {

    public static Bitmap uriToBitmap(Context context, Uri selectedImageUri, int REQUIRED_SIZE) {

        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(context.getContentResolver().openInputStream(selectedImageUri),
                    null, options);

            int width = options.outWidth;
            int height = options.outHeight;
            int scale = ONE;

            while (true) {
                if (width / TWO < REQUIRED_SIZE || height / TWO < REQUIRED_SIZE) {
                    break;
                }
                width /= TWO;
                height /= TWO;
                scale *= TWO;
            }
            BitmapFactory.Options options2 = new BitmapFactory.Options();
            options2.inSampleSize = scale;
            return BitmapFactory.decodeStream(context.getContentResolver().openInputStream(selectedImageUri),
                    null, options2);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] bitmapToBytes(Bitmap bitmap) {
        if (bitmap == null) return null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, ProductUtils.ZERO, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public static Bitmap byteArrayToBitmap(byte[] imageByteArray) {
        if (imageByteArray == null) return null;
        return BitmapFactory.decodeByteArray(imageByteArray, ProductUtils.ZERO, imageByteArray.length);
    }


}
