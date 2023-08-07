package com.example.shopeaze;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

public class SplashScreenFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_splash_screen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final ImageView logoImageView = view.findViewById(R.id.logoImageView);
        final NavController navController = NavHostFragment.findNavController(this);

        Animation fadeInAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in);
        final Animation fadeOutAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_out);

        logoImageView.setVisibility(View.VISIBLE);
        logoImageView.startAnimation(fadeInAnimation);
        fadeInAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                logoImageView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                fadeOutAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        logoImageView.setVisibility(View.INVISIBLE);
                        view.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                navigateToWelcomeScreen(navController);
                            }
                        }, 200);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });

                logoImageView.startAnimation(fadeOutAnimation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        logoImageView.startAnimation(fadeInAnimation);
    }

    private void navigateToWelcomeScreen(NavController navController) {
        navController.navigate(R.id.action_splashScreenFragment_to_welcomeScreenFragment);
    }
}

