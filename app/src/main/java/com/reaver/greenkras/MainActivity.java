package com.reaver.greenkras;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.reaver.greenkras.async.AsyncName;
import com.reaver.greenkras.async.AsyncUpdatePark;
import com.reaver.greenkras.async.AsyncUserInfo;
import com.reaver.greenkras.ui.user.Authorization;
import com.squareup.picasso.Picasso;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.widget.ProgressBar;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sPref, fPref, themePref, uploadParkPref;
    String Saved_Login, login, Autologin, theme, Saved_theme, uploadPark, Saved_park;
    Context context;
    DBHelper dbHelper;
    CircleImageView photoUser;
    TextView tvTitul,tvLvl, tvNextLvl;
    ProgressBar progress;
    SQLiteDatabase database;

    String titul;
    String avatar;
    String lvl;
    String proc;
    String ostobj;
    String pushEnable, Saved_push;

    private AppBarConfiguration mAppBarConfiguration;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        themePref = getSharedPreferences("Theme", Context.MODE_PRIVATE);
        theme = themePref.getString(Saved_theme, "False");
        super.onCreate(savedInstanceState);
        if (theme.equals("True")){
            setTheme(R.style.DarkTheme);
        } else
            setTheme(R.style.LightTheme);

        dbHelper = new DBHelper(getApplicationContext());

        SharedPreferences push = getSharedPreferences("Push",Context.MODE_PRIVATE);
        pushEnable = push.getString(Saved_push, "True");
        FirebaseMessaging.getInstance().subscribeToTopic("APP");
        if (pushEnable.equals("True")) {
            FirebaseMessaging.getInstance().subscribeToTopic("APP");
        } else
            FirebaseMessaging.getInstance().unsubscribeFromTopic("APP");


        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setPopupTheme(R.style.AppTheme_PopupOverlay);
        setSupportActionBar(toolbar);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        photoUser = headerView.findViewById(R.id.avatar);
        tvTitul = headerView.findViewById(R.id.tvTitul);
        TextView Login = headerView.findViewById(R.id.login);
        tvLvl = headerView.findViewById(R.id.tvLvl);
        tvNextLvl = headerView.findViewById(R.id.tvNextLvl);
        progress = headerView.findViewById(R.id.progressLvl);
        sPref = getSharedPreferences("login", Context.MODE_PRIVATE);
        login = sPref.getString(Saved_Login, "");
        Login.setText(login);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_account, R.id.nav_tree, R.id.nav_scrub, R.id.nav_parterres, R.id.nav_poligon, R.id.nav_vote,
                R.id.nav_map, R.id.nav_exit, R.id.nav_tp, R.id.nav_site)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        database = dbHelper.getWritableDatabase();
        database.delete(DBHelper.TABLE_CONTACTS5, null, null);
        database.delete(DBHelper.TABLE_CONTACTS7, null, null);
        database.delete(DBHelper.TABLE_CONTACTS8, null, null);
        database.delete(DBHelper.TABLE_CONTACTS9, null, null);
        AsyncUpdatePark asyncUpdate = new AsyncUpdatePark(getApplicationContext());
        asyncUpdate.execute();
        context = this;
        AsyncName asyncName = new AsyncName(getApplicationContext());
        asyncName.execute();
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller,
                                             @NonNull NavDestination destination, @Nullable Bundle arguments) {
                uploadParkPref = getSharedPreferences("UploadPark", Context.MODE_PRIVATE);
                uploadPark = uploadParkPref.getString(Saved_park, "");
                if(destination.getId() == R.id.nav_exit) {
                    SQLiteDatabase database = dbHelper.getWritableDatabase();
                    database.delete(DBHelper.TABLE_CONTACTS6, null, null);
                    database.close();
                    sPref = getSharedPreferences("Autologin", MODE_PRIVATE);
                    SharedPreferences.Editor ed = sPref.edit();
                    ed.remove(Autologin).apply();
                    fPref = getSharedPreferences("login", MODE_PRIVATE);
                    SharedPreferences.Editor edf = sPref.edit();
                    edf.remove(Saved_Login).apply();
                    Intent Start = new Intent(MainActivity.this, Authorization.class);
                    startActivity(Start);
                    finish();

                } else if (destination.getId() == R.id.nav_tp) {
                    Intent selectorIntent = new Intent(Intent.ACTION_SENDTO);
                    selectorIntent.setData(Uri.parse("mailto:"));
                    final Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"iitk.answer@gmail.com"});

                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Обращение в техподдержку от пользователя: " + login);
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "");
                    emailIntent.setSelector( selectorIntent );

                    startActivity(Intent.createChooser(emailIntent, "Выберите почтовый клиент..."));

                } else if (destination.getId() == R.id.nav_site) {
                String url = "https://www.greenkras.ru";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                }  if (uploadPark.equals("True")) {
                    if (destination.getId() == R.id.nav_home) {
                        AsyncUpdatePark asyncUpdate = new AsyncUpdatePark(getApplicationContext());
                        asyncUpdate.execute();
                        UpdateUser(2000);

                    } else if (destination.getId() == R.id.nav_account) {
                        AsyncUpdatePark asyncUpdate = new AsyncUpdatePark(getApplicationContext());
                        asyncUpdate.execute();
                        UpdateUser(2000);

                    } else if (destination.getId() == R.id.nav_tree) {
                        AsyncUpdatePark asyncUpdate = new AsyncUpdatePark(getApplicationContext());
                        asyncUpdate.execute();

                    } else if (destination.getId() == R.id.nav_scrub) {
                        AsyncUpdatePark asyncUpdate = new AsyncUpdatePark(getApplicationContext());
                        asyncUpdate.execute();

                    } else if (destination.getId() == R.id.nav_parterres) {
                        AsyncUpdatePark asyncUpdate = new AsyncUpdatePark(getApplicationContext());
                        asyncUpdate.execute();

                    } else if (destination.getId() == R.id.nav_map) {
                        AsyncUpdatePark asyncUpdate = new AsyncUpdatePark(getApplicationContext());
                        asyncUpdate.execute();
                    }
                }

            }
        });

        if(Build.VERSION.SDK_INT >= 23){
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION}, 2);

        }

        final Handler h = new Handler();
        Runnable run = new Runnable() {

            @Override
            public void run() {

                UpdateUser(0);
                h.postDelayed(this, 10000);

            }
        };
        run.run();
    }

    public void UpdateUser(Integer time){
        String method = "UploadUser";
        AsyncUserInfo asyncUserInfo = new AsyncUserInfo(getApplicationContext());
        asyncUserInfo.execute(method);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @SuppressLint("SetTextI18n")
            public void run() {
                SQLiteDatabase database = dbHelper.getWritableDatabase();
                Cursor cursor = database.query(DBHelper.TABLE_CONTACTS6, null, null, null, null, null, null);
                if (cursor.moveToFirst()) {
                    int id_titC = cursor.getColumnIndex(DBHelper.KEY_TITUL6);
                    int avatarC = cursor.getColumnIndex(DBHelper.KEY_AVATAR6);
                    int lvlC = cursor.getColumnIndex(DBHelper.KEY_LVL6);
                    int procC = cursor.getColumnIndex(DBHelper.KEY_PROC6);
                    int ostobjC = cursor.getColumnIndex(DBHelper.KEY_OST6);
                    do {
                        titul = cursor.getString(id_titC);
                        avatar = cursor.getString(avatarC);
                        lvl = cursor.getString(lvlC);
                        proc = cursor.getString(procC);
                        int progresslvl = Integer.parseInt(proc.trim());
                        ostobj = cursor.getString(ostobjC);
                        tvNextLvl.setText("До следующего уровня: "+ostobj);
                        tvLvl.setText(lvl + " уровень");
                        progress.setProgress(progresslvl);
                        tvTitul.setText(titul);
                        Picasso.get()
                                .load(avatar)
                                .error(R.drawable.shinobu)
                                .into(photoUser);


                    } while (cursor.moveToNext());
                    Log.e("Msg", avatar);
                }
                cursor.close();
                database.close();
            }
        }, time); //specify the number of milliseconds

    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
