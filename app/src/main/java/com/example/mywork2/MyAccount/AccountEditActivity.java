package com.example.mywork2.MyAccount;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mywork2.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class AccountEditActivity extends AppCompatActivity implements View.OnClickListener{
    private BottomSheetDialog bottomSheetDialog;
    private View bottomView;
    private ConstraintLayout username,email;
    private TextView bottomHeader,warningMessage;
    private EditText change;
    private Button bottomSaveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_edit);

        //set action bar
        TextView title = findViewById(R.id.header_title);
        title.setText("Edit My Account");
        findViewById(R.id.back_button).setOnClickListener(view -> {
            this.finish()
            ;});

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

        username = findViewById(R.id.account_username_edit);
        username.setOnClickListener(this);

        email = findViewById(R.id.account_email_edit);
        email.setOnClickListener(this);





    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.account_username_edit:
                bottomHeader.setText("new username");
                change.setHint("your new username");
                bottomSheetDialog.show();
                break;
            case R.id.account_email_edit:
                bottomHeader.setText("new Email address");
                change.setHint("your new Email address");
                bottomSheetDialog.show();
                break;
            case R.id.button_bottom_save:
                bottomSheetDialog.dismiss();
                break;
            default:
                break;
        }

    }
}