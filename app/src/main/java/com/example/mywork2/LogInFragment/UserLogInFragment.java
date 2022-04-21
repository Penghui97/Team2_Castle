package com.example.mywork2.LogInFragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mywork2.MainActivity;
import com.example.mywork2.R;
import com.example.mywork2.Util.UserThreadLocal;
import com.example.mywork2.dao.UserDao;
import com.example.mywork2.domain.User;

import java.io.UnsupportedEncodingException;

public class UserLogInFragment extends Fragment {
    private EditText username, password;
    private TextView username_warn, password_warn, forgot;
    Button login;
    NavController navController;

    public UserLogInFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user__log__in_, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        username = view.findViewById(R.id.username_login);
        username_warn = view.findViewById(R.id.username_login_warn);
        password = view.findViewById(R.id.password_login);
        password_warn = view.findViewById(R.id.password_login_warn);
        
        login = view.findViewById(R.id.btn_login);
        login.setOnClickListener(view1 -> {
            new Thread(() -> {
                initView();
                try {
                    checkInfoAndLogin();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }).start();
            
        });


        navController = Navigation.findNavController(view);
        forgot = view.findViewById(R.id.forget_password);
        forgot.setOnClickListener(view1 -> {
            navController.navigate(R.id.action_userLogInFragment_to_forgetPasswordFragment);
        });



    }

    //check the information is right and login
    private void checkInfoAndLogin() throws UnsupportedEncodingException {
        String name, plain_password;
        User user = null;
        boolean information = false;
        while (!information){
            //regex to check email
            //referenced from https://blog.csdn.net/qq_60750453/article/details/123709670
            String reg = getString(R.string.reg);
            if(username.getText().toString().length() == 0 ) {//no username found
                username_warn.setText(R.string.please_enter_username);
                return;
            }else if (username.getText().toString().length()>25){//username's length should be less than 25
                username_warn.setText(R.string.username_too_long);
                return;
            }else if(password.getText().toString().length()==0) {//no password found
                password_warn.setText(R.string.enter_password);
                return;
            }else if(password.getText().toString().length()>16){//password's length should be less than 16
                password_warn.setText(R.string.password_long);
                return;
            }else if(username.getText().toString().contains("@")&&!username.getText().toString().matches(reg)
            ||!username.getText().toString().endsWith(".ac.uk")){//wrong email address
                username_warn.setText(R.string.email_end);
                return;
            }else {
                //check the database and verify
                UserDao userDao = new UserDao();
                name = username.getText().toString();
                plain_password = password.getText().toString();

                if(name.contains("@")){//if the user login with email
                    try {
                        user = userDao.getUserByEmail(name);
                        if(user == null) {//no user found
                            username_warn.setText("Your email has not been registered !!!");
                            return;
                        }else {
                            if (!plain_password.equals(NewAccountFragment.hex2Str(user.getPassword()))){//password is wrong
                                password_warn.setText("Your password is incorrect !!!");

                            }else {
                                information = true;
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }else {//if the user login with username
                    try {
                        user = userDao.getUserByUsername(name);
                        if(user == null) {//no user found
                            username_warn.setText("Your username has not been registered !!!");
                            return;
                        }else {
                            if (!plain_password.equals(NewAccountFragment.hex2Str(user.getPassword()))){//password is wrong
                                password_warn.setText("Your password is incorrect !!!");
                            }else {
                                information = true;
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

            }
        }

        //if the user login successfully
        //pass his username to the MainActivity
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.putExtra("username", user.getUsername());
        startActivity(intent);

    }


    //initialize the warn text views
    private void initView() {
        username_warn.setText("");
        password_warn.setText("");
    }
}