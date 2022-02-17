package com.reaver.greenkras.ui.user;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.reaver.greenkras.async.AsyncAuthorization;
import com.reaver.greenkras.R;


public class Registration extends AppCompatActivity {
    private EditText ET_login, ET_password, ET_password2, ET_email;
    private ImageView visible;
    private int visib = 0;
    private String reg_login;
    private String reg_password;
    private String email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);
        ET_login =  findViewById(R.id.Login);
        ET_password = findViewById(R.id.Password);
        ET_password2 =  findViewById(R.id.Password2);
        ET_email =  findViewById(R.id.Email);
        visible =  findViewById(R.id.visibleOn);

        visible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (visib == 0) {
                    visible.setImageResource(R.drawable.ic_visibility_off);
                    ET_password.setTransformationMethod(null);
                    ET_password.setSelection(ET_password.length());
                    ET_password2.setTransformationMethod(null);
                    ET_password2.setSelection(ET_password2.length());
                    visib=1;
                }
                else {
                    visible.setImageResource(R.drawable.ic_visibility);
                    ET_password.setTransformationMethod(new PasswordTransformationMethod());
                    ET_password.setSelection(ET_password.length());
                    ET_password2.setTransformationMethod(new PasswordTransformationMethod());
                    ET_password2.setSelection(ET_password2.length());
                    visib=0;
                }
            }
        });

        ET_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ET_password.setTextColor(Color.BLACK);
                ET_password2.setTextColor(Color.BLACK);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        ET_password2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ET_password.setTextColor(Color.BLACK);
                ET_password2.setTextColor(Color.BLACK);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    public void goRegistration(View view)
    {
        reg_login = ET_login.getText().toString();
        reg_password = ET_password.getText().toString();
        String reg_password2 = ET_password2.getText().toString();
        email = ET_email.getText().toString();
        if (CheckNetwork()){
        if(TextUtils.isEmpty(reg_login) || TextUtils.isEmpty(reg_password)|| TextUtils.isEmpty(reg_password2)
                || TextUtils.isEmpty(email)){
            Toast.makeText(Registration.this, "Заполнены не все поля", Toast.LENGTH_LONG).show();
        }else {
            if (reg_login.length() < 5){
                Toast.makeText(getBaseContext(),"Логин должен содержать больше 5и символов",Toast.LENGTH_LONG).show();
            } else if (!reg_password.equals(reg_password2)) {
                ET_password.setTextColor(Color.RED);
                ET_password2.setTextColor(Color.RED);
                Toast.makeText(getBaseContext(),"Пароли должны совпадать",Toast.LENGTH_LONG).show();
            }
            else if (reg_password.length() < 6) {
                Toast.makeText(getBaseContext(),"Пароль должен содержать больше 6и символов",Toast.LENGTH_LONG).show();
            } else {

                Send();
            }

        }
        }else{
            Toast.makeText(getApplicationContext(),"Проверьте интернет соединение",Toast.LENGTH_LONG).show();
        }
    }
    private boolean CheckNetwork() {
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert conMgr != null;
        return conMgr.getActiveNetworkInfo() != null
                && conMgr.getActiveNetworkInfo().isAvailable()
                && conMgr.getActiveNetworkInfo().isConnected();

    }

    @Override
    public void onBackPressed()
    {
        Intent aut = new Intent(Registration.this, Authorization.class);
        startActivity(aut);
        finish();
    }

    public void Send(){
        String method ="Registration";
        AsyncAuthorization asyncAuthorization = new AsyncAuthorization(this);
        asyncAuthorization.execute(method, reg_login, reg_password, email);
    }

}

