package com.reaver.greenkras.async;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.reaver.greenkras.MainActivity;
import com.reaver.greenkras.ui.user.Authorization;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class AsyncAuthorization extends AsyncTask<String,Void,String> {
    @SuppressLint("StaticFieldLeak")
    private Context ctx;
    private String Success;
    private String Login;
    private String Saved_Login;
    private String FAutologin;
    private String Fpassword;
    private String Saved_password;
    private String token, tokenHere;

    public AsyncAuthorization(Context ctx){
        this.ctx=ctx;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                token = instanceIdResult.getToken();
                SharedPreferences sharedPreferences = ctx.getSharedPreferences("token", Context.MODE_PRIVATE);
                SharedPreferences.Editor ed = sharedPreferences.edit();
                ed.putString(tokenHere, token).apply();
                ed.commit();
            }
        });

    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

        @Override
        protected String doInBackground(String... params) {

            String send_login= "https://greenkras.ru/login.php";
            String send_registration= "https://greenkras.ru/register.php";
            String send_restore = "https://greenkras.ru/respas.php";
            String key = "Request_a9Xi7EF&%gAMkPkFWVpxIe3Bvx%%t*Z0uXU69#0YbbsUwjo@n$";
            String method = params[0];
            if (method.equals("Autorization")){
                String login = params[1];
                String password = params[2];
                FAutologin = params[3];
                Fpassword = password;
                Login = login;
                SharedPreferences sharedPreferences = ctx.getSharedPreferences("token", Context.MODE_PRIVATE);
                String Newtoken = sharedPreferences.getString(token, "");
                Log.e("ТОкен в памяти: ",Newtoken);
                try {
                    URL url = new URL (send_login);
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream OS = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, StandardCharsets.UTF_8));
                    String data = URLEncoder.encode("login","UTF-8") +"="+ URLEncoder.encode(login,"UTF-8") +"&"+
                            URLEncoder.encode("password","UTF-8") +"="+ URLEncoder.encode(password,"UTF-8") +"&"+
                            URLEncoder.encode("token","UTF-8") +"="+ URLEncoder.encode(Newtoken,"UTF-8") +"&"+
                            URLEncoder.encode("key","UTF-8") +"="+ URLEncoder.encode(key,"UTF-8");
                    bufferedWriter.write(data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    OS.close();
                    InputStream IS = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(IS, StandardCharsets.UTF_8));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine())!=null)
                    {
                        response.append(line);
                    }
                    bufferedReader.close();
                    IS.close();
                    httpURLConnection.disconnect();
                    return response.toString();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        if (method.equals("Restore")){
            String email = params[1];
            try {
                URL url = new URL (send_restore);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, StandardCharsets.UTF_8));
                String data = URLEncoder.encode("mail","UTF-8") +"="+ URLEncoder.encode(email,"UTF-8") +"&"+
                        URLEncoder.encode("key","UTF-8") +"="+ URLEncoder.encode(key,"UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();
                InputStream IS = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(IS, StandardCharsets.UTF_8));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine())!=null)
                {
                    response.append(line);
                }
                bufferedReader.close();
                IS.close();
                httpURLConnection.disconnect();
                return response.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (method.equals("Registration")){
            String reg_login = params[1];
            String reg_password = params[2];
            String email = params[3];
            try {
                URL url = new URL (send_registration);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, StandardCharsets.UTF_8));
                String data = URLEncoder.encode("login","UTF-8") +"="+ URLEncoder.encode(reg_login,"UTF-8") +"&"+
                        URLEncoder.encode("password","UTF-8") +"="+ URLEncoder.encode(reg_password,"UTF-8") +"&"+
                        URLEncoder.encode("mail","UTF-8") +"="+ URLEncoder.encode(email,"UTF-8") +"&"+
                        URLEncoder.encode("key","UTF-8") +"="+ URLEncoder.encode(key,"UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();
                InputStream IS = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(IS, StandardCharsets.UTF_8));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine())!=null)
                {
                    response.append(line);
                }
                bufferedReader.close();
                IS.close();
                httpURLConnection.disconnect();
                return response.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
        protected void onPostExecute(String result) {
        if (result == null){
            Toast.makeText(ctx, "Повторите попытку через несколько секунд", Toast.LENGTH_LONG).show();
        }
        else if (result.equals("Успешный вход ")) {
            if (FAutologin.equals("True")){
                SharedPreferences sharedPreferences = ctx.getSharedPreferences("Autologin", Context.MODE_PRIVATE);
                SharedPreferences.Editor ed = sharedPreferences.edit();
                ed.putString(Success, "True").apply();
                ed.apply();
                SharedPreferences sharedPreferences1 = ctx.getSharedPreferences("password", Context.MODE_PRIVATE);
                SharedPreferences.Editor ed1 = sharedPreferences1.edit();
                ed1.putString(Saved_password, Fpassword).apply();
                ed1.apply();
            }
            else {
                SharedPreferences sharedPreferences = ctx.getSharedPreferences("Autologin", Context.MODE_PRIVATE);
                SharedPreferences.Editor ed = sharedPreferences.edit();
                ed.putString(Success, "False").apply();
                ed.apply();
            }
            SharedPreferences sharedPreferences2 = ctx.getSharedPreferences("login", Context.MODE_PRIVATE);
            SharedPreferences.Editor ed2 = sharedPreferences2.edit();
            ed2.putString(Saved_Login, Login).apply();
            ed2.commit();
            Intent intent = new Intent(ctx, MainActivity.class);
            ctx.startActivity(intent);
            ((Activity)ctx).finish();
        }

        else if (result.equals("Вы успешно зарегистрированы!")) {
            Toast.makeText(ctx,result,Toast.LENGTH_LONG).show();
            Intent intent = new Intent(ctx, Authorization.class);
            ctx.startActivity(intent);
            ((Activity)ctx).finish();
        }

        else if (result.equals("Письмо активации выслано вам на почту.")) {
            Toast.makeText(ctx,result,Toast.LENGTH_LONG).show();
            Intent intent = new Intent(ctx, Authorization.class);
            ctx.startActivity(intent);
            ((Activity)ctx).finish();
        }
        else {
            Toast.makeText(ctx,result,Toast.LENGTH_LONG).show();
            Intent intent = new Intent(ctx, Authorization.class);
            ctx.startActivity(intent);
            ((Activity)ctx).finish();

        }
    }

}
