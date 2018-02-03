package com.example.dsychyov.sychyovmd;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public class Holder {
    public static class GridHolder extends RecyclerView.ViewHolder {
        private final View mImageView;

        public GridHolder(final View view) {
            super(view);

            mImageView = view.findViewById(R.id.launcher_image);
        }

        public View getImageView() {
            return mImageView;
        }
    }
}

