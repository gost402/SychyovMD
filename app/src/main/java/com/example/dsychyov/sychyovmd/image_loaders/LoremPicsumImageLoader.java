package com.example.dsychyov.sychyovmd.image_loaders;

public class LoremPicsumImageLoader implements ImageLoader {
    @Override
    public String getImageUrl() {
        return "https://picsum.photos/1280/720/?random";
    }
}
