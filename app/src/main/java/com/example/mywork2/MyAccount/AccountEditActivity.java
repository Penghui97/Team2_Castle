package com.example.mywork2.MyAccount;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
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

public class AccountEditActivity extends AppCompatActivity implements View.OnClickListener{
    private BottomSheetDialog bottomSheetDialog;
    private View bottomView;
    private ConstraintLayout username,email;
    private TextView bottomHeader,warningMessage, account_nickname, account_email;
    private EditText change;
    private Button bottomSaveButton;
    private User user, customer;
    private String username_, email_, nickname;

    //receive the data from the database
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 0x11) {
                account_nickname.setText(nickname);
                account_email.setText(email_);
            }else if (msg.what == 0x22) {
                account_nickname.setText(customer.getNickname());
                account_email.setText(customer.getEmail());
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




        //set action bar
        TextView title = findViewById(R.id.header_title);
        title.setText(R.string.editMyaccount);
        findViewById(R.id.back_button).setOnClickListener(view -> {
            this.finish()
            ;});

        account_nickname = findViewById(R.id.account_nick_name);
        account_email = findViewById(R.id.account_email_address);

//        account_nickname.setText(nickname);
//        account_email.setText(email_);


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
        bottomSaveButton.setOnClickListener(this);


        bottomSheetDialog.setContentView(bottomView);

        username = findViewById(R.id.account_nickname_edit);
        username.setOnClickListener(this);

        email = findViewById(R.id.account_email_edit);
        email.setOnClickListener(this);

        showUserInfo();




    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.account_nickname_edit:
                bottomHeader.setText(R.string.edit_name);
                change.setHint(nickname);
                bottomSheetDialog.show();
                break;
            case R.id.account_email_edit:
                bottomHeader.setText(R.string.Edit_email);
                change.setHint(email_);
                bottomSheetDialog.show();
                break;
            case R.id.button_bottom_save:
                bottomSheetDialog.dismiss();
                break;
            default:
                break;
        }

    }

    //show the particular user's info
    public void showUserInfo(){
        new Thread(() -> {
            UserDao userDao = new UserDao();
            user = userDao.getUserByUsername(username_);
            customer = userDao.getUserByUsername("root");

            if(user != null){
                Message message = handler.obtainMessage();
                message.what = 0x11;
                nickname = user.getNickname();
                handler.sendMessage(message);
            }else {
                Message message = handler.obtainMessage();
                message.what = 0x22;
                nickname = customer.getNickname();
                handler.sendMessage(message);
            }
        }).start();
    }
}