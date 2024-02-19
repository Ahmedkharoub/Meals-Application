package com.mahdy.mealsapplication.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;
import com.mahdy.mealsapplication.R;

public class MainActivity extends AppCompatActivity {
    public static final int LOGGED_OUT = -1;
    public static final int BASIC_AUTH = 1;
    public static final int GOOGLE_AUTH = 2;
    LottieAnimationView lottieAnimationView;
    FragmentContainerView fragmentContainer;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    AppBarLayout appBarLayout;
    Toolbar toolbar;
    private static final String PREF_KEY_GMAIL_LOGIN = "gmail_login_status";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lottieAnimationView = findViewById(R.id.logo);
        fragmentContainer = findViewById(R.id.fragment_container);

        drawerLayout = findViewById(R.id.main_screen);
        navigationView = findViewById(R.id.navigation_view);
        appBarLayout = findViewById(R.id.main_app_bar);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                boolean guest = sharedPreferences.getBoolean("guest", false);
                if (item.isChecked()) return false;
                int id = item.getItemId();
                if (id == R.id.action_home) {
                    navigationView.setCheckedItem(R.id.action_home);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new MainFragment())
                            .addToBackStack(null)
                            .commitAllowingStateLoss();
                } else if (id == R.id.action_plan) {
                    if (guest) {
                        Toast.makeText(MainActivity.this, "You are guest", Toast.LENGTH_LONG).show();
                        return false;
                    }
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new PlanFragment())
                            .addToBackStack(null)
                            .commitAllowingStateLoss();
                } else if (id == R.id.action_favorites) {
                    if (guest) {
                        Toast.makeText(MainActivity.this, "You are guest", Toast.LENGTH_LONG).show();
                        return false;
                    }
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new FavoriteFragment())
                            .addToBackStack(null)
                            .commitAllowingStateLoss();
                } else if (id == R.id.action_logout) {
                    if (guest) {
                        Toast.makeText(MainActivity.this, "You are guest", Toast.LENGTH_LONG).show();
                        return false;
                    }
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.apply();
                    getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new LoginFragment())
                            .addToBackStack(null)
                            .commitAllowingStateLoss();
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        Animation scaleUpAnimation = AnimationUtils.loadAnimation(this, R.anim.scale_up);
        scaleUpAnimation.setDuration(3000);

        lottieAnimationView.setAnimation(scaleUpAnimation);

        scaleUpAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                int login = determineLoginMethod();
                lottieAnimationView.animate()
                        .alpha(0)
                        .setDuration(1000)
                        .withStartAction(() -> {
                            fragmentContainer.setVisibility(View.VISIBLE);
                            fragmentContainer.animate()
                                    .alpha(1)
                                    .withStartAction(() -> {
                                        appBarLayout.setVisibility(View.VISIBLE);
                                        appBarLayout.animate()
                                                .alpha(1)
                                                .setDuration(1000)
                                                .start();
                                    })
                                    .setDuration(1000)
                                    .start();
                        }).withEndAction(() -> {
                            Fragment fragment;
                            switch (login) {
                                case BASIC_AUTH:
                                    checkLoginStatus();
                                    Toast.makeText(MainActivity.this, "username&&password Login", Toast.LENGTH_SHORT).show();
                                    break;
                                case GOOGLE_AUTH:
                                    navigationView.setCheckedItem(R.id.action_home);
                                    fragment = new MainFragment();
                                    getSupportFragmentManager().beginTransaction()
                                            .replace(R.id.fragment_container, fragment)
                                            .commitAllowingStateLoss();
                                    Toast.makeText(MainActivity.this, "Gmail login", Toast.LENGTH_SHORT).show();
                                    break;
                                case LOGGED_OUT:
                                    getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.fragment_container, new LoginFragment())
                                        .commit();
                                    break;
                                default:
                                    Toast.makeText(MainActivity.this, "An error occurred. Please try again later.", Toast.LENGTH_LONG)
                                            .show();
                                    break;
                            }
                        }).start();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

    }
    private int determineLoginMethod() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isGmailLoggedIn = sharedPreferences.getBoolean(PREF_KEY_GMAIL_LOGIN, false);
        String username = sharedPreferences.getString("username", null);
        String password = sharedPreferences.getString("password", null);
        boolean basicAuthLoggedIn = username != null && password != null;

        if (isGmailLoggedIn) {
            return GOOGLE_AUTH;
        } else if (basicAuthLoggedIn) {
            return BASIC_AUTH;
        } else {
            return LOGGED_OUT;
        }
    }

    private void checkLoginStatus() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String username = sharedPreferences.getString("username", null);
        String password = sharedPreferences.getString("password", null);

        if (username != null && password != null) {
            navigationView.setCheckedItem(R.id.action_home);
            MainFragment fragment = new MainFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commitAllowingStateLoss();
        } else {
            LoginFragment fragment = new LoginFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
        }
    }

    private boolean isGmailLoggedIn(MainActivity context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean(PREF_KEY_GMAIL_LOGIN, false);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            getSupportFragmentManager().popBackStack();
        }

        drawerLayout.closeDrawer(GravityCompat.START);

        return super.onOptionsItemSelected(item);
    }
}
