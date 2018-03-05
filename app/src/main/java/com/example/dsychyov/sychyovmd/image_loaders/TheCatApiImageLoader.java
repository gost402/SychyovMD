package com.example.dsychyov.sychyovmd.image_loaders;

public class TheCatApiImageLoader implements ImageLoader {
    @Override
    public String getImageUrl() {
        return "http://thecatapi.com/api/images/get?format=src&type=png";
    }
}
