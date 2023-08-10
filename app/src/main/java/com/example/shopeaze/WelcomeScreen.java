package com.example.shopeaze;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.shopeaze.databinding.FragmentWelcomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WelcomeScreen extends Fragment {

    private FragmentWelcomeBinding binding;
    DatabaseReference ref;
    String user;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = FragmentWelcomeBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button shopperButton = view.findViewById(R.id.button_shopper);
        shopperButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseDatabase db = FirebaseDatabase.getInstance("https://grocery-d4fbb-default-rtdb.firebaseio.com//");
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if(currentUser != null){
                    user = currentUser.getUid();
                    ref = FirebaseDatabase.getInstance().getReference().child("Users").child("StoreOwner").child(user);
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                Toast.makeText(getActivity(), "You are a store owner", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                NavHostFragment.findNavController(WelcomeScreen.this)
                                        .navigate(R.id.action_WelcomeScreen_to_storelist);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                else{
                    NavHostFragment.findNavController(WelcomeScreen.this)
                            .navigate(R.id.action_WelcomeScreen_to_Login);
                }
            }
        });

        Button ownerButton = view.findViewById(R.id.button_owner);
        ownerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase db = FirebaseDatabase.getInstance("https://grocery-d4fbb-default-rtdb.firebaseio.com//");
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if(currentUser != null){
                    user = currentUser.getUid();
                    ref = FirebaseDatabase.getInstance().getReference().child("Users").child("StoreOwner").child(user);

                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                NavHostFragment.findNavController(WelcomeScreen.this)
                                        .navigate(R.id.action_WelcomeScreen_to_productlist);
                            }
                            else{
                                Toast.makeText(getActivity(), "You are a shopper", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }
                else{
                    NavHostFragment.findNavController(WelcomeScreen.this)
                            .navigate(R.id.action_WelcomeScreen_to_ownerLogin);
                }
            }
        });
    }

    public void isShopper(){
        String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child("StoreOwner").child(user);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Toast.makeText(getActivity(), "You are a store owner", Toast.LENGTH_SHORT).show();
                }
                else{
                    NavHostFragment.findNavController(WelcomeScreen.this)
                            .navigate(R.id.action_WelcomeScreen_to_storelist);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void isOwner(){
        String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child("StoreOwner").child(user);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    NavHostFragment.findNavController(WelcomeScreen.this)
                            .navigate(R.id.action_WelcomeScreen_to_productlist);
                }
                else{
                    Toast.makeText(getActivity(), "You are a shopper", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}