package com.reaver.greenkras.async;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import com.reaver.greenkras.DBHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


import static java.nio.charset.StandardCharsets.UTF_8;

public class AsyncName extends AsyncTask<String,Integer,String> {
    @SuppressLint("StaticFieldLeak")
    Context ctx;
    public AsyncName(Context ctx){
        this.ctx=ctx;
    }

    DBHelper dbHelper;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected String doInBackground(String... params) {
        String download= "https://greenkras.ru/jsonlist/scrublist.php";
        HttpURLConnection connection;
        BufferedReader reader;
        dbHelper = new DBHelper(ctx);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        try {
            URL url = new URL (download);
            connection  = (HttpURLConnection)url.openConnection();
            connection.connect();
            InputStream stream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream, UTF_8));
            StringBuilder buffer = new StringBuilder();
            String line;
            while ((line = reader.readLine())!=null) {
                buffer.append(line);
            }
            String finalJson = buffer.toString();
            JSONObject parentObject = new JSONObject(finalJson);
            JSONArray parentArray = parentObject.getJSONArray("scrublist");
            JSONObject parentObject2 = new JSONObject(finalJson);
            JSONArray parentArray2 = parentObject2.getJSONArray("treelist");
            JSONObject parentObject3 = new JSONObject(finalJson);
            JSONArray parentArray3 = parentObject3.getJSONArray("parterreslist");
            for(int i=0; i<parentArray.length(); i++) {
                JSONObject finalObject = parentArray.getJSONObject(i);
                String nameScrub  = finalObject.getString("scrub");
                ContentValues contentValues = new ContentValues();
                contentValues.put(DBHelper.KEY_NAMESCRUB7, nameScrub);
                database.insert(DBHelper.TABLE_CONTACTS7, null, contentValues);
            }
            for(int i=0; i<parentArray2.length(); i++) {
                JSONObject finalObject2 = parentArray2.getJSONObject(i);
                String nameTree  = finalObject2.getString("tree");
                ContentValues contentValues2 = new ContentValues();
                contentValues2.put(DBHelper.KEY_NAMETREE8, nameTree);
                database.insert(DBHelper.TABLE_CONTACTS8, null, contentValues2);
            }
            for(int i=0; i<parentArray3.length(); i++) {
                JSONObject finalObject3 = parentArray3.getJSONObject(i);
                String nameParterres  = finalObject3.getString("parterres");
                ContentValues contentValues3 = new ContentValues();
                contentValues3.put(DBHelper.KEY_NAMEPARTERRES9, nameParterres);
                database.insert(DBHelper.TABLE_CONTACTS9, null, contentValues3);

            }
            database.close();
            dbHelper.close();
            return "Данные о метках обновлены";
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void onProgressUpdate(Integer... progress) {
        super.onProgressUpdate();
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

    }
}
