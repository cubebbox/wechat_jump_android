package com.cubebox.tiaoyitiao;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.cubebox.tiaoyitiao.utils.DensityUtil;

/**
 * Created by luozi on 2017/12/30.
 */

public class AssistentView extends View {
    private Paint paint;
    /**
     * 实心画笔
     */
    private Paint paint2;
    private RectF rootRect = new RectF();
    private RectF rectA = new RectF();
    private RectF rectB = new RectF();
    private int DP10 = 10;

    private int BTN_SIZE = 100;

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
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.RED);

        paint2 = new Paint();
        paint2.setAntiAlias(true);
        paint2.setStyle(Paint.Style.FILL);
        paint2.setColor(Color.RED);

        DP10 = DensityUtil.dip2px(context, DP10);
        BTN_SIZE = DensityUtil.dip2px(context, BTN_SIZE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        rootRect.left = 0;
        rootRect.top = 0;
        rootRect.right = getMeasuredWidth();
        rootRect.bottom = getMeasuredHeight();

        rectA.left = DP10 * 3;
        rectA.top = rootRect.centerY();
        rectA.right = DP10 * 3 + BTN_SIZE;
        rectA.bottom = rootRect.centerY() + BTN_SIZE;
        lastAX = rectA.centerX();
        lastAY = rectA.centerY();

        rectB.left = rootRect.right - DP10 * 3 - BTN_SIZE;
        rectB.top = rootRect.centerY();
        rectB.right = rootRect.right - DP10 * 3;
        rectB.bottom = rootRect.centerY() + BTN_SIZE;
        lastBX = rectB.centerX();
        lastBY = rectB.centerY();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //点A
        paint.setColor(Color.RED);
        paint2.setColor(Color.RED);
        paint.setStrokeWidth(DP10 / 6);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(rootRect, paint);

        if (moveBtnType == 1) {
            rectA.left = lastAX + disX - (BTN_SIZE / 2);
            rectA.top = lastAY + disY - (BTN_SIZE / 2);
            rectA.right = lastAX + disX + (BTN_SIZE / 2);
            rectA.bottom = lastAY + disY + (BTN_SIZE / 2);
        }
        canvas.drawCircle(rectA.centerX(), rectA.centerY(), rectA.width() / 2, paint);
        canvas.drawCircle(rectA.centerX(), rectA.centerY(), rectA.width() / 40, paint2);
        canvas.drawLine(rectA.centerX(), rectA.top, rectA.centerX(), rectA.bottom, paint);
        canvas.drawLine(rectA.left, rectA.centerY(), rectA.right, rectA.centerY(), paint);
        //点B
        paint.setColor(Color.BLUE);
        paint2.setColor(Color.BLUE);

        if (moveBtnType == 2) {
            rectB.left = lastBX + disX - (BTN_SIZE / 2);
            rectB.top = lastBY + disY - (BTN_SIZE / 2);
            rectB.right = lastBX + disX + (BTN_SIZE / 2);
            rectB.bottom = lastBY + disY + (BTN_SIZE / 2);
        }
        canvas.drawCircle(rectB.centerX(), rectB.centerY(), rectB.width() / 2, paint);
        canvas.drawCircle(rectB.centerX(), rectB.centerY(), rectB.width() / 40, paint2);
        canvas.drawLine(rectB.centerX(), rectB.top, rectB.centerX(), rectB.bottom, paint);
        canvas.drawLine(rectB.left, rectB.centerY(), rectB.right, rectB.centerY(), paint);

        paint.setStrokeWidth(DP10 / 4);
        canvas.drawLine(rectA.centerX(), rectA.centerY(), rectB.centerX(), rectB.centerY(), paint);

        paint.setStrokeWidth(DP10 / 20);
        paint.setTextSize(DP10);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawText("距离：" + Math.sqrt(((rectA.centerX() - rectB.centerX()) * (rectA.centerX() - rectB.centerX())) + ((rectA.centerY() - rectB.centerY()) * (rectA.centerY() - rectB.centerY())))
                , 0, DP10 * 2, paint);

    }

    float downX = 0, downY = 0;
    float disX = 0, disY = 0;
    /**
     * 上一次移动到的中心点位置
     */
    float lastAX = 0, lastAY = 0;
    float lastBX = 0, lastBY = 0;
    /**
     * 移动的按钮  1 点A  2点B
     */
    int moveBtnType = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX(), y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN://0
                downX = x;
                downY = y;
                if (rectA.contains(x, y)) {
                    moveBtnType = 1;
                } else if (rectB.contains(x, y)) {
                    moveBtnType = 2;
                } else {
                    moveBtnType = 1;
                }
                break;
            case MotionEvent.ACTION_MOVE://2
                disX = x - downX;
                disY = y - downY;
                if (moveBtnType == 1 || moveBtnType == 2)//只有出发了这两个状态绘制
                    invalidate();
                break;
            case MotionEvent.ACTION_UP://1
                lastAX = rectA.centerX();
                lastAY = rectA.centerY();
                lastBX = rectB.centerX();
                lastBY = rectB.centerY();

                disX = 0;
                disY = 0;
                break;

        }
        return true;
    }

    public double getDistance() {
        double dis = Math.sqrt(((rectA.centerX() - rectB.centerX()) * (rectA.centerX() - rectB.centerX())) + ((rectA.centerY() - rectB.centerY()) * (rectA.centerY() - rectB.centerY())));
        double cen = 730f;
        if (dis < cen) {//小于
            double tmp = cen - dis;
            int tmp2 = (int) (tmp / 25);
//            dis += (tmp * (tmp2 * 0.01 + 0.06) * (((1 - tmp) / cen)));
            if (tmp >= 0 && tmp < 25) {
                dis += (tmp * 0.06f);
            } else if (tmp >= 25 && tmp < 50) {
                dis += (tmp * 0.07f);
            } else if (tmp >= 50 && tmp < 75) {
                dis += (tmp * 0.08f);
            } else if (tmp >= 75 && tmp < 100) {
                dis += (tmp * 0.09f);
            } else if (tmp >= 100 && tmp < 125) {
                dis += (tmp * 0.10f);
            } else if (tmp >= 125 && tmp < 150) {
                dis += (tmp * 0.11f);
            } else if (tmp >= 150 && tmp < 175) {
                dis += (tmp * 0.12f);
            } else if (tmp >= 175 && tmp < 200) {
                dis += (tmp * 0.13f);
            } else if (tmp >= 200 && tmp < 225) {
                dis += (tmp * 0.14f);
            } else if (tmp >= 225 && tmp < 250) {
                dis += (tmp * 0.15f);
            } else if (tmp >= 250 && tmp < 275) {
                dis += (tmp * 0.155f);
            } else if (tmp >= 275 && tmp < 300) {
                dis += (tmp * 0.17);
            } else if (tmp >= 300 && tmp < 325) {
                dis += (tmp * 0.18f);
            } else if (tmp >= 325 && tmp < 350) {
                dis += (tmp * 0.185f);
            } else if (tmp >= 350 && tmp < 375) {
                dis += (tmp * 0.18f);
            } else if (tmp >= 375 && tmp < 400) {
                dis += (tmp * 0.175f);
            } else if (tmp >= 400 && tmp < 425) {
                dis += (tmp * 0.17f);
            } else if (tmp >= 425 && tmp < 450) {
                dis += (tmp * 0.15f);
            } else if (tmp >= 450 && tmp < 475) {
                dis += (tmp * 0.14f);
            } else if (tmp >= 475 && tmp < 500) {
                dis += (tmp * 0.13f);
            }  else {
                dis += (tmp * 0.12f);
            }
        } else {//大于
            double tmp = dis - cen;
            int tmp2 = (int) (tmp / 25);
//            dis -= (tmp * (tmp2 * 0.025 + 0.05));
            if (tmp >= 0 && tmp < 25) {
                dis -= (tmp * 0.055f);
            } else if (tmp >= 25 && tmp < 50) {
                dis -= (tmp * 0.06f);
            } else if (tmp >= 50 && tmp < 75) {
                dis -= (tmp * 0.07f);
            } else if (tmp >= 75 && tmp < 100) {
                dis -= (tmp * 0.08f);
            } else if (tmp >= 100 && tmp < 125) {
                dis -= (tmp * 0.09f);
            } else if (tmp >= 125 && tmp < 150) {
                dis -= (tmp * 0.10f);
            } else if (tmp >= 150 && tmp < 175) {
                dis -= (tmp * 0.11f);
            } else if (tmp >= 175 && tmp < 200) {
                dis -= (tmp * 0.12f);
            } else if (tmp >= 200 && tmp < 225) {
                dis -= (tmp * 0.13f);
            } else if (tmp >= 225 && tmp < 250) {
                dis -= (tmp * 0.14f);
            } else if (tmp >= 250 && tmp < 275) {
                dis -= (tmp * 0.15f);
            } else if (tmp >= 275 && tmp < 300) {
                dis -= (tmp * 0.16f);
            } else if (tmp >= 300 && tmp < 325) {
                dis -= (tmp * 0.17f);
            }else {
                dis -= (tmp * 0.19f);
            }
        }
        return dis;
    }

    /**
     * 微调距离
     *
     * @param type     1.红色 2.蓝色
     * @param moveType 1.左右 2.上下
     */
    public void tinyMove(int type, int moveType, float value) {
        moveBtnType = type;
        switch (moveType) {
            case 1:
                disX += value;
                break;
            case 2:
                disY += value;
                break;
        }
        invalidate();
    }
}
