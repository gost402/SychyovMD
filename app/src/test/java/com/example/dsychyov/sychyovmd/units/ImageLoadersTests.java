package com.example.dsychyov.sychyovmd.units;

import com.example.dsychyov.sychyovmd.image_loaders.ImageLoader;
import com.example.dsychyov.sychyovmd.image_loaders.LoremFlickrImageLoader;
import com.example.dsychyov.sychyovmd.image_loaders.LoremPicsumImageLoader;
import com.example.dsychyov.sychyovmd.image_loaders.TheCatApiImageLoader;
import com.example.dsychyov.sychyovmd.image_loaders.UnsplashImageLoader;
import com.example.dsychyov.sychyovmd.utils.Converters;
import com.example.dsychyov.sychyovmd.viewmodel.DesktopApp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(Parameterized.class)
public class ImageLoadersTests {

    @Parameterized.Parameters
    public static List<Object[]> data() {
        return Arrays.asList(
                new Object[] { new LoremPicsumImageLoader(),  "https://picsum.photos/1280/720/?random" },
                new Object[] { new LoremFlickrImageLoader(), "https://loremflickr.com/1280/720" },
                new Object[] { new TheCatApiImageLoader(), "http://thecatapi.com/api/images/get?format=src&type=png" },
                new Object[] { new UnsplashImageLoader(), "https://source.unsplash.com/random/1280x720" }
        );
    }

    private final ImageLoader mInput;
    private final String mOutput;

    public ImageLoadersTests(ImageLoader input, String output) {
        mInput = input;
        mOutput = output;
    }

    @Test
    public void convertToType() throws Exception {
        assertThat(mOutput, is(equalTo(mInput.getImageUrl())));
    }
}