package com.example.mywork2.LogInFragment;

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

import com.example.mywork2.R;

import org.w3c.dom.Text;


public class NewAccountFragment extends Fragment {
    private EditText username;
    private EditText email;
    private EditText password;
    private EditText confirmPassword;
    private Button register;

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

        email = view.findViewById(R.id.email_registry);

        password = view.findViewById(R.id.password_registry);

        confirmPassword = view.findViewById(R.id.confirm_password_registry);

        register = view.findViewById(R.id.btn_regstery);



    }
}