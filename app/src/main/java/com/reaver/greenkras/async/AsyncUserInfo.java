package com.reaver.greenkras.async;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import com.reaver.greenkras.DBHelper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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

public class AsyncUserInfo extends AsyncTask<String, Void, StringBuffer> {
    @SuppressLint("StaticFieldLeak")
    private Context ctx;
    private String Saved_Login;
    private DBHelper dbHelper;

    public AsyncUserInfo(Context ctx){
        this.ctx=ctx;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected StringBuffer doInBackground(String... params) {
        String upload_user= "https://greenkras.ru/users.php";
        String key = "Request_a9Xi7EF&%gAMkPkFWVpxIe3Bvx%%t*Z0uXU69#0YbbsUwjo@n$";
        String method = params[0];
        if (method.equals("UploadUser")){
            SharedPreferences pref = ctx.getSharedPreferences("login", Context.MODE_PRIVATE);
            String login = pref.getString(Saved_Login, "");
            try {
                URL url = new URL (upload_user);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, StandardCharsets.UTF_8));
                String data = URLEncoder.encode("key","UTF-8") +"="+ URLEncoder.encode(key,"UTF-8") +"&"+
                        URLEncoder.encode("login","UTF-8") +"="+ URLEncoder.encode(login,"UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();
                InputStream IS = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(IS, StandardCharsets.UTF_8));
                String line;
                StringBuffer buffer = new StringBuffer();
                while ((line = bufferedReader.readLine())!=null)
                {
                    buffer.append(line);
                }
                bufferedReader.close();
                IS.close();
                httpURLConnection.disconnect();
                return buffer;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }




    @Override
    protected void onPostExecute(StringBuffer result) {
        if (result == null) {
            Log.e("UserInfo", "NULL");
        } else {
            try {
                dbHelper = new DBHelper(ctx);
                SQLiteDatabase database = dbHelper.getWritableDatabase();
                String finalJson = result.toString();
                JSONObject parentObject = new JSONObject(finalJson);
                JSONArray parentArray = parentObject.getJSONArray("users");
                    database.delete(DBHelper.TABLE_CONTACTS6, null, null);
                    for (int i = 0; i < parentArray.length(); i++) {
                        JSONObject finalObject = parentArray.getJSONObject(i);
                        String login  = finalObject.getString("login");
                        String id_tit  = finalObject.getString("id_tit");
                        String avatar  = finalObject.getString("avatar");
                        String lvl = finalObject.getString("lvl");
                        String proc  = finalObject.getString("proc");
                        String ostobj  = finalObject.getString("ostobj");
                        String schetder = finalObject.getString("schetder");
                        String schetkust = finalObject.getString("schetkust");
                        String schetparter = finalObject.getString("schetparter");
                        String schetparks = finalObject.getString("schetparks");
                        String allobj = finalObject.getString("allobj");
                        String allvoters = finalObject.getString("allvoters");
                        String warn = finalObject.getString("warn");
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(DBHelper.KEY_LOGIN6, login);
                        contentValues.put(DBHelper.KEY_TITUL6, id_tit);
                        contentValues.put(DBHelper.KEY_AVATAR6, avatar);
                        contentValues.put(DBHelper.KEY_LVL6, lvl);
                        contentValues.put(DBHelper.KEY_PROC6, proc);
                        contentValues.put(DBHelper.KEY_OST6, ostobj);
                        contentValues.put(DBHelper.KEY_SCHETDER6, schetder);
                        contentValues.put(DBHelper.KEY_SCHETKUST6, schetkust);
                        contentValues.put(DBHelper.KEY_SCHETPART6, schetparter);
                        contentValues.put(DBHelper.KEY_SCHETPARK6, schetparks);
                        contentValues.put(DBHelper.KEY_ALLOBJ6, allobj);
                        contentValues.put(DBHelper.KEY_ALLVOTERS6, allvoters);
                        contentValues.put(DBHelper.KEY_WARN6, warn);
                        database.insert(DBHelper.TABLE_CONTACTS6, null, contentValues);
                        database.close();

                    }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            dbHelper.close();
        }
    }
}
