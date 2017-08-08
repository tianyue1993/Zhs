package com.zhs.zhs.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.zhs.zhs.MainActivity;
import com.zhs.zhs.R;
import com.zhs.zhs.utils.StringUtils;


/**
 * Created by tianyue on 2017/3/23.
 * app启动页面
 */
public class SplashActivity extends Baseactivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ImageView splash = (ImageView) findViewById(R.id.splash);
        Animation animation = new AlphaAnimation(1.0f, 1.0f);
        animation.setDuration(500);
        splash.setAnimation(animation);
        animation.startNow();
        animation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                SplashActivity.this.finish();
                if (StringUtils.isNotEmptyOrNull(prefs.getToken())) {
                    startActivity(new Intent(mContext, MainActivity.class));
                } else {
                    startActivity(new Intent(mContext, LoginActivity.class));
                }

            }

        });
    }


}
