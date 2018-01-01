package com.cubebox.tiaoyitiao;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Created by luozi on 2017/12/30.
 */

public class TouchrReleative extends LinearLayout {
    public TouchrReleative(Context context) {
        this(context, null);
    }

    public TouchrReleative(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TouchrReleative(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: // touch down so check if the
                Log.e("test", "down");
                break;
            case MotionEvent.ACTION_MOVE: // touch drag with the ball
                Log.e("test", "x - " + event.getX() + "  y - " + event.getY());
                break;
            case MotionEvent.ACTION_UP:
                Log.e("test", "up");
                break;

        }
        return true;
    }

}
