package com.ryan.demostore.activity;


import android.app.Activity;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.ryan.demostore.R;


/**
 * Created by hu on 16-11-17.
 */

public class AnimationActivity extends Activity {
    private TranslateAnimation mTranslateAnimation;
    private ScaleAnimation mScaleAnimation;
    private AlphaAnimation mAlphaAnimation;
    private RotateAnimation mRotateAnimation;


    @InjectView(R.id.scale_btn)
    Button scale;

    @OnClick(R.id.scale_btn)
    public void onScaleBtnClicked(){
        Animation scaleAnimation = AnimationUtils.loadAnimation(AnimationActivity.this, R.anim.scale_animation);
        textView.startAnimation(scaleAnimation);
    }

    @InjectView(R.id.tv)
    TextView textView;

    @InjectView(R.id.translate_btn)
    Button translate;

    @OnClick(R.id.translate_btn)
    public void onTranslateBtnClicked(){
        Animation translateAnimation = AnimationUtils.loadAnimation(AnimationActivity.this, R.anim.translate_animation);
        textView.startAnimation(translateAnimation);
    }

    @InjectView(R.id.alpha_btn)
    Button alpha;

    @OnClick(R.id.alpha_btn)
    public void onAlphaBtnClicked(){
        Animation alphaAnimation = AnimationUtils.loadAnimation(AnimationActivity.this, R.anim.alpha_animation);
        textView.startAnimation(alphaAnimation);
    }

    @InjectView(R.id.rotate_btn)
    Button rotate;

    @OnClick(R.id.rotate_btn)
    public void onRotateBtnClicked(){
        Animation rotateAnimation = AnimationUtils.loadAnimation(AnimationActivity.this, R.anim.rotate_animation);
        textView.startAnimation(rotateAnimation);
    }

    @InjectView(R.id.set_btn)
    Button set;

    @OnClick(R.id.set_btn)
    public void onSetClicked(){
        Animation setAnimation = AnimationUtils.loadAnimation(AnimationActivity.this, R.anim.set_animatrion);
        textView.startAnimation(setAnimation);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);
        ButterKnife.inject(this);
    }


}
