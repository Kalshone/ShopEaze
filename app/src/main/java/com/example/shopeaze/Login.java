package com.example.shopeaze;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class Login extends Fragment implements LoginContract.View {
    TextInputEditText editTextEmail, editTextPassword;
    ImageButton buttonLogin;
    ProgressBar progressBar;
    TextView textView;
    private LoginContract.Presenter presenter;

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
        Button buttonBackToWelcome = view.findViewById(R.id.buttonBackToWelcome);
        progressBar = view.findViewById(R.id.progressBar);
        textView = view.findViewById(R.id.signUpNow);

        presenter = new LoginPresenter(this, new LoginModel());

        textView.setOnClickListener(v -> {
            NavHostFragment.findNavController(Login.this)
                    .navigate(R.id.action_Login_to_SignUp);
        });
        buttonBackToWelcome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(Login.this)
                        .navigate(R.id.action_Login_to_Welcome);
            }
        });

        buttonLogin.setOnClickListener(v -> {
            presenter.loginUser();
        });
    }
    public String getEmail() {
        if (editTextEmail != null) {
            return String.valueOf(editTextEmail.getText());
        }
        return null;
    }
    public String getPassword() {
        if (editTextPassword != null) {
            return String.valueOf(editTextPassword.getText());
        }
        return null;
    }

    @Override
    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showErrorMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoginSuccessMessage() {
        Toast.makeText(getActivity(), "Login Successful", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToStoreList() {
        NavHostFragment.findNavController(Login.this).navigate(R.id.action_Login_to_StoreList);
    }
}