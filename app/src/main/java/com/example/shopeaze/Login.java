package com.example.shopeaze;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends Fragment {

    TextInputEditText editTextEmail, editTextPassword;
    Button buttonLogin;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    TextView textView;

    @Override
    public void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        // Check if user is already signed in (non-null)
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            //Intent intent = new Intent(getActivity(), LogoutActivity.class);
            //startActivity(intent);
            //getActivity().finish();
            NavHostFragment.findNavController(Login.this)
                    .navigate(R.id.action_Login_to_logout);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTextEmail = view.findViewById(R.id.email);
        editTextPassword = view.findViewById(R.id.password);
        buttonLogin = view.findViewById(R.id.btn_login);
        progressBar = view.findViewById(R.id.progressBar);
        textView = view.findViewById(R.id.signUpNow);
        textView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                NavHostFragment.findNavController(Login.this)
                        .navigate(R.id.action_Login_to_SignUp);
                //Intent intent = new Intent(getActivity(), SignUp.class);
                //startActivity(intent);
                //getActivity().finish();
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String email;
                String password;
                //read email and password:
                email = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());

                if (TextUtils.isEmpty(email)){
                    Toast.makeText(getActivity(), "Please enter email", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    Toast.makeText(getActivity(), "Please enter password", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    Toast.makeText(getActivity(), "Login Successful", Toast.LENGTH_SHORT).show();
                                    //Intent intent = new Intent(getActivity(), LogoutActivity.class);
                                    //startActivity(intent);
                                    //getActivity().finish();
                                    NavHostFragment.findNavController(Login.this)
                                            .navigate(R.id.action_Login_to_logout);

                                } else {
                                    //display message if email already exists (didnt do that yet):

                                    //or, other issue:
                                    Toast.makeText(getActivity(), "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}