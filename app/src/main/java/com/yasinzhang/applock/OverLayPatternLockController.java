package com.yasinzhang.applock;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.itsxtt.patternlock.PatternLockView;
import com.yasinzhang.applock.utils.MD5Util;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class OverLayPatternLockController implements PatternLockView.OnPatternListener{

    private static final int connect_least_dots = 4;

    private TextView mTips = null;
    private OverLayLayout mOverLayLayout = null;
    private String mPass = null;
    private boolean mIsShowing = false;

    public void showAndCheckPattern(String patternPassword, Drawable icon){
        assert(patternPassword != null && !patternPassword.isEmpty());

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                Build.VERSION.SDK_INT < Build.VERSION_CODES.O?WindowManager.LayoutParams.TYPE_PHONE:WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL|
                        WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.OPAQUE
                );
        Context context = AppLockApplication.getInstance();
        View v = LayoutInflater.from(context).inflate(R.layout.activity_pattern_lock, null);

        mOverLayLayout = new OverLayLayout(context);
        mOverLayLayout.addView(v, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.addView(mOverLayLayout, params);
        mIsShowing = true;
        PatternLockView patternLockView = v.findViewById(R.id.pattern_lock_view);
        patternLockView.setOnPatternListener(this);
        mTips = v.findViewById(R.id.tips);
        mTips.setTag(mTips.getCurrentTextColor());
        ImageView iconView = v.findViewById(R.id.app_icon);
        iconView.setImageDrawable(icon);
        mPass = patternPassword;
    }

    public boolean isShowing(){
        return mIsShowing;
    }

    public void close(){
        if(mIsShowing){
            Context context = AppLockApplication.getInstance();
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            wm.removeView(mOverLayLayout);
            mIsShowing = false;
            clearDate();
        }
    }

    @Override
    public boolean onComplete(@NotNull ArrayList<Integer> arrayList) {
        if (arrayList.size() < connect_least_dots) {
            mTips.setText(String.format(AppLockApplication.getInstance().getResources().getString(R.string.pattern_lock_dots_not_enough), connect_least_dots));
            setTipsColorByRrsourceId(android.R.color.holo_red_dark);
        } else {

            if (!verifyPatternLock(arrayList)) {
                setTipsColorByRrsourceId(android.R.color.holo_red_dark);
                mTips.setText(R.string.pattern_lock_incorrect);
            }else{
                //closeWithResult(true);
                return true;
            }
        }

        return false;
    }

    protected void setTipsColorByRrsourceId(int resourceColorId){
        Context context = AppLockApplication.getInstance();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            mTips.setTextColor(context.getResources().getColor(resourceColorId, context.getTheme()));
        }else{
            mTips.setTextColor(context.getResources().getColor(resourceColorId));
        }
    }

    protected void restoreTipsColor(){
        mTips.setTextColor((Integer)mTips.getTag());
    }

    protected boolean verifyPatternLock(ArrayList<Integer> arrayList){

        StringBuilder sb = new StringBuilder();
        for(int i = 0;i<arrayList.size();i++){
            sb = sb.append(arrayList.get(i));
        }

        return MD5Util.crypt(sb.toString()).equals(mPass);
    }

    @Override
    public void onStarted() {
        restoreTipsColor();
        mTips.setText(R.string.pattern_lock_drawing);
    }

    @Override
    public void onProgress(@NotNull ArrayList<Integer> arrayList) {

    }

    protected void clearDate(){
        mTips = null;
        mOverLayLayout = null;
        mPass = null;
    }

    class OverLayLayout extends LinearLayout
    {
        public OverLayLayout(Context context)
        {
            super(context);
        }


        @Override
        public boolean dispatchKeyEvent(KeyEvent event)
        {
            int keyCode = event.getKeyCode();
            if (keyCode == KeyEvent.KEYCODE_BACK)
            {
                if (event.getAction() == KeyEvent.ACTION_UP)
                {
                    Intent startMain = new Intent(Intent.ACTION_MAIN);
                    startMain.addCategory(Intent.CATEGORY_HOME);
                    startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    AppLockApplication.getInstance().startActivity(startMain);

                    return true;
                }
            }
            return super.dispatchKeyEvent(event);
        }
    }
}
