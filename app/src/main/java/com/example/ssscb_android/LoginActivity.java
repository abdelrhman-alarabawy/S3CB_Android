package com.example.ssscb_android;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {


    private String userId ;
    private String password;
    private Button login_btn ;
    private EditText et_Email;
    private EditText et_Pass ;
    private EditText et_token;
    private Button btn_token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login_btn = findViewById(R.id.btn_login);
        et_Email = findViewById(R.id.et_username);
        et_Pass = findViewById(R.id.et_password);
        et_token = findViewById(R.id.et_token);
        btn_token = findViewById(R.id.btn_get_token);
        btn_token.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseMessaging.getInstance().getToken()
                        .addOnCompleteListener(new OnCompleteListener<String>() {
                            @Override
                            public void onComplete(@NonNull Task<String> task) {
                                if (!task.isSuccessful()) {
                                    Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                                    return;
                                }

                                // Get new FCM registration token
                                String token = task.getResult();
                                // Log and toast

                                et_token.setText(token , TextView.BufferType.EDITABLE);

                            }
                        });

            }
        });
        login_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                userId = et_Email.getText().toString();
                password = et_Pass.getText().toString();
                if (checkValidation()) {
                    if (CommonMethod.isNetworkAvailable(LoginActivity.this))
                    {
                        loginRetrofit2Api(userId, password);
                        Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
                        myIntent.putExtra("key",userId);
                        startActivity(myIntent);
                    }
                    else
                        CommonMethod.showAlert("Internet Connectivity Failure", LoginActivity.this);
                }
                FirebaseMessaging.getInstance().getToken()
                        .addOnCompleteListener(new OnCompleteListener<String>() {
                            @Override
                            public void onComplete(@NonNull Task<String> task) {
                                if (!task.isSuccessful()) {
                                    Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                                    return;
                                }

                                // Get new FCM registration token
                                String token = task.getResult();
                                // Log and toast

                                Log.d("TAG", token);
                                Toast.makeText(LoginActivity.this, token, Toast.LENGTH_LONG).show();
                                putResults(token,userId);

                            }
                        });


            }


        });
    }
    private void loginRetrofit2Api(String userId, String password) {
        final LoginResponse login = new LoginResponse(userId, password);
        Call<LoginResponse> call1 = RetrofitClient.getInstance().getMyApi().createUser(login);
        call1.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                LoginResponse loginResponse = response.body();

                Log.e("keshav", "loginResponse 1 --> " + loginResponse);
                if (loginResponse != null) {
                    Log.e("keshav", "getUserId          -->  " + loginResponse.getUserId());

                    String responseCode = loginResponse.getResponseCode();
                    Log.e("keshav", "getResponseCode  -->  " + loginResponse.getResponseCode());
                    Log.e("keshav", "getResponseMessage  -->  " + loginResponse.getResponseMessage());
                    if (responseCode != null && responseCode.toString().compareTo("401")==0) {
                        {
                            Toast.makeText(LoginActivity.this, "Invalid Login Details \n Please try again", Toast.LENGTH_SHORT).show();

                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Welcome " + loginResponse.getUserId(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "onFailure called ", Toast.LENGTH_SHORT).show();
                call.cancel();
            }
        });
    }

    public boolean checkValidation() {
        userId = et_Email.getText().toString();
        password = et_Pass.getText().toString();

        Log.e("Keshav", "userId is -> " + userId);
        Log.e("Keshav", "password is -> " + password);

        if (et_Email.getText().toString().trim().equals("")) {
            CommonMethod.showAlert("UserId Cannot be left blank", LoginActivity.this);
            return false;
        } else if (et_Pass.getText().toString().trim().equals("")) {
            CommonMethod.showAlert("password Cannot be left blank", LoginActivity.this);
            return false;
        }
        return true;
    }



    private void putResults(String devicetoken,String userName) {
        final LoginResponse login = new LoginResponse(userId, password);
        Call<String> call1 = RetrofitClient.getInstance().getMyApi().putResults(userName,devicetoken);
        call1.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String Response = response.body();

                if (Response != null) {
                    Log.e("keshav", "The response : " + Response.toString());


                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "onFailure called 2", Toast.LENGTH_SHORT).show();
                call.cancel();
            }
        });
    }

}