package com.example.mywork2.LogInFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.example.mywork2.R;
/**
 * @author Jiacheng
 * function: the activity to use the google Twitter facebook login (has been abandoned)
 * modification date and description can be found in github repository history
 */


public class GoogleTwitterFacebookLogIn extends Fragment {
    static private String types = "";

    public GoogleTwitterFacebookLogIn() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_google_twitter_facebook_log_in, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        WebView content = view.findViewById(R.id.alternative_log_in);

        switch (types){
            case "facebook":
                content.loadUrl("https://www.facebook.com/login.php?next=https%3A%2F%2Fwww.facebook.com%2Fprivacy%2Fconsent%2Fuser_cookie_choice%2F%3Fsource%3Dpft_user_cookie_choice");
                break;
            case "google":
                content.loadUrl("https://www.google.co.uk/");
                break;
            case "twitter":
                content.loadUrl("https://twitter.com/home");
                break;
        }
    }

    public static void setTypes(String types) {
        GoogleTwitterFacebookLogIn.types = types;
    }

}