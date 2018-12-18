package com.navyliu.customtextclock.CustomTextClock;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.style.ImageSpan;

public class NavyBackgroundSpan extends ImageSpan {

    private Rect mTextBound;
    private int maxHeight = 0;
    private int maxWidth = 0;
    private int mPaddingLeft = 20;
    private int mPaddingRight = 20;
    private int mPaddingTop = 20;
    private int mPaddingBottom = 20;
    private int mTextColor = Color.GREEN;
    private int mTextSize = 50;

    public NavyBackgroundSpan(Drawable d, int verticalAlignment) {
        super(d, verticalAlignment);
        mTextBound = new Rect();
    }

    public NavyBackgroundSpan setTimeTextColor(int mTextColor) {
        this.mTextColor = mTextColor;
        return this;
    }

    public NavyBackgroundSpan setTimerTextSize(int textSize) {
        this.mTextSize = textSize;
        return this;
    }

    public NavyBackgroundSpan setTimerPadding(int left, int top, int right, int bottom) {
        this.mPaddingLeft = left;
        this.mPaddingRight = right;
        this.mPaddingTop = top;
        this.mPaddingBottom = bottom;
        return this;
    }

    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        // 绘制文本内容的背景
        paint.setTextSize(mTextSize);
        // 测量文本的宽度和高度， 通过mTextBound得到
        paint.getTextBounds(text.toString(), start, end, mTextBound);
        // 设置文本背景的宽度和高度，传入的是left、top、right、bottom四个参数
        maxWidth = maxWidth < mTextBound.width() ? mTextBound.width() : maxWidth;
        maxHeight = maxHeight < mTextBound.height() ? mTextBound.height() : maxHeight;
        // 设置最大宽度和最大高度是为了防止在倒计时 数字切换过程中会重绘，导致倒计时边框的宽度和高度抖动
        // 所以每次获取最大的宽度和高度， 而不是每次都去取测量的高度和宽度
        getDrawable().setBounds(0, 0, maxWidth + mPaddingLeft + mPaddingRight
                , mPaddingTop + mPaddingBottom + maxHeight);
        // 绘制文本的背景
        super.draw(canvas, text, start, end, x, top, y, bottom, paint);
        // 设置文本的颜色
        paint.setColor(mTextColor);
        // 设置文本字体的大小
        paint.setTextSize(mTextSize);
        int mGapX = (getDrawable().getBounds().width() - maxWidth) / 2;
        int mGapY = (getDrawable().getBounds().height() - maxHeight) / 2;
        // 绘制文本内容
        canvas.drawText(text.subSequence(start, end).toString(), x + mGapX, y - mGapY + maxHeight / 3, paint);
    }

}
