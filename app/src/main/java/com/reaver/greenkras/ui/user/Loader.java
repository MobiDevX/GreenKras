package com.reaver.greenkras.ui.user;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.reaver.greenkras.R;
import com.reaver.greenkras.async.AsyncAuthorization;


public class Loader extends AppCompatActivity{
    private String Autologin, Enter, Saved_Login, Saved_Login_f, Saved_password, Saved_Password_f;
    private TextView tvPreload;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preloader);
        tvPreload = findViewById(R.id.preload);
        SharedPreferences sPref = getSharedPreferences("Autologin", Context.MODE_PRIVATE);
        Enter = sPref.getString(Autologin, "False");
        sPref = getSharedPreferences("login", Context.MODE_PRIVATE);
        Saved_Login_f = sPref.getString(Saved_Login, "");
        sPref = getSharedPreferences("password", Context.MODE_PRIVATE);
        Saved_Password_f = sPref.getString(Saved_password, "null");

        if (Enter.equals("True")) {
            if (CheckNetwork()){
                Send();
            }
            else {
                final Handler h = new Handler();
                Runnable run = new Runnable() {

                    @Override
                    public void run() {
                        if (CheckNetwork()){
                            tvPreload.setText("Подождите минутку...");
                            Send();
                            h.removeCallbacks(this);
                        }
                        else {
                            h.postDelayed(this, 2000);
                        }

                    }
                };
                run.run();
                tvPreload.setText("Проверьте интернет соединение");
                Toast.makeText(getApplicationContext(),"Проверьте интернет соединение",Toast.LENGTH_LONG).show();
            }
        }
        else if (Saved_Password_f.equals("null")){
            Intent main = new Intent(Loader.this, Authorization.class);
            startActivity(main);
        }
        else {
            Intent main = new Intent(Loader.this, Authorization.class);
            startActivity(main);
        }


    }
    public void Send(){
        String method ="Autorization";
        AsyncAuthorization asyncAuthorization = new AsyncAuthorization(this);
        asyncAuthorization.execute(method, Saved_Login_f, Saved_Password_f, Enter);
    }

    private boolean CheckNetwork() {
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert conMgr != null;
        return conMgr.getActiveNetworkInfo() != null
                && conMgr.getActiveNetworkInfo().isAvailable()
                && conMgr.getActiveNetworkInfo().isConnected();

    }
}
