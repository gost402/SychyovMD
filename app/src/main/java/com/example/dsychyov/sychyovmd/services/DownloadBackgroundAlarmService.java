package com.example.dsychyov.sychyovmd.services;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.example.dsychyov.sychyovmd.R;
import com.example.dsychyov.sychyovmd.Utils;
import com.example.dsychyov.sychyovmd.image_loaders.ImageLoader;
import com.example.dsychyov.sychyovmd.image_loaders.LoremFlickrImageLoader;
import com.example.dsychyov.sychyovmd.image_loaders.LoremPicsumImageLoader;
import com.example.dsychyov.sychyovmd.image_loaders.TheCatApiImageLoader;
import com.example.dsychyov.sychyovmd.image_loaders.UnsplashImageLoader;
import com.example.dsychyov.sychyovmd.image_loaders.YandexImageLoader;
import com.example.dsychyov.sychyovmd.image_loaders.ImageSaver;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DownloadBackgroundAlarmService extends Service {

    public static final String BROADCAST_ACTION_UPDATE_IMAGE = "com.example.shad2018_practical6.simpleexample.UPDATE_IMAGE";
    public static final String BROADCAST_PARAM_IMAGE = "com.example.shad2018_practical6.simpleexample.IMAGE";
    public static final String MAIN_BACKGROUND_FILE = "myImage.png";

    class DownloadBackgroundAlarmTask implements Runnable {
        DownloadBackgroundAlarmTask() { }

        @Override
        public void run() {
            Log.i("TAG", "***************************START DOWNLOAD***********************");

            final String imageUrl = getImageLoader().getImageUrl();
            if (!TextUtils.isEmpty(imageUrl)) {
                final Bitmap bitmap = Utils.loadBitmap(imageUrl);
                ImageSaver.getInstance().saveImage(getApplicationContext(), bitmap, MAIN_BACKGROUND_FILE);

                final Intent broadcastIntent = new Intent(BROADCAST_ACTION_UPDATE_IMAGE);
                broadcastIntent.putExtra(BROADCAST_PARAM_IMAGE, MAIN_BACKGROUND_FILE);
                sendBroadcast(broadcastIntent);
            }
        }
    }

    private ExecutorService mExecutor;

    public DownloadBackgroundAlarmService () {
        mExecutor = Executors.newSingleThreadExecutor();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mExecutor.execute(new DownloadBackgroundAlarmTask());
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mExecutor.shutdownNow();
    }

    private ImageLoader getImageLoader() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String sourceKey = preferences.getString(
                getString(R.string.preference_background_sources_key),
                getString(R.string.preference_background_source_yandex_key)
        );

        ImageLoader imageLoader = null;

        switch (sourceKey) {
            case "The Cat Api":
                imageLoader = new TheCatApiImageLoader();
                break;
            case "Yandex":
                imageLoader = new YandexImageLoader();
                break;
            case "Lorem Picsum":
                imageLoader = new LoremPicsumImageLoader();
                break;
            case "Lorem Flickr":
                imageLoader = new LoremFlickrImageLoader();
                break;
            case "Unsplash":
                imageLoader = new UnsplashImageLoader();
                break;
        }

        return imageLoader;
    }
}
