package com.example.shopeaze;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.navigation.fragment.NavHostFragment;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.shopeaze.databinding.ActivityLogoutBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Logout extends Fragment {

    FirebaseAuth auth;
    Button button;
    TextView textView;
    FirebaseUser user;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_logout, container, false);
        button = view.findViewById(R.id.logout);
        textView = view.findViewById(R.id.user_details);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if (user==null){
            //Intent intent = new Intent(getActivity(), Login.class);
            //startActivity(intent);
            NavHostFragment.findNavController(Logout.this)
                    .navigate(R.id.action_logout_to_WelcomeScreen);
        }
        else{
            textView.setText(user.getEmail());
        }

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                FirebaseAuth.getInstance().signOut();
                //Intent intent = new Intent(getActivity(), WelcomeScreen.class);
                //startActivity(intent);
                NavHostFragment.findNavController(Logout.this)
                        .navigate(R.id.action_logout_to_WelcomeScreen);
            }
        });

        return view;
    }
}