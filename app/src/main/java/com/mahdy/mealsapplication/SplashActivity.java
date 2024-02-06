package com.mahdy.mealsapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.airbnb.lottie.LottieAnimationView;

public class SplashActivity extends AppCompatActivity {
    LottieAnimationView lottieAnimationView;
    ConstraintLayout splashScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        splashScreen = findViewById(R.id.splash_screen);
        lottieAnimationView = findViewById(R.id.logo);

        Animation scaleUpAnimation = AnimationUtils.loadAnimation(this, R.anim.scale_up);
        scaleUpAnimation.setDuration(3000);

        lottieAnimationView.setAnimation(scaleUpAnimation);

        scaleUpAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {



            }

            @Override
            public void onAnimationEnd(Animation animation) {

                AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
                alphaAnimation.setDuration(2000);
                alphaAnimation.setFillAfter(true);
                lottieAnimationView.startAnimation(alphaAnimation);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out); // Transition animation
                        finish();

                    }

                },2000);


            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
}