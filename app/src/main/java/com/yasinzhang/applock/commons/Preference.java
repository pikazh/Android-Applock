package com.yasinzhang.applock.commons;

import androidx.lifecycle.MutableLiveData;

public class Preference {

    private static Preference Instance = new Preference();

    public static Preference getInstance(){
        return Instance;
    }

    public MutableLiveData<Boolean> isInitialized = new MutableLiveData<>(false);
    public String encryptionPatternLock;
}
