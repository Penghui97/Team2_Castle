package com.example.mywork2.LogInFragment;

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
import android.widget.TextView;

import com.example.mywork2.LogInActivity;
import com.example.mywork2.MainActivity;
import com.example.mywork2.R;

public class LogInMainFragment extends Fragment implements View.OnClickListener{
    NavController navController;

    public LogInMainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_log_in_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        Button usernameSignIn = view.findViewById(R.id.sign_in_with_username);
        usernameSignIn.setOnClickListener(this);

        TextView customerLogin = view.findViewById(R.id.customer_login);
        customerLogin.setOnClickListener(this);

        TextView newAccount = view.findViewById(R.id.create_account);
        newAccount.setOnClickListener(this);

        TextView facebook = view.findViewById(R.id.facebook_sign_in);
        facebook.setOnClickListener(this);

        TextView google = view.findViewById(R.id.google_sign_in);
        google.setOnClickListener(this);

        TextView twitter = view.findViewById(R.id.twitter_sign_in);
        twitter.setOnClickListener(this);



    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.sign_in_with_username:
                navController.navigate(R.id.action_logInMainFragment_to_userLogInFragment);
                break;
            case R.id.customer_login:
                navController.navigate(R.id.action_logInMainFragment_to_mainActivity);
                break;
            case R.id.create_account:
                navController.navigate(R.id.action_logInMainFragment_to_newAccountFragment);
                break;
            case R.id.facebook_sign_in:
                navController.navigate(R.id.action_logInMainFragment_to_googleTwitterFacebookLogIn);
                GoogleTwitterFacebookLogIn.setTypes("facebook");
                break;
            case R.id.google_sign_in:
                navController.navigate(R.id.action_logInMainFragment_to_googleTwitterFacebookLogIn);
                GoogleTwitterFacebookLogIn.setTypes("google");
                break;
            case R.id.twitter_sign_in:
                navController.navigate(R.id.action_logInMainFragment_to_googleTwitterFacebookLogIn);
                GoogleTwitterFacebookLogIn.setTypes("twitter");
                break;
            default:
                break;

        }
    }
}