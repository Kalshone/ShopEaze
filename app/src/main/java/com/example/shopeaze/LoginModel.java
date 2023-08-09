package com.example.shopeaze;

import com.google.firebase.auth.FirebaseAuth;

public class LoginModel implements LoginContract.Model{
    private FirebaseAuth mAuth;
    public boolean status;
    public LoginModel() {
        mAuth = FirebaseAuth.getInstance();
    }
    public void loginUser(String email, String password, final OnLoginFinishedListener listener) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        setStatus(true);
                        listener.onLoginSuccess();
                    } else {
                        setStatus(false);
                        listener.onLoginFailure();
                    }
                });
    }
    public boolean getStatus(){
        return status;
    }
    public void setStatus(boolean status){
        this.status=status;
    }
    public interface OnLoginFinishedListener {
        void onLoginSuccess();
        void onLoginFailure();
    }

}
