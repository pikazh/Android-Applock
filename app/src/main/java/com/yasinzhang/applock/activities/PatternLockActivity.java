package com.yasinzhang.applock.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.itsxtt.patternlock.PatternLockView;
import com.yasinzhang.applock.R;

import org.jetbrains.annotations.*;

import java.util.ArrayList;

public class PatternLockActivity extends AppCompatActivity implements PatternLockView.OnPatternListener{

    private PatternLockView patternLockView = null;
    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);

        setContentView(R.layout.activity_pattern_lock);
        patternLockView = findViewById(R.id.pattern_lock_view);
        patternLockView.setOnPatternListener(this);
    }

    @Override
    public boolean onComplete(@NotNull ArrayList<Integer> arrayList) {
        return false;
    }

    @Override
    public void onProgress(@NotNull ArrayList<Integer> arrayList) {
        //super.onProgress(arrayList);
    }

    @Override
    public void onStarted() {
        super.onStart();
    }
}
