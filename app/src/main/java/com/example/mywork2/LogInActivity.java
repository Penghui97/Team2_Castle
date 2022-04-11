package com.example.mywork2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LogInActivity extends AppCompatActivity implements View.OnClickListener{
    EditText userName;
    EditText password;
    TextView logIn;
    TextView forgetPassword;
    TextView createAccount;
    TextView customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        userName = findViewById(R.id.username);
        userName.setOnClickListener(this);
        password = findViewById(R.id.password);
        password.setOnClickListener(this);
        logIn = findViewById(R.id.LOGIN);
        logIn.setOnClickListener(this);
        forgetPassword = findViewById(R.id.forget_password);
        forgetPassword.setOnClickListener(this);
        createAccount = findViewById(R.id.create_account);
        createAccount.setOnClickListener(this);
        customer = findViewById(R.id.customer_login);
        customer.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.LOGIN:
                Toast.makeText(LogInActivity.this,"Log in",Toast.LENGTH_SHORT).show();
                break;
            case R.id.forget_password:
                Toast.makeText(LogInActivity.this,"Forget Password",Toast.LENGTH_SHORT).show();
                break;
            case R.id.create_account:
                Toast.makeText(LogInActivity.this,"Create Account",Toast.LENGTH_SHORT).show();
                break;
            case R.id.customer_login:
                mainActivity();
                break;
        }
    }

    public void mainActivity(){
        Intent intent = new Intent(LogInActivity.this,MainActivity.class);
        startActivity(intent);
    }
}