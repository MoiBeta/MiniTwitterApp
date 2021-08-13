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
import com.example.minitwitter.retrofit.request.RequestLogin;
import com.example.minitwitter.retrofit.response.ResponseAuth;
import com.example.minitwitter.ui.DashboardActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnLogin;
    TextView tvSignUp;
    EditText etEmail, etPassword;
    MiniTwitterClient miniTwitterClient;
    MiniTwitterService miniTwitterService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        btnLogin = findViewById(R.id.buttonLogin);
        tvSignUp = findViewById(R.id.textViewGoSignUp);
        etEmail = findViewById(R.id.editTextEmail);
        etPassword = findViewById(R.id.editTextPassword);
    }

    private void events() {
        btnLogin.setOnClickListener(this);
        tvSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.buttonLogin:
                goToLogin();
                break;
            case R.id.textViewGoSignUp:
                goSignUp();
                break;
        }
    }

    private void goToLogin() {
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        if(email.isEmpty()){
            etEmail.setError("Email required");
        } else if(password.isEmpty()){
            etPassword.setError("Password required");
        } else {
            RequestLogin requestLogin = new RequestLogin(email, password);
            Call<ResponseAuth> call = miniTwitterService.doLogin(requestLogin);

            call.enqueue(new Callback<ResponseAuth>() {
                @Override
                public void onResponse(Call<ResponseAuth> call, Response<ResponseAuth> response) {
                    if(response.isSuccessful()){
                        Toast.makeText(MainActivity.this, "Sesion iniciada correctamente", Toast.LENGTH_SHORT).show();

                        assert response.body() != null;
                        SharedPreferencesManager.setSomeStringValue(Constants.PREF_TOKEN, response.body().getToken());
                        SharedPreferencesManager.setSomeStringValue(Constants.PREF_USERNAME, response.body().getUsername());
                        SharedPreferencesManager.setSomeStringValue(Constants.PREF_EMAIL, response.body().getEmail());
                        SharedPreferencesManager.setSomeStringValue(Constants.PREF_PHOTOURL, response.body().getPhotoUrl());
                        SharedPreferencesManager.setSomeStringValue(Constants.PREF_CREATED, response.body().getCreated());
                        SharedPreferencesManager.setSomeBooleanValue(Constants.PREF_ACTIVE, response.body().getActive());

                        Intent i = new Intent(MainActivity.this, DashboardActivity.class);
                        startActivity(i);

                        //We can destroy this activity so the user can not come back
                        finish();
                    } else {
                        Toast.makeText(MainActivity.this, "Something went wrong, please check your login information", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseAuth> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Connection issues, please try again", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void goSignUp() {
        Intent i = new Intent(MainActivity.this, SignUpActivity.class);
        startActivity(i);
        finish();
    }
}