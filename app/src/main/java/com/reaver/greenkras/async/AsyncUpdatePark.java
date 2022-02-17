package com.reaver.greenkras.async;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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
import java.nio.charset.StandardCharsets;


public class AsyncUpdatePark extends AsyncTask<String,Integer,String> {
    @SuppressLint("StaticFieldLeak")
    private Context ctx;
    private DBHelper dbHelper;

    public AsyncUpdatePark(Context ctx){
        this.ctx=ctx;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        String download= "https://greenkras.ru/text.php";
        HttpURLConnection connection;
        BufferedReader reader;
        try {
            URL url = new URL (download);
            connection  = (HttpURLConnection)url.openConnection();
            connection.connect();
            InputStream stream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
            StringBuilder buffer = new StringBuilder();
            String line;
            while ((line = reader.readLine())!=null) {
                buffer.append(line);
            }
            dbHelper = new DBHelper(ctx);
            SQLiteDatabase database = dbHelper.getWritableDatabase();
            int count = 0;
            Cursor cursor5 = database.query(DBHelper.TABLE_CONTACTS5, null, null, null, null, null, null);
            if (cursor5.moveToFirst()) {

                do {
                    count = count+1;
                } while (cursor5.moveToNext());
            }
            cursor5.close();
            String finalJson = buffer.toString();
            JSONObject parentObject = new JSONObject(finalJson);
            JSONArray parentArray = parentObject.getJSONArray("parktop");
            if (count!= parentArray.length()){
                database.delete(DBHelper.TABLE_CONTACTS5, null, null);
                for(int i=0; i<parentArray.length(); i++) {
                JSONObject finalObject = parentArray.getJSONObject(i);
                String name = finalObject.getString("name_park");
                String cords = finalObject.getString("cords");
                String x = finalObject.getString("centerx");
                String y = finalObject.getString("centery");
                ContentValues contentValues5 = new ContentValues();
                contentValues5.put(DBHelper.KEY_NAMEPARK5, name);
                contentValues5.put(DBHelper.KEY_PARKCORD5, cords);
                contentValues5.put(DBHelper.KEY_CENTERX5, x);
                contentValues5.put(DBHelper.KEY_CENTERY5, y);
                database.insert(DBHelper.TABLE_CONTACTS5, null, contentValues5);
                }
            }
            database.close();
            return "Данные о метках обновлены";
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        dbHelper.close();
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
