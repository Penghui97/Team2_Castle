package com.example.mywork2.LogInFragment;

import android.annotation.SuppressLint;
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

import com.example.mywork2.MainActivity;
import com.example.mywork2.R;
import com.example.mywork2.Util.DESUtil;
import com.example.mywork2.dao.UserDao;
import com.example.mywork2.domain.User;


public class NewAccountFragment extends Fragment {
    private EditText username;
    private EditText email;
    private EditText password;
    private EditText confirmPassword;
    private TextView username_warn, email_warn, password_warn, confirm_warn;
    private final StringBuffer key = new StringBuffer("12345678");

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

            new Thread(new Runnable(){
                @SuppressLint("LongLogTag")
                @Override
                public void run() {
                    initView();
                    checkInfo();

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

    //check the information is right
    @SuppressLint("SetTextI18n")
    private void checkInfo() {
        boolean information = false;//to control the loop
        while (!information){
            //to check email address
            String reg = "^[A-Za-z\\d]+([-_.][A-Za-z\\d]+)*@([A-Za-z\\d]+[-.])+[A-Za-z\\d]{2,4}$";
            if(username.getText().toString().length() == 0 ){//no username found
                username_warn.setText("Please enter a username !!!");
                return;
            }else if(username.getText().toString().contains("@")){//username should not contain @
                username_warn.setText("Username should not contain @ !!!");
                return;
            }else if (username.getText().toString().length()>25){//username's length should be less than 25
                username_warn.setText("Username's length should not be longer than 25 !!!");
                return;
            }else if(!email.getText().toString().matches(reg)){//wrong email address
                email_warn.setText("Email address is incorrect !!!");
                return;
            }else if(password.getText().toString().length()>16){//password's length should be less than 16
                password_warn.setText("Password's length should be less than 16 !!!");
                return;
            }else if(password.getText().toString().length()==0) {//no password found
                password_warn.setText("Please enter your password !!!");
                return;
            }else if(confirmPassword.getText().toString().length()==0){//no confirmation
                confirm_warn.setText("Please confirm your password !!!");
                return;
            }else if(!password.getText().toString().equals(confirmPassword.getText().toString())){
                confirm_warn.setText("Password conformation failed !!!");
                return;
            } else {
                //add the new user to the database
                String name = username.getText().toString();
                String emailAddress = email.getText().toString();
                String _password = password.getText().toString();
                StringBuffer encryptedPassword = new StringBuffer(_password.toString());//encrypt the password via DES algorithm
                int mode = 0;
                DESUtil instance = null;
                try {
                    instance = new DESUtil(encryptedPassword, key, mode);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (instance != null) {
                    instance.start();
                }
                User user = null;
                if (instance != null) {
                    user = new User(name,name,emailAddress,instance.getCiphertext().toString());
                }
                UserDao userDao = new UserDao();
                if (user != null) {
                    if (userDao.addUser(user)==1){//username has been used
                        username_warn.setText("Your username has been used !!!");
                        return;
                    }else if(userDao.addUser(user)==2){//email address has been used
                        email_warn.setText("Your email has been used !!!");
                        return;
                    }else if(userDao.addUser(user)==-1){//something wrong with the database
                        confirm_warn.setText("There is something wrong with our database");
                        return;
                    }else {
                        information = true;
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);

                    }
                }
            }

        }




    }
}