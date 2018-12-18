package com.navyliu.customtextclock.CustomTextClock;

import android.content.Context;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;

import java.util.ArrayList;
import java.util.List;

public class VIPCountDownTimer extends NavyCountDownTimer {

    private SpannableString mSpan;
    private Context mContext;
    private int mDrawableId;
    private List<NavyBackgroundSpan> mSpanList;
    private String[] vipNumbers;
    private char[] vipNonNumbers;

    public VIPCountDownTimer(Context mContext, long mGapTime, String mTimePattern, int mDrawableId) {
        super(mContext, mGapTime, mTimePattern, mDrawableId);
        this.mContext = mContext;
        this.mDrawableId = mDrawableId;
        mSpanList = new ArrayList<>();
    }

    /**
     * 重写父类的setBackgroundSpan方法
     * 我们知道设置Span的样式主要是控制两个变量start,end索引
     * 以确定设置start到end位置的字符串的子串的样式,表示每个数字子串在整个字符串中的位置范围
     * mGapLen = 1,表示一个间隔块的长度,
     * 例如:12时36分27秒的"时","分","秒"的间隔长度
     * 所以通过遍历Span集合,给字符串设置Span,
     * 通过分析不难得出每个数值块的Span的start索引:start = i*numbers[i].length() + i*mGapLen;
     * end = start + numbers[i].length();
     */
    @Override
    public void setBackgroundSpan(String timeStr) {
        int mGapLen = 1;
        mSpan = new SpannableString(timeStr);
        initSpanData(timeStr);
        int start = 0;
        int count = 0;
        for (int i = 0; i < vipNumbers.length; i++) {
            for (int j = start; j < start + vipNumbers[i].toCharArray().length; j++, count++) {
                TimerUtils.setContentSpan(mSpan, mSpanList.get(count), j, j + mGapLen);
            }
            //此时表示遍历完了某一块的数值，从而需要将此时该块数值去更新start变量
            start = start + vipNumbers[i].toCharArray().length;
            if (i < nonNumbers.length) {
                TimerUtils.setContentSpan(mSpan, mTextColorSpanList.get(i), start, start + mGapLen);
                start = start + mGapLen;//如果是个间隔还得去加上每个间隔长度最后去更新start变量
            }
        }
        mDateTv.setMovementMethod(LinkMovementMethod.getInstance());
        mDateTv.setText(mSpan);
    }

    /**
     * 重写父类的initSpanData方法
     * 通过number数组得到每块数值对应的自定义MikyouBackgroundSpan对象
     * 然后通过MikyouBackgroundSpan对象定义每块数值的样式包括背景,边框,边框圆角样式,然后将这些对象加入到集合中去
     * 通过nonNumber数组得到每个间隔的ForegroundColorSpan对象
     * 然后通过这些对象就可以定义每个间隔块的样式,因为只定义了ForegroundColorSpan所以只能定义
     * 每个间隔块的字体颜色,setmGapSpanColor方式也是供外部自由定制每个间隔的样式
     * 实际上还可以定义其他的Span,同理实现也是很简单的。
     */
    @Override
    public void initSpanData(String timeStr) {
        super.initSpanData(timeStr);
        vipNumbers = TimerUtils.getNumInTimerStr(timeStr);//得到每个数字注意不是每块数值，并加入数组
        vipNonNumbers = TimerUtils.getNonNumInTimerStr(timeStr);//得到每个间隔字符，并加入到数组
        for (int i = 0; i < vipNumbers.length; i++) {
            for (int j = 0; j < vipNumbers[i].toCharArray().length; j++) {//因为需要得到每个数字所以还得遍历每块数值中的每个数字，所以需要二层循环
                NavyBackgroundSpan mSpan = new NavyBackgroundSpan(mContext.getDrawable(mDrawableId), ImageSpan.ALIGN_BOTTOM);
                initBackSpanStyle(mSpan);
                mSpanList.add(mSpan);
            }
        }
        for (int i = 0; i < vipNonNumbers.length; i++) {
            ForegroundColorSpan mGapSpan = new ForegroundColorSpan(mGapSpanColor);
            mTextColorSpanList.add(mGapSpan);
        }
    }
}
