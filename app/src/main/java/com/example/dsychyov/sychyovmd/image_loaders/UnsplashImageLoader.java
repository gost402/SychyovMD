package com.example.dsychyov.sychyovmd.image_loaders;

public class UnsplashImageLoader implements ImageLoader {
    @Override
    public String getImageUrl() {
        return "https://source.unsplash.com/random/1280x720";
    }
}
