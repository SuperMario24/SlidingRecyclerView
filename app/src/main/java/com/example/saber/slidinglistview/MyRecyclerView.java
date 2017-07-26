package com.example.saber.slidinglistview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by saber on 2017/7/26.
 */

public class MyRecyclerView extends RecyclerView {

    private static final String TAG = "MyRecyclerView";
    
    public MyRecyclerView(Context context) {
        super(context);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {

        switch (e.getAction()){
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "onInterceptTouchEvent: down");
                int childCount = getChildCount();
                for(int i=0;i<childCount;i++){
                    SlidingView slidingView = (SlidingView) getChildAt(i);
                    if(slidingView.isOpen){
                        slidingView.close();
                    }
                }
                break;
        }
        return super.onInterceptTouchEvent(e);
    }
}
