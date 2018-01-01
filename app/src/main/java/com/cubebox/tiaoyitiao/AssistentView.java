package com.cubebox.tiaoyitiao;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by luozi on 2017/12/30.
 */

public class AssistentView extends LinearLayout {
    private RectF rootRect = new RectF();

    public AssistentView(Context context) {
        this(context, null);
    }

    public AssistentView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AssistentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        rootRect.left = 0;
        rootRect.top = 0;
        rootRect.right = getMeasuredWidth();
        rootRect.bottom = getMeasuredHeight();

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }
}
