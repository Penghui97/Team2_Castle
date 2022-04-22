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
import com.example.mywork2.dao.UserDao;
import com.example.mywork2.domain.User;


public class ForgetPasswordFragment extends Fragment {


    public ForgetPasswordFragment() {
        // Required empty public constructor
    }

    private EditText username_v, email_v;
    private TextView username_w, email_w,remind_password;
    private Button find;
    public User user;
    public String username, email, password;


    //receive the data from the database
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @SuppressLint("SetTextI18n")
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 0x11) {
                password = NewAccountFragment.hex2Str(user.getPassword());
                remind_password.setText("Your password is: "+password);
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forget_password, container, false);

    }

    @SuppressLint("SetTextI18n")
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
            //now we get the username and email
            //check the database and verify, show password
            showPassword();


        });


    }

    //init warn textview to blank
    private void initWarn() {
        username_w.setText("");
        email_w.setText("");
        remind_password.setText("");
    }

    private void checkLocalInfo() {
        boolean information = false;
        while (!information) {
            //regex to check email
            //referenced from https://blog.csdn.net/qq_60750453/article/details/123709670
            String reg = getString(R.string.reg);
            if (username_v.getText().toString().length() == 0) {//no username found
                username_w.setText(R.string.please_enter_username);
                return;
            } else if (username_v.getText().toString().length() > 25) {//username's length should be less than 25
                username_w.setText(R.string.username_too_long);
                return;
            } else if(!email_v.getText().toString().matches(reg)
            ||!email_v.getText().toString().endsWith(".ac.uk")){//wrong email address
                email_w.setText(R.string.email_end);
                return;
            } else {
                username = username_v.getText().toString();
                email = email_v.getText().toString();
                information = true;
            }

        }

    }

    @SuppressLint("SetTextI18n")
    public void showPassword() {
        new Thread(() ->{
            UserDao userDao = new UserDao();
            try {
                user = userDao.getUserByUsername(username);
                if(user == null){//no user found
                    username_w.setText("Your username has not been registered !!!");
                } else {//check the email
                    if(!user.getEmail().equals(email)){//the email is incorrect
                        email_w.setText("Your email is incorrect !!!");
                    }else {//verify successfully
                        Message message = handler.obtainMessage();
                        message.what = 0x11;
                        handler.sendMessage(message);

                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }).start();
    }

}