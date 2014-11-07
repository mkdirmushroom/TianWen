package me.jeremy.ccst.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.nhaarman.listviewanimations.appearance.AnimationAdapter;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;


import me.jeremy.ccst.App;

/**
 * Powered by gonjay
 */
public class ItemAnimationAdapter extends AnimationAdapter {

    private float mTranslationY = 150;
    private float mRotationX = 8;
    private long mDuration;

    public ItemAnimationAdapter(BaseAdapter baseAdapter) {
        super(baseAdapter);
        mDuration = App.getContext().getResources().getInteger(android.R.integer.config_mediumAnimTime);
    }

    @Override
    public Animator[] getAnimators(ViewGroup parent, View view) {
        return new Animator[] {
                ObjectAnimator.ofFloat(view, "translationY", mTranslationY, 0),
                ObjectAnimator.ofFloat(view, "rotationX", mRotationX, 0)
        };
    }


}
