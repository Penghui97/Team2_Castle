package com.example.mywork2.MyAccount;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.mywork2.LogInActivity;
import com.example.mywork2.LogInFragment.LogInMainFragment;
import com.example.mywork2.MainActivity;
import com.example.mywork2.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Locale;

public class AppSettingsActivity extends AppCompatActivity implements View.OnClickListener{
    private BottomSheetDialog bottomSheetDialog;
    private TextView language,setting_warn, setting_cn;
    private Button refresh;


    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_settings);

        findViewById(R.id.back_button).setOnClickListener(view -> {
            this.finish()
            ;});

        language = findViewById(R.id.settings_language);
        RadioGroup rgLanguage = findViewById(R.id.rg_language);
        refresh = findViewById(R.id.setting_refresh);
        setting_warn = findViewById(R.id.setting_warn);
        setting_cn = findViewById(R.id.setting_warn_cn);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String username = (String) extras.get("username");

        Intent intent1 = new Intent(AppSettingsActivity.this,MainActivity.class);
        intent1.putExtra("username", username);



        //set listener on radio group
        rgLanguage.setOnCheckedChangeListener(this::onCheckedChanged);
        refresh.setOnClickListener(view -> {
            startActivity(intent1);
        });
        


    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setLocale(String lang) {
        //Initialize resources
        Resources resources = getResources();
        //Initialize metrics
        DisplayMetrics metrics = resources.getDisplayMetrics();
        //initialize configurations
        Configuration configuration = resources.getConfiguration();
        //initialize locale
        configuration.locale = new Locale(lang);
        //update configuration
        resources.updateConfiguration(configuration,metrics);
        onConfigurationChanged(configuration);

    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //set strings from resources
        language.setText(R.string.language);
        refresh.setText(R.string.refresh);
        setting_warn.setText(R.string.update_language);
        setting_cn.setText(R.string.language_warn);
    }

    @Override
    public void onClick(View view) {

    }

    @SuppressLint("NonConstantResourceId")
    private void onCheckedChanged(RadioGroup radioGroup, int i) {
        //check condition
        switch (i) {
            case R.id.rb_english:
                String lang = "en";
                //set locale
                setLocale(lang);
                break;
            case R.id.rb_Chinese:
                setLocale("zh");
                break;
        }

    }
}