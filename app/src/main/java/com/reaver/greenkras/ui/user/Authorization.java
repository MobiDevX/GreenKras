package com.reaver.greenkras.ui.user;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import com.reaver.greenkras.DBHelper;
import com.reaver.greenkras.async.AsyncAuthorization;
import com.reaver.greenkras.R;


public class Authorization extends AppCompatActivity {

    private EditText ET_login, ET_password;
    private CheckBox Remember;
    private ImageView visible;
    private int visib = 0;
    private DialogFragment dialog;
    private String login, password, Autologin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.autorization);
        ET_login = findViewById(R.id.Login);
        ET_password =  findViewById(R.id.Password);
        Remember =  findViewById(R.id.remember);
        visible =  findViewById(R.id.visibleOn);
        TextView TV_restore = findViewById(R.id.TV_restore);
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        database.delete(DBHelper.TABLE_CONTACTS6, null, null);


        TV_restore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new RestorePassword();
                dialog.show(getSupportFragmentManager(), "Восстановление пароля");
            }
        });

        visible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (visib == 0) {
                    visible.setImageResource(R.drawable.ic_visibility_off);
                    ET_password.setTransformationMethod(null);
                    ET_password.setSelection(ET_password.length());
                    visib=1;
                }
                else {
                    visible.setImageResource(R.drawable.ic_visibility);
                    ET_password.setTransformationMethod(new PasswordTransformationMethod());
                    ET_password.setSelection(ET_password.length());
                    visib=0;
                }
            }
        });
    }
    private static long back_pressed;
    @Override
    public void onBackPressed() {
        if (back_pressed + 2000 > System.currentTimeMillis()) {
            finish();
        } else {
            Toast.makeText(getBaseContext(), "Нажмите ещё раз чтобы закрыть приложение!", Toast.LENGTH_SHORT).show();
        }
        back_pressed = System.currentTimeMillis();
    }

    public void onAutorization(View view)
    {
        if (CheckNetwork()){
            if (Remember.isChecked()) {
                Autologin = "True";
            }
            else {
                Autologin = "False";
            }
            login = ET_login.getText().toString();
            password = ET_password.getText().toString();

            if(TextUtils.isEmpty(login) || TextUtils.isEmpty(password)){
                Toast.makeText(getApplicationContext(), "Заполнены не все поля", Toast.LENGTH_LONG).show();
            }else
                Send(view);
        }
        else {
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

    public void onRegistration(View view)
    {
        Intent reg = new Intent(Authorization.this, Registration.class);
        startActivity(reg);
    }

    public void Send(View view){
        String method ="Autorization";
        AsyncAuthorization asyncAuthorization = new AsyncAuthorization(this);
        asyncAuthorization.execute(method, login, password, Autologin);
    }


}
