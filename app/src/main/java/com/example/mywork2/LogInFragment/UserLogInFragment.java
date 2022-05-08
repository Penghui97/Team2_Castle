package com.example.mywork2.LogInFragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mywork2.LogInActivity;
import com.example.mywork2.MainActivity;
import com.example.mywork2.R;
import com.example.mywork2.Util.ImageUtil;
import com.example.mywork2.Util.PasswordUtil;
import com.example.mywork2.dao.UserDao;
import com.example.mywork2.domain.User;

import java.io.UnsupportedEncodingException;

public class UserLogInFragment extends Fragment {
    private EditText username, password;
    private TextView username_warn;
    private TextView password_warn, forgot, forgot_warn;
    Button login,back;
    NavController navController;
    String rememberName, rememberPassword;

    public UserLogInFragment() {
        // Required empty public constructor
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 0x11://no username found
                    username_warn.setText(R.string.please_enter_username);
                    break;
                case 0x22://username is too long
                    username_warn.setText(R.string.username_too_long);
                    break;
                case 0x33://no password found
                    password_warn.setText(R.string.enter_password);
                    break;
                case 0x44://password too long
                    password_warn.setText(R.string.password_long);
                    break;
                case 0x55://wrong email address
                    username_warn.setText(R.string.email_end);
                    break;
                case 0x66://no email found in db
                    username_warn.setText(R.string.emailhasnotbeenregistered);
                    break;
                case 0x77://password is wrong
                    password_warn.setText(R.string.passwordwrong);
                    break;
                case 0x88://no username found in db
                    username_warn.setText(R.string.usernamenotregister);
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user__log__in_, container, false);

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        username = view.findViewById(R.id.username_login);
        username_warn = view.findViewById(R.id.username_login_warn);
        password = view.findViewById(R.id.password_login);
        password_warn = view.findViewById(R.id.password_login_warn);
        forgot_warn = view.findViewById(R.id.pass_forgot_warn);


        navController = Navigation.findNavController(view);
        forgot = view.findViewById(R.id.forget_password);
        forgot.setOnClickListener(view1 -> navController.navigate(R.id.action_userLogInFragment_to_forgetPasswordFragment));
        //get data from login activity
        Bundle bundle = getArguments();
        if(bundle!=null){
            rememberName = bundle.getString("username");
            rememberPassword = bundle.getString("password");
            username.setText(rememberName);
            password.setText(rememberPassword);
            forgot_warn.setTextColor(Color.BLUE);
            forgot_warn.setText(R.string.findback);

            //show password
            forgot.setOnTouchListener((view1, motionEvent) -> {
                if (motionEvent.getAction()== MotionEvent.ACTION_DOWN)
                    password.setInputType(InputType.TYPE_CLASS_TEXT);
                if (motionEvent.getAction()== MotionEvent.ACTION_UP)
                    password.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
                return true;
            });

        }
        //buttons
        back = view.findViewById(R.id.btn_back_);
        back.setOnClickListener(view1 -> startActivity(new Intent(getActivity(), LogInActivity.class)));
        login = view.findViewById(R.id.btn_login);
        login.setOnClickListener(view1 -> new Thread(() -> {
            initView();
            try {
                checkInfoAndLogin();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }).start());




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
                Message message = handler.obtainMessage();
                message.what = 0x11;
                handler.sendMessage(message);
                return;
            }else if (username.getText().toString().length()>40){//username's length should be less than 40
                Message message = handler.obtainMessage();
                message.what = 0x22;
                handler.sendMessage(message);
                return;
            }else if(password.getText().toString().length()==0) {//no password found
                Message message = handler.obtainMessage();
                message.what = 0x33;
                handler.sendMessage(message);
                return;
            }else if(password.getText().toString().length()>16){//password's length should be less than 16
                Message message = handler.obtainMessage();
                message.what = 0x44;
                handler.sendMessage(message);
                return;
            }else if(username.getText().toString().contains("@")&&(!username.getText().toString().matches(reg)
            ||!username.getText().toString().endsWith(".ac.uk"))){//wrong email address
                Message message = handler.obtainMessage();
                message.what = 0x55;
                handler.sendMessage(message);
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
                            Message message = handler.obtainMessage();
                            message.what = 0x66;
                            handler.sendMessage(message);
                            return;
                        }else {
                            if (!plain_password.equals(PasswordUtil.hex2Str(user.getPassword()))){//password is wrong
                                Message message = handler.obtainMessage();
                                message.what = 0x77;
                                handler.sendMessage(message);

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
                            Message message = handler.obtainMessage();
                            message.what = 0x88;
                            handler.sendMessage(message);
                            return;
                        }else {
                            if (!plain_password.equals(PasswordUtil.hex2Str(user.getPassword()))){//password is wrong
                                Message message = handler.obtainMessage();
                                message.what = 0x77;
                                handler.sendMessage(message);
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