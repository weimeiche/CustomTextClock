package com.navyliu.customtextclock.CustomTextClock;

import android.content.Context;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;

public class JDCountDownTimer extends NavyCountDownTimer {

    private SpannableString mSpan;
    private Context mContext;
    private int mDrawableId;

    public JDCountDownTimer(Context mContext, long mGapTime, String mTimePattern, int mDrawableId) {
        super(mContext, mGapTime, mTimePattern, mDrawableId);
        this.mContext = mContext;
        this.mDrawableId = mDrawableId;
    }

    /**
     * 重写父类的initSpanData方法
     * 通过number数组得到每块数值对应的自定义NavyBackgroundSpan对象
     * <p>
     * 然后通过NavyBackgroundSpan对象定义每块数值样式 包括背景、边框、边框圆角样式
     * 然后将这些对象假如到集合中去
     * <p>
     * 通过nonNumber数组得到每个间隔的ForegroundColorSpan对象
     * 然后通过这些对象就可以定义每个间隔块的样式，因为只定义了ForegroundColorSpan 所以只能定义
     * 每个间隔块的字体颜色，setGapSpanColor方式也是供外部自由定制每个间隔的样式
     * 实际上还可以定义其他的Span，同理实现也是很简单的     *
     */
    public void initSpanData(String timeStr) {
        super.initSpanData(timeStr);
        for (int i = 0; i < numbers.length; i++) {
            NavyBackgroundSpan mBackSpan = new NavyBackgroundSpan(mContext.getDrawable(mDrawableId), ImageSpan.ALIGN_BOTTOM);
            initBackSpanStyle(mBackSpan);
            mBackSpanList.add(mBackSpan);
        }
        for (int i = 0; i < nonNumbers.length; i++) {
            ForegroundColorSpan mGapSpan = new ForegroundColorSpan(mGapSpanColor);
            mTextColorSpanList.add(mGapSpan);
        }
    }


    /**
     * 重写父类的setBackgroundSpan方法
     * 我们知道设置span的样式主要是控制两个变量start，end索引
     * 以确定设置start到end的位置的字符串的样式
     * mGapLen=1，表示一个间隔块的长度
     * 例如：12时32分23秒的 时分秒的间隔长度
     * 所以通过遍历span集合，给字符串设置Span
     * 通过分析不难得出每个数字快的span的start索引；start = i*number[i].length+ i*mGapLen；
     * end = start+number[i].length();
     */
    public void setBackgroundSpan(String timeStr) {
        super.setBackgroundSpan(timeStr);
        int mGapLen = 1;
        mSpan = new SpannableString(timeStr);
        for (int i = 0; i < mBackSpanList.size(); i++) {
            int start = i * numbers[i].length() + i * mGapLen;
            int end = start + numbers[i].length();
            TimerUtils.setContentSpan(mSpan, mBackSpanList.get(i), start, end);
            if (i < mTextColorSpanList.size()) {
                // 这里为了就是防止12:36:27这种样式，这种样式间隔只有２个所以需要做判断，防止数组越界
                TimerUtils.setContentSpan(mSpan, mTextColorSpanList.get(i), end, end + mGapLen);
            }
        }
        mDateTv.setMovementMethod(LinkMovementMethod.getInstance());
        // 此方法很重要需要调用，否则绘制出来的倒计时就是重叠的样式
        mDateTv.setText(mSpan);
    }
}
