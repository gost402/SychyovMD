package com.example.dsychyov.sychyovmd.image_loaders;

public class LoremFlickrImageLoader implements ImageLoader {
    @Override
    public String getImageUrl() {
        return "https://loremflickr.com/1280/720";
    }
}
