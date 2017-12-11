package com.example.suzanne.recipesapp.IdlingResource;

import android.support.annotation.Nullable;
import android.support.test.espresso.IdlingResource;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by suzanne on 11/12/2017.
 */

public class RecipeIdlingResource implements IdlingResource {

    @Nullable private volatile ResourceCallback mResourceCallback;
    private AtomicBoolean mIsIdleNow = new AtomicBoolean(true);

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    public boolean isIdleNow() {
        return mIsIdleNow.get();
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        mResourceCallback = callback;
    }

    public void setIdleState(boolean isIdleNow){
        mIsIdleNow.set(isIdleNow);
        if(isIdleNow && mResourceCallback != null){
            mResourceCallback.onTransitionToIdle();
        }

    }
}
