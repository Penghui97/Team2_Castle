package com.example.mywork2.LogInFragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mywork2.R;
import com.example.mywork2.domain.User;


public class ForgetPasswordFragment extends Fragment {


    public ForgetPasswordFragment() {
        // Required empty public constructor
    }

    private TextView username_v, email_v, remind_password;
    private EditText username_w, email_w;
    private Button find;
    public User user;
    public String username;


    //receive the data from the database
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 0x11) {

            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forget_password, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        username_v = view.findViewById(R.id.forget_username_login);
        email_v = view.findViewById(R.id.find_password_email);
        username_w = view.findViewById(R.id.username_forgot_warn);
        email_w = view.findViewById(R.id.email_forgot_warn);
        find = view.findViewById(R.id.btn_find_password);
        remind_password = view.findViewById(R.id.remind_password);

        find.setOnClickListener(view1 -> {
            initWarn();//init the warn view to blank
            checkLocalInfo();

        });


    }

    //init warn textview to blank
    private void initWarn() {
        username_w.setText("");
        email_w.setText("");
        remind_password.setText("");
    }

    @SuppressLint("SetTextI18n")
    private void checkLocalInfo() {
        boolean information = false;
        while (!information) {
            //regex to check email
            //referenced from https://blog.csdn.net/qq_60750453/article/details/123709670
            String reg = "^[A-Za-z\\d]+([-_.][A-Za-z\\d]+)*@([A-Za-z\\d]+[-.])+[A-Za-z\\d]{2,4}$";
            if (username_v.getText().toString().length() == 0) {//no username found
                username_w.setText("Please enter a username !!!");
                return;
            } else if (username_v.getText().toString().length() > 25) {//username's length should be less than 25
                username_w.setText("Username's length should not be longer than 25 !!!");
                return;
            } else if(email_v.getText().toString().contains("@")&&!email_v.getText().toString().matches(reg)){//wrong email address
                email_w.setText("Email address is incorrect !!!");
                return;
            } else {
                information = true;
            }

        }

    }
}