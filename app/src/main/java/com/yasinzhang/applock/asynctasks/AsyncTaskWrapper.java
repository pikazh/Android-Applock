package com.yasinzhang.applock.asynctasks;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

public abstract class AsyncTaskWrapper<T> extends AsyncTask<Void, Void, T> {

    private List<CallBackWrapper<T>> mCallbackWrappers = new ArrayList<>();

    protected abstract T process();

    @Override
    protected T doInBackground(Void... voids) {
        return process();
    }

    @Override
    protected void onPostExecute(T t) {
        super.onPostExecute(t);

        for (int i = 0; i < mCallbackWrappers.size(); i++) {
            mCallbackWrappers.get(i).onCallback(t);
        }
    }


    public void registerCallback(LifecycleOwner lifeCycleOwner, CallBack<T> callBack){
        if(lifeCycleOwner == null || callBack == null)
            return;

        mCallbackWrappers.add(new CallBackWrapper<>(callBack, lifeCycleOwner));
    }

    public void registerCallback(CallBack<T> callBack){
        if(callBack != null){
            mCallbackWrappers.add(new CallBackWrapper<>(callBack, null));
        }
    }

    public void unregisterCallback(CallBack<T> callBack){
        Iterator<CallBackWrapper<T>> it = mCallbackWrappers.iterator();
        while(it.hasNext()){
            CallBackWrapper<T> o = it.next();
            if(o.cb == callBack){
                it.remove();
            }
        }
    }

    public void unregisterCallbacksFor(LifecycleOwner lifeCycleOwner){
        Iterator<CallBackWrapper<T>> it = mCallbackWrappers.iterator();
        while(it.hasNext()){
            CallBackWrapper<T> o = it.next();
            if(o.owner == lifeCycleOwner){
                it.remove();
            }
        }
    }

    public void start() {
        this.execute();
    }


    public interface CallBack<U>{
        void onFinish(U t);
    }

    class CallBackWrapper<U> implements LifecycleObserver {
        private CallBack<U> cb;
        private LifecycleOwner owner;

        public CallBackWrapper(CallBack<U> cb, LifecycleOwner owner){
            this.cb = cb;
            this.owner = owner;
            if(this.owner != null)
                this.owner.getLifecycle().addObserver(this);
        }

        public void onCallback(U t){
            if(cb == null)
                return;

            cb.onFinish(t);
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        protected void onDestroy(){
            mCallbackWrappers.remove(this);
        }

    }

}
