package com.example.shopeaze;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class OwnerSignUp extends Fragment {
    TextInputEditText editTextEmail, editTextPassword, editStoreName;
    Button buttonSignUp;
    TextView textView;
    ProgressBar progressBar;
    private FirebaseDatabase db;
    FirebaseAuth mAuth;


    public void onStart() {
        super.onStart();
        //new line:
        db = FirebaseDatabase.getInstance("https://grocery-d4fbb-default-rtdb.firebaseio.com//");
        //old:
        mAuth = FirebaseAuth.getInstance();
        // Check if user is already signed in (non-null)
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            NavHostFragment.findNavController(OwnerSignUp.this)
                    .navigate(R.id.action_OwnerSignUp_to_logout);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_sign_up_owner, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTextEmail = view.findViewById(R.id.email);
        editTextPassword = view.findViewById(R.id.password);
        editStoreName = view.findViewById(R.id.store_name);
        buttonSignUp = view.findViewById(R.id.btn_signup);
        progressBar = view.findViewById(R.id.progressBar);
        textView = view.findViewById(R.id.loginNow);

        textView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                NavHostFragment.findNavController(OwnerSignUp.this)
                        .navigate(R.id.action_OwnerSignUp_to_Login);
            }
        });

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference ref= db.getReference();   //new
                progressBar.setVisibility(View.VISIBLE);
                String email;
                String password;
                String storeName;
                //read email and password:
                email = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());
                storeName = editStoreName.getText().toString();

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
                if (TextUtils.isEmpty(storeName)){
                    Toast.makeText(getActivity(), "Please enter a store name", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }
                if (password.length() < 6){
                    Toast.makeText(getActivity(), "Password should be at least 6 characters", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }


                //Check if there are duplicate store names in the database if there are, don't create account
                Query query = ref.child("Users").child("StoreOwner").orderByChild("StoreName").equalTo(storeName);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            Toast.makeText(getActivity(), "Store name already exists", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            //throw new AppExceptions.StoreNameAlreadyExistsException("Store name already exists");
                            return;
                        }

                        mAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        progressBar.setVisibility(View.GONE);
                                        if (task.isSuccessful()) {
                                            FirebaseUser owner = mAuth.getCurrentUser();
                                            StoreOwner storeOwner = new StoreOwner(email, password, editStoreName.getText().toString());
                                            DatabaseReference userRef = ref.child("Users").child("StoreOwner").child(owner.getUid());
                                            userRef.child("Email").setValue(email);
                                            userRef.child("StoreName").setValue(storeName);
                                            userRef.child("Product List").setValue(storeOwner.getProducts());
                                            //ref.child("Users").child("Shopper").child("Email").setValue(email); //new line
                                            Toast.makeText(getActivity(), "Account Created.",
                                                    Toast.LENGTH_SHORT).show();
                                            NavHostFragment.findNavController(OwnerSignUp.this)
                                                    .navigate(R.id.action_OwnerSignUp_to_logout);

                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Toast.makeText(getActivity(), "Authentication failed.",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });

    }

}
