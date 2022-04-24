package com.example.mywork2.LogInFragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
    private final StringBuffer key = new StringBuffer("12345678");//key for encryption

    public NewAccountFragment() {
        // Required empty public constructor
    }


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
                initView();
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
        boolean information = false;//to control the loop
        while (!information){
            //to check email address
            String reg = getString(R.string.reg);
            if(username.getText().toString().length() == 0 ){//no username found
                username_warn.setText(R.string.please_enter_username);
                return;
            }else if(username.getText().toString().contains("@")){//username should not contain @
                username_warn.setText(R.string.username_no_at);
                return;
            }else if (username.getText().toString().length()>40){//username's length should be less than 25
                username_warn.setText(R.string.username_too_long);
                return;
            }else if(!email.getText().toString().matches(reg)||!email.getText().toString().endsWith(".ac.uk")
            || !(email.getText().toString().length() <40)){//wrong email address
                email_warn.setText(R.string.email_end);
                return;
            }else if(password.getText().toString().length()>16){//password's length should be less than 16
                password_warn.setText(R.string.password_long);
                return;
            }else if(password.getText().toString().length()==0) {//no password found
                password_warn.setText(R.string.enter_password);
                return;
            }else if(confirmPassword.getText().toString().length()==0){//no confirmation
                confirm_warn.setText(R.string.please_confirm);
                return;
            }else if(!password.getText().toString().equals(confirmPassword.getText().toString())){
                confirm_warn.setText(R.string.confirm_failed);
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
                    username_warn.setText(R.string.usernameused);
                    return;
                }else if(userDao.addUser(user)==2){//email address has been used
                    email_warn.setText(R.string.emailused);
                    return;
                }else if(userDao.addUser(user)==-1){//something wrong with the database
                    confirm_warn.setText("There is something wrong with our database");
                    return;
                }else {
                    information = true;
                    Intent intent = new Intent(getActivity(), LogInActivity.class);
                    startActivity(intent);

                }
            }

        }


    }



}