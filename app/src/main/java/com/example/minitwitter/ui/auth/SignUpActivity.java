package com.example.minitwitter.ui.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.minitwitter.R;
import com.example.minitwitter.common.Constants;
import com.example.minitwitter.common.SharedPreferencesManager;
import com.example.minitwitter.retrofit.MiniTwitterClient;
import com.example.minitwitter.retrofit.MiniTwitterService;
import com.example.minitwitter.retrofit.request.RequestSignUp;
import com.example.minitwitter.retrofit.response.ResponseAuth;
import com.example.minitwitter.ui.DashboardActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnSignUp;
    TextView tvLogIn;
    EditText etUsername, etEmail, etPassword;
    MiniTwitterClient miniTwitterClient;
    MiniTwitterService miniTwitterService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getSupportActionBar().hide();
        
        retrofitInit();
        findViews();
        events();
    }

    private void retrofitInit() {
        miniTwitterClient = MiniTwitterClient.getInstance();
        miniTwitterService = miniTwitterClient.getMiniTwitterService();
    }

    private void findViews() {
        btnSignUp = findViewById(R.id.buttonSignUp);
        tvLogIn = findViewById(R.id.textViewGoLogIn);
        etUsername = findViewById(R.id.editTextUserName);
        etEmail = findViewById(R.id.editTextEmail);
        etPassword = findViewById(R.id.editTextPassword);
    }
    
    private void events() {
        btnSignUp.setOnClickListener(this);
        tvLogIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id){
            case R.id.buttonSignUp:
                goToSignUp();
                break;
            case R.id.textViewGoLogIn:
                goLogIn();
                break;
        }
    }

    private void goToSignUp() {
        String email = etEmail.getText().toString();
        String userName = etUsername.getText().toString();
        String password = etPassword.getText().toString();

        if (userName.isEmpty()){
            etUsername.setError("User Name needed to continue");
        } else if (email.isEmpty()){
            etEmail.setError("Email is required to continue");
        } else if(password.isEmpty() || password.length() < 4){
            etPassword.setError("The password is required to continue and must have 4 characters at least");
        } else {
            String code = "UDEMYANDROID";
            RequestSignUp requestSignUp = new RequestSignUp(userName,email,password, code);
            Call<ResponseAuth> call = miniTwitterService.doSignup(requestSignUp);

            call.enqueue(new Callback<ResponseAuth>() {
                @Override
                public void onResponse(Call<ResponseAuth> call, Response<ResponseAuth> response) {
                    if(response.isSuccessful()){

                        SharedPreferencesManager.setSomeStringValue(Constants.PREF_TOKEN, response.body().getToken());
                        SharedPreferencesManager.setSomeStringValue(Constants.PREF_USERNAME, response.body().getUsername());
                        SharedPreferencesManager.setSomeStringValue(Constants.PREF_EMAIL, response.body().getEmail());
                        SharedPreferencesManager.setSomeStringValue(Constants.PREF_PHOTOURL, response.body().getPhotoUrl());
                        SharedPreferencesManager.setSomeStringValue(Constants.PREF_CREATED, response.body().getCreated());
                        SharedPreferencesManager.setSomeBooleanValue(Constants.PREF_ACTIVE, response.body().getActive());

                        Intent i = new Intent(SignUpActivity.this, DashboardActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        Toast.makeText(SignUpActivity.this, "Something went wron, please check the information you entered is correct", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseAuth> call, Throwable t) {
                    Toast.makeText(SignUpActivity.this, "Connection Error", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void goLogIn() {
        Intent i = new Intent(SignUpActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }
    
    
}