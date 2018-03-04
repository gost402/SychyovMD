package com.example.dsychyov.sychyovmd;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;

import com.yandex.metrica.YandexMetrica;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.util.Locale;

public class Utils
{
    public static void setActivityTheme(Activity activity)
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        boolean isDark = preferences.getBoolean(activity.getResources().getString(R.string.launcher_theme_dark_key), false);

        activity.setTheme(isDark ? R.style.AppTheme_Dark : R.style.AppTheme);
    }

    public static String formatDate(Context context, long lDate) {
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(context);
        return dateFormat.format(lDate);
    }

    public static Locale getCurrentLocale(Context context){
        Locale locale;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            locale = context.getResources().getConfiguration().getLocales().get(0);
        } else{
            locale = context.getResources().getConfiguration().locale;
        }

        return locale;
    }

    public static byte[] getByteArrayFromImage(Context context, Uri selectedImage) throws IOException {
        final int maxIconSize = 256;
        final int iconQuality = 100;

        Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), selectedImage);
        Bitmap resizedBitmap = Utils.resizeBitmap(bitmap, maxIconSize);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        resizedBitmap.compress(Bitmap.CompressFormat.PNG, iconQuality, bos);
        return bos.toByteArray();
    }

    @Nullable
    public static Bitmap loadBitmap(String srcUrl) {
        try {
            URL url = new URL(srcUrl);
            URLConnection urlConnection = url.openConnection();
            InputStream is = urlConnection.getInputStream();
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            int nRead;
            byte[] data = new byte[16384];

            while ((nRead = is.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            buffer.flush();
            byte [] bitmap = buffer.toByteArray();
            return BitmapFactory.decodeByteArray(bitmap, 0, bitmap.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Bitmap resizeBitmap(Bitmap bitmap, int maxSize) {
        int outWidth;
        int outHeight;
        int inWidth = bitmap.getWidth();
        int inHeight = bitmap.getHeight();

        if(inWidth > inHeight){
            outWidth = maxSize;
            outHeight = (inHeight * maxSize) / inWidth;
        } else {
            outHeight = maxSize;
            outWidth = (inWidth * maxSize) / inHeight;
        }

        return Bitmap.createScaledBitmap(bitmap, outWidth, outHeight, false);
    }
}
