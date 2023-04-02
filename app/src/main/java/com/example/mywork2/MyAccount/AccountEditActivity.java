package com.example.mywork2.MyAccount;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mywork2.MainActivity;
import com.example.mywork2.R;
import com.example.mywork2.dao.UserDao;
import com.example.mywork2.domain.User;
import com.google.android.material.bottomsheet.BottomSheetDialog;
/**
 * @author Penghui Xiao
 * function: for user to edit his account information
 * modification date and description can be found in github repository history
 */
public class AccountEditActivity extends AppCompatActivity implements View.OnClickListener{
    private BottomSheetDialog bottomSheetDialog;
    private View bottomView;
    private ConstraintLayout username,email,account_username, password_edit;
    private TextView bottomHeader,warningMessage, account_nickname, account_email,
            password_w, confirm_w, username_tv;
    private EditText change, password, confirm;
    private Button bottomSaveButton, bottomCancelButton;
    private User user, customer;
    private String username_, email_, nickname, old_password, new_password, password_confirm;
    private boolean email_changed = false;

    //receive the data from the database
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 0x11) {//update nickname
                account_nickname.setText(nickname);
                account_email.setText(email_);
            } else if(msg.what == 0x12) {//wrong email
                warningMessage.setText(R.string.email_end);
            } else if (msg.what == 0x13) {//email used
                warningMessage.setText(R.string.emailused);
            } else if (msg.what == 0x14) {//email edittext changed
                initWarn();//initialize
            }
            else if (msg.what == 0x22) {//update nickname
                account_nickname.setText(customer.getNickname());
                account_email.setText(customer.getEmail());
            }else if (msg.what == 0x33) {//update email
                bottomSheetDialog.dismiss();
                account_email.setText(email_);
                //show the alert dialog to tell user
                email_changed = true;
                //show the dialog to tell to user
                emailChanged();
            }else if (msg.what == 0x44){
                bottomSheetDialog.dismiss();
                emailNotChanged();

            }else if (msg.what == 0x55){
                bottomSheetDialog.dismiss();
                passwordChanged();
                hideEditPassword();
                //remember account
                SharedPreferences spfRecord = getSharedPreferences("remName", MODE_PRIVATE);
                SharedPreferences.Editor edit = spfRecord.edit();
                edit.putString("RemPass",new_password);
                edit.apply();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_edit);

        //get user from main activity
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        username_ = (String)extras.getString("username");
        email_ = (String)extras.getString("email") ;

        Intent intent1 = new Intent(AccountEditActivity.this, MainActivity.class);
        intent1.putExtra("username", username_);
        intent1.putExtra("email",email_);
        initView();

        showUserInfo();

    }

    public void initView(){
        //set action bar
        TextView title = findViewById(R.id.header_title);
        title.setText(R.string.editMyaccount);
        findViewById(R.id.back_button).setOnClickListener(view -> {
            this.finish()
            ;});

        account_nickname = findViewById(R.id.account_nick_name);
        account_email = findViewById(R.id.account_email_address);
        account_username = findViewById(R.id.account_username_show);
        account_username.setOnClickListener(this);
        username_tv = findViewById(R.id.account_username);
        username_tv.setText(username_);

        password_edit = findViewById(R.id.account_password_edit);
        password_edit.setOnClickListener(this);

        //get bottom sheet view
        bottomSheetDialog = new BottomSheetDialog(AccountEditActivity.this,R.style.BottomSheetDialogTheme);
        bottomView = LayoutInflater.from(getApplicationContext()).inflate(
                R.layout.bottom_sheet_account_edit,
                findViewById(R.id.accout_edit_bottom)
        );
        bottomHeader = bottomView.findViewById(R.id.change_item);
        change = bottomView.findViewById(R.id.bottom_sheet_edit_text);
        //the warning messaging text
        warningMessage = bottomView.findViewById(R.id.warn_text);
        bottomSaveButton = bottomView.findViewById(R.id.button_bottom_save);
        password = bottomView.findViewById(R.id.bottom_sheet_edit_password);
        confirm = bottomView.findViewById(R.id.bottom_sheet_edit_confirm);
        password_w = bottomView.findViewById(R.id.warn_password);
        confirm_w = bottomView.findViewById(R.id.warn_confirm);
        bottomCancelButton = bottomView.findViewById(R.id._button_bottom_cancel);
        bottomCancelButton.setOnClickListener(this);
        bottomSheetDialog.setContentView(bottomView);

        username = findViewById(R.id.account_nickname_edit);
        username.setOnClickListener(this);

        email = findViewById(R.id.account_email_edit);
        email.setOnClickListener(this);
    }
    //if email is changed, tell to user
    public void emailChanged(){
        if(email_changed){
            //show the alert dialog to tell user
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.emailchanged).setNegativeButton("OK"
                    , (dialogInterface,i) -> dialogInterface.dismiss()).show();
            email_changed = false;

        }
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.account_nickname_edit:
                hideEditPassword();

                bottomHeader.setText(R.string.edit_name);
                change.setHint(nickname);
                change.setText(nickname);
                bottomSheetDialog.show();
                bottomSaveButton.setOnClickListener(view1 -> {
                    if(!change.getText().toString().equals(nickname)){//if nickname changed
                        initWarn();//initialize warning
                        if(change.getText().toString().length()==0){ //empty nickname
                            warningMessage.setText(R.string.enternickname);
                        }else if (change.getText().toString().length()>40){
                            warningMessage.setText(R.string.nicknametoolong);
                        }else {//save to database
                            nickname = change.getText().toString();
                            account_nickname.setText(nickname);
                            user.setNickname(change.getText().toString());
                            changeUserInfo();
                            AlertDialog.Builder builder = new AlertDialog.Builder(this);
                            builder.setMessage(R.string.nicknamechanged).setNegativeButton("OK"
                                    , (dialogInterface,i) -> dialogInterface.dismiss()).show();
                            bottomSheetDialog.dismiss();
                        }

                    }else {//nickname not changed
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setMessage(R.string.nicknamenochange).setNegativeButton("OK"
                                , (dialogInterface,i) -> dialogInterface.dismiss()).show();
                        bottomSheetDialog.dismiss();
                    }

                });
                break;
            case R.id.account_email_edit:
                hideEditPassword();
                bottomHeader.setText(R.string.Edit_email);
                change.setHint(email_);
                change.setText(email_);
                bottomSheetDialog.show();
                bottomSaveButton.setOnClickListener(view1 -> {
                    new Thread(() ->{
                        String reg = getString(R.string.reg);
                        if(!change.getText().toString().equals(email_)){//if email changed
                            Message message = handler.obtainMessage();
                            message.what = 0x14;
                            handler.sendMessage(message);

                            if(!change.getText().toString().matches(reg)||!change.getText().toString().endsWith(".ac.uk")
                                    ||!(change.getText().toString().length()<40)){//wrong email
                                message = handler.obtainMessage();
                                message.what = 0x12;
                                handler.sendMessage(message);
                            }else {//check and save to database
                                //check
                                user.setEmail(change.getText().toString());
                                UserDao userDao = new UserDao();
                                if(userDao.updateUser(user)==2){//email used
                                    user.setEmail(email_);//set email back
                                    message = handler.obtainMessage();
                                    message.what = 0x13;
                                    handler.sendMessage(message);

                                }else {//everything is ok. save the user to database and refresh
                                    email_ = change.getText().toString();
                                    message = handler.obtainMessage();
                                    message.what = 0x33;
                                    handler.sendMessage(message);
                                }
                            }
                        }else {//email not changed
                            Message message = handler.obtainMessage();
                            message.what = 0x44;
                            handler.sendMessage(message);

                        }
                    }).start();

                });

                break;
            case R.id._button_bottom_cancel:
                bottomSheetDialog.dismiss();
                break;
            case R.id.account_username_show:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.cannotchange).setNegativeButton("OK"
                , (dialogInterface,i) -> dialogInterface.dismiss()).show();
                break;
            case R.id.account_password_edit:
                showEditPassword();
                bottomHeader.setText(R.string.changepassword);
                change.setHint(R.string.enter_you_old_password_here);
                bottomSheetDialog.show();
                bottomSaveButton.setOnClickListener(view1 -> {
                    new Thread(()->{
                        initWarn();
                        old_password = change.getText().toString();
                        if(old_password.length()==0){
                            warningMessage.setText(R.string.enter_oldpassword);
                        }else {
                            //verify password
                            if (!old_password.equals(user.getPassword())){
                                //if password is wrong
                                warningMessage.setText(R.string.passwordwrong);
                            }else {
                                //password is right
                                //check new password and confirm
                                new_password = password.getText().toString();
                                password_confirm = confirm.getText().toString();
                                if (new_password.length()==0||new_password.length()>16){
                                    //empty password or too long
                                    password_w.setText(R.string.enterpasswaord);
                                } else if (new_password.equals(old_password)){
                                    //new password is as same as the old one
                                    password_w.setText(R.string.samepassword);

                                }else {//new password is different
                                    if (!new_password.equals(password_confirm)){
                                        //if confirmation failed
                                        confirm_w.setText(R.string.confirm_failed);
                                    }else {
                                        //confirmation succeed
                                        //save it to database
                                        user.setPassword(new_password);
                                        UserDao userDao = new UserDao();
                                        userDao.updateUser(user);
                                        Message message = handler.obtainMessage();
                                        message.what = 0x55;
                                        handler.sendMessage(message);
                                    }
                                }

                            }
                        }
                    }).start();
                });
                break;

            default:
                break;
        }

    }

    public void initWarn(){
        warningMessage.setText("");
        confirm_w.setText("");
        password_w.setText("");
    }


    //show the edit views of password
    public void showEditPassword(){
        change.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
        change.setText("");
        password.setText("");
        confirm.setText("");
        password.setVisibility(View.VISIBLE);
        password_w.setVisibility(View.VISIBLE);
        confirm.setVisibility(View.VISIBLE);
        confirm_w.setVisibility(View.VISIBLE);
    }

    //hide the edit views of password
    public void hideEditPassword(){
        password.setVisibility(View.GONE);
        password_w.setVisibility(View.GONE);
        confirm.setVisibility(View.GONE);
        confirm_w.setVisibility(View.GONE);
        change.setInputType(InputType.TYPE_CLASS_TEXT);
    }
    //tell user the email is not changed
    private void emailNotChanged() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.emailnochanged).setNegativeButton("OK"
                , (dialogInterface,i) -> dialogInterface.dismiss()).show();
        bottomSheetDialog.dismiss();
    }

    public void passwordChanged(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.passwordchanged).setNegativeButton("OK"
                , (dialogInterface,i) -> dialogInterface.dismiss()).show();
        bottomSheetDialog.dismiss();
    }

    private void changeUserInfo() {
        new Thread(() ->{
            UserDao userDao = new UserDao();
            userDao.updateUser(user);

        }).start();
    }

    //show the particular user's info
    public void showUserInfo(){
        new Thread(() -> {
            UserDao userDao = new UserDao();
            user = userDao.getUserByUsername(username_);
            customer = userDao.getUserByUsername("root");

            Message message = handler.obtainMessage();
            if(user != null){
                message.what = 0x11;
                nickname = user.getNickname();
            }else {
                message.what = 0x22;
                nickname = customer.getNickname();
            }
            handler.sendMessage(message);
        }).start();
    }
}