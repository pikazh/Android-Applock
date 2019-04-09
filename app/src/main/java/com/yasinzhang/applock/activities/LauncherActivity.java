package com.yasinzhang.applock.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.itsxtt.patternlock.PatternLockView;
import com.yasinzhang.applock.R;
import com.yasinzhang.applock.asynctasks.AsyncTaskWrapper;
import com.yasinzhang.applock.commons.Preference;
import com.yasinzhang.applock.db.AppDatabase;
import com.yasinzhang.applock.utils.MD5Util;

import org.jetbrains.annotations.*;

import java.util.ArrayList;

public class LauncherActivity extends AppCompatActivity implements PatternLockView.OnPatternListener{

    private static final int connect_least_dots = 4;

    private PatternLockView mPatternLockView = null;
    private TextView mTips = null;

    enum State{
        PATTERN_LOCK_UNKNOWN,
        PATTERN_LOCK_NOT_SET,
        PATTERN_LOCK_CONFIRM,
        PATTERN_LOCK_VERIFY,
    };

    private State mState = State.PATTERN_LOCK_UNKNOWN;
    private boolean mPassed = false;
    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);

        setContentView(R.layout.activity_pattern_lock);
        mPatternLockView = findViewById(R.id.pattern_lock_view);
        mPatternLockView.setOnPatternListener(this);
        mTips = findViewById(R.id.tips);
        mTips.setTag(mTips.getCurrentTextColor());

        ImageView ico = findViewById(R.id.app_icon);
        ico.setImageResource(R.mipmap.ic_launcher);

    }

    @Override
    protected void onStart(){
        super.onStart();

        mState = State.PATTERN_LOCK_UNKNOWN;
        restoreTipsColor();

        Preference preference = Preference.getInstance();
        if(!preference.isInitialized.getValue()){
            preference.isInitialized.observe(this, v->initView());
        }else{
            initView();
        }
    }

    @Override
    public void finish(){
        super.finish();
        if(mPassed){
            startActivity(new Intent(this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }

    void initView(){
        Preference preference = Preference.getInstance();
        if(preference.encryptionPatternLock == null ||preference.encryptionPatternLock.isEmpty()){
            mState = State.PATTERN_LOCK_NOT_SET;
            mTips.setText(R.string.set_pattern_lock);
        }else{
            mState = State.PATTERN_LOCK_VERIFY;
            mTips.setText(R.string.verify_pattern_lock);
        }
    }

    @Override
    public boolean onComplete(@NotNull ArrayList<Integer> arrayList) {
        if(arrayList.size() < connect_least_dots){
            mTips.setText(String.format(getResources().getString(R.string.pattern_lock_dots_not_enough),connect_least_dots));
            setTipsColorByRrsourceId(android.R.color.holo_red_dark);

        }
        else{
            switch (mState){
                case PATTERN_LOCK_CONFIRM:
                    ArrayList<Integer> origArrayList = (ArrayList<Integer>) mPatternLockView.getTag();
                    if(origArrayList.equals(arrayList)){
                        savePatternLock(arrayList);
                        mPassed = true;
                        finish();
                        return true;
                    }else{
                        mState = State.PATTERN_LOCK_NOT_SET;
                        mTips.setText(R.string.pattern_lock_inconsistent);
                        setTipsColorByRrsourceId(android.R.color.holo_red_dark);
                    }

                    break;
                case PATTERN_LOCK_NOT_SET:
                    mPatternLockView.setTag(arrayList);
                    mState = State.PATTERN_LOCK_CONFIRM;
                    mTips.setText(R.string.confirm_pattern_lock);
                    return true;
                    //break;
                case PATTERN_LOCK_VERIFY:
                    if(!verifyPatternLock(arrayList)){
                        setTipsColorByRrsourceId(android.R.color.holo_red_dark);
                        mTips.setText(R.string.pattern_lock_incorrect);
                    }
                    else{
                        mPassed = true;
                        finish();
                        return true;
                    }
                    //break;
                default:
                    //no operation with state=PATTERN_LOCK_UNKNOWN

            }
        }

        return false;
    }

    protected void setTipsColorByRrsourceId(int resourceColorId){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            mTips.setTextColor(getResources().getColor(resourceColorId, getTheme()));
        }else{
            mTips.setTextColor(getResources().getColor(resourceColorId));
        }
    }

    protected void restoreTipsColor(){
        mTips.setTextColor((Integer)mTips.getTag());
    }

    protected boolean verifyPatternLock(ArrayList<Integer> arrayList){
        Preference preference = Preference.getInstance();
        assert(preference.encryptionPatternLock != null && !preference.encryptionPatternLock.isEmpty());
        StringBuilder sb = new StringBuilder();
        for(int i = 0;i<arrayList.size();i++){
            sb = sb.append(arrayList.get(i));
        }

        return MD5Util.crypt(sb.toString()).equals(preference.encryptionPatternLock);
    }

    protected void savePatternLock(ArrayList<Integer> arrayList){
        StringBuilder sb = new StringBuilder();
        for(int i = 0;i<arrayList.size();i++){
            sb = sb.append(arrayList.get(i));
        }

        Preference preference = Preference.getInstance();
        preference.encryptionPatternLock = MD5Util.crypt(sb.toString());

        new AsyncTaskWrapper<Void>() {
            @Override
            protected Void process() {
                Preference preference = Preference.getInstance();
                AppDatabase.getDatabase().configModel().updatePattern(preference.encryptionPatternLock);
                return null;
            }
        }.start();
    }

    @Override
    public void onStarted() {
        super.onStart();
        restoreTipsColor();
        mTips.setText(R.string.pattern_lock_drawing);
    }

    @Override
    public void onProgress(@NotNull ArrayList<Integer> arrayList) {

    }
}
