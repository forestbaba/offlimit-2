package com.forestsoftware.offlimit.util;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;

/**
 * Created by user on 8/5/2018.
 */

public class FullscreenHelper extends ViewGroup {
    public FullscreenHelper(Context context) {
        super(context);
    }
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.v("customViewGroup", "**********Intercepted");
        return true;
    }
}
