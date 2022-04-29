package com.example.mywork2.LogInFragment;

import android.annotation.SuppressLint;
import android.content.Intent;
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

import com.example.mywork2.LogInActivity;
import com.example.mywork2.R;
import com.example.mywork2.dao.UserDao;
import com.example.mywork2.domain.User;

import java.io.UnsupportedEncodingException;

import static com.example.mywork2.Util.PasswordUtil.str2Hex;


public class NewAccountFragment extends Fragment {
    private EditText username;
    private EditText email;
    private EditText password;
    private EditText confirmPassword;
    private TextView username_warn, email_warn, password_warn, confirm_warn;

    public NewAccountFragment() {
        // Required empty public constructor
    }
    //receive the data from the database
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 0x11:
                    username_warn.setText(R.string.please_enter_username);
                    break;
                case 0x22:
                    username_warn.setText(R.string.username_no_at);
                    break;
                case 0x33:
                    username_warn.setText(R.string.username_too_long);
                    break;
                case 0x44:
                    email_warn.setText(R.string.email_end);
                    break;
                case 0x55:
                    password_warn.setText(R.string.password_long);
                    break;
                case 0x66:
                    password_warn.setText(R.string.enter_password);
                    break;
                case 0x77:
                    confirm_warn.setText(R.string.please_confirm);
                    break;
                case 0x88:
                    confirm_warn.setText(R.string.confirm_failed);
                    break;
                case 0x91:
                    username_warn.setText(R.string.usernameused);
                    break;
                case 0x92:
                    email_warn.setText(R.string.emailused);
                    break;
                case 0x93:
                    confirm_warn.setText(R.string.wrongdatabase);
                    break;
                case 0x94:
                    initView();
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new__account_, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        username = view.findViewById(R.id.username_registry);
        username_warn = view.findViewById(R.id.username_warn);
        email = view.findViewById(R.id.email_registry);
        email_warn = view.findViewById(R.id.email_warn);
        password = view.findViewById(R.id.password_registry);
        password_warn = view.findViewById(R.id.password_warn);
        confirmPassword = view.findViewById(R.id.confirm_password_registry);
        confirm_warn = view.findViewById(R.id.confirm_warn);
        Button register = view.findViewById(R.id.btn_regstery);

        register.setOnClickListener(view1 -> {

            new Thread(() -> {
                Message message = handler.obtainMessage();
                message.what = 0x94;
                handler.sendMessage(message);
                try {
                    checkInfoAndRegister();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }).start();

        });
    }

    //to initialize the warning textviews
    private void initView() {
        confirm_warn.setText("");
        email_warn.setText("");
        username_warn.setText("");
        password_warn.setText("");
    }

    //check the information is right and register
    private void checkInfoAndRegister() throws UnsupportedEncodingException {
        new Thread(()->{
        boolean information = false;//to control the loop
        while (!information){
            //to check email address
            String reg = getString(R.string.reg);
            if(username.getText().toString().length() == 0 ){//no username found
                Message message = handler.obtainMessage();
                message.what = 0x11;
                handler.sendMessage(message);
                return;
            }else if(username.getText().toString().contains("@")){//username should not contain @
                Message message = handler.obtainMessage();
                message.what = 0x22;
                handler.sendMessage(message);
                return;
            }else if (username.getText().toString().length()>40){//username's length should be less than 25
                Message message = handler.obtainMessage();
                message.what = 0x33;
                handler.sendMessage(message);
                return;
            }else if(!email.getText().toString().matches(reg)||!email.getText().toString().endsWith(".ac.uk")
                    || !(email.getText().toString().length() <40)){//wrong email address
                Message message = handler.obtainMessage();
                message.what = 0x44;
                handler.sendMessage(message);
                return;
            }else if(password.getText().toString().length()>16){//password's length should be less than 16
                Message message = handler.obtainMessage();
                message.what = 0x55;
                handler.sendMessage(message);
                return;
            }else if(password.getText().toString().length()==0) {//no password found
                Message message = handler.obtainMessage();
                message.what = 0x66;
                handler.sendMessage(message);
                return;
            }else if(confirmPassword.getText().toString().length()==0){//no confirmation
                Message message = handler.obtainMessage();
                message.what = 0x77;
                handler.sendMessage(message);
                return;
            }else if(!password.getText().toString().equals(confirmPassword.getText().toString())){
                Message message = handler.obtainMessage();
                message.what = 0x88;
                handler.sendMessage(message);
                return;
            } else {
                //add the new user to the database
                String name = username.getText().toString();
                String emailAddress = email.getText().toString();
                String _password = password.getText().toString();
                //save the user into database, convert the password to hex
                User user = new User(name,name,emailAddress,str2Hex(_password));

                //check the database
                UserDao userDao = new UserDao();
                if (userDao.addUser(user)==1){//username has been used
                    Message message = handler.obtainMessage();
                    message.what = 0x91;
                    handler.sendMessage(message);
                    return;
                }else if(userDao.addUser(user)==2){//email address has been used
                    Message message = handler.obtainMessage();
                    message.what = 0x92;
                    handler.sendMessage(message);
                    return;
                }else if(userDao.addUser(user)==-1){//something wrong with the database
                    Message message = handler.obtainMessage();
                    message.what = 0x93;
                    handler.sendMessage(message);
                    return;
                }else {
                    information = true;
                    Intent intent = new Intent(getActivity(), LogInActivity.class);
                    startActivity(intent);

                }
            }

        }
        }).start();


    }



}