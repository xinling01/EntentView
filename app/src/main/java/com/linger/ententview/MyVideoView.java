package com.linger.ententview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

import static android.os.Build.VERSION_CODES.M;

/**
 * Created by linger on 2018/10/9.
 */

public class MyVideoView extends VideoView {
    public MyVideoView(Context context) {
        super(context);
    }

    public MyVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /*测量控件的大小*/

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize=MeasureSpec.getSize(widthMeasureSpec);
        int heightSize=MeasureSpec.getSize(heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(MeasureSpec.getMode(widthMeasureSpec)==MeasureSpec.EXACTLY&&
                MeasureSpec.getMode(heightMeasureSpec)==MeasureSpec.EXACTLY){
                setMeasuredDimension(widthMeasureSpec,heightMeasureSpec);//自定义设置控件的尺寸
        }
    }
    }
