package com.example.shopeaze;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.shopeaze.databinding.FragmentWelcomeBinding;

public class WelcomeScreen extends Fragment {

    private FragmentWelcomeBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentWelcomeBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button shopperButton = view.findViewById(R.id.button_shopper);
        shopperButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(WelcomeScreen.this)
                        .navigate(R.id.action_WelcomeScreen_to_Login);
            }
        });

        Button ownerButton = view.findViewById(R.id.button_owner);
        ownerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //    NavHostFragment.findNavController(WelcomeScreen.this)
                //            .navigate(R.id.action_FirstFragment_to_OwnerFragment);
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}