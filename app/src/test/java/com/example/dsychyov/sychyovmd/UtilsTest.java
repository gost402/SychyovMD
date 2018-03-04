package com.example.dsychyov.sychyovmd;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.Locale;

import static junit.framework.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
@PowerMockIgnore({ "org.mockito.*", "org.robolectric.*", "android.*" })
@PrepareForTest({ MediaStore.Images.Media.class, Bitmap.class })
public class UtilsTest {
    private Context context;

    @Rule
    public PowerMockRule rule = new PowerMockRule();

    @Before
    public void setUp() {
        context = RuntimeEnvironment.application.getApplicationContext();
    }

    @Test
    public void formatDate() throws Exception {
        String formattedDate = Utils.formatDate(context, 0);
        assertEquals("Jan-01-1970", formattedDate);
    }

    @Test
    public void getCurrentLocale() throws Exception {
        Locale locale = Utils.getCurrentLocale(context);
        assertEquals("English (United States)", locale.getDisplayName());
    }

    @Test
    public void getByteArrayFromImage() throws Exception {
//        Bitmap imageBitmap = Mockito.mock(Bitmap.class);
//        Bitmap resizedBitmap = Mockito.mock(Bitmap.class);
//
//        PowerMockito.mockStatic(MediaStore.Images.Media.class);
//        PowerMockito.mockStatic(Bitmap.class);
//
//        BDDMockito.given(
//                MediaStore.Images.Media.getBitmap(
//                        Mockito.any(ContentResolver.class),
//                        Mockito.any(Uri.class))
//        ).willReturn(imageBitmap);
//
//        BDDMockito.given(
//                Bitmap.createScaledBitmap(
//                        Mockito.any(Bitmap.class),
//                        Mockito.any(Integer.class),
//                        Mockito.any(Integer.class),
//                        Mockito.any(Boolean.class))
//        ).willReturn(resizedBitmap);
//
//        Mockito.verify(resizedBitmap).compress(
//                Mockito.any(Bitmap.CompressFormat.class),
//                100,
//                Mockito.any(OutputStream.class)
//        );

//        ByteArrayOutputStream bos = Mockito.mock(ByteArrayOutputStream.class);
//        PowerMockito.whenNew(ByteArrayOutputStream.class).withNoArguments().thenReturn(bos);
//
//        byte[] expectedByteArray = new byte[] { 1, 2, 4, 1 };
//        Mockito.when(bos.toByteArray()).thenReturn(expectedByteArray);

        Uri selectedImage = Uri.parse("example://fake_image");
        byte[] byteArray = Utils.getByteArrayFromImage(context, selectedImage);
        assertEquals(null , byteArray);
    }

    @Test
    public void loadBitmap() throws Exception {
        assertEquals(null, Utils.loadBitmap("wrong_url"));
    }

    @Test
    public void loadBitmapError() throws Exception {
        assertEquals(null, Utils.loadBitmap("wrong_url"));
    }
}
