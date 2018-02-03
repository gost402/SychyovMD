package com.example.dsychyov.sychyovmd;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class SquareView extends View implements View.OnLongClickListener {
    public SquareView(final Context context) {
        super(context);
        setOnLongClickListener(this);
    }

    public SquareView(final Context context, @Nullable final AttributeSet attrs) {
        super(context, attrs);
        setOnLongClickListener(this);
    }

    public SquareView(final Context context, @Nullable final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnLongClickListener(this);
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }
}
