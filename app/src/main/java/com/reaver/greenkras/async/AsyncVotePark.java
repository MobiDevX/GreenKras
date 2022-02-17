package com.reaver.greenkras.async;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;
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

public class AsyncVotePark extends AsyncTask<String,Void,String> {
    @SuppressLint("StaticFieldLeak")
    private Context ctx;
    private String Saved_Login;

    public AsyncVotePark(Context ctx){
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
    protected String doInBackground(String... params) {
        String send_vote = "https://greenkras.ru/votepark.php";
        String key = "Request_a9Xi7EF&%gAMkPkFWVpxIe3Bvx%%t*Z0uXU69#0YbbsUwjo@n$";

        SharedPreferences pref = ctx.getSharedPreferences("login", Context.MODE_PRIVATE);
        String login = pref.getString(Saved_Login, "");
        String Coords = params[0];
        String vote = params[1];
        try {
            URL url = new URL(send_vote);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream OS = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, StandardCharsets.UTF_8));
            String data = URLEncoder.encode("login", "UTF-8") + "=" + URLEncoder.encode(login, "UTF-8") + "&" +
                    URLEncoder.encode("cord_park", "UTF-8") + "=" + URLEncoder.encode(Coords, "UTF-8") + "&" +
                    URLEncoder.encode("voters", "UTF-8") + "=" + URLEncoder.encode(vote, "UTF-8") + "&" +
                    URLEncoder.encode("key", "UTF-8") + "=" + URLEncoder.encode(key, "UTF-8");
            bufferedWriter.write(data);
            bufferedWriter.flush();
            bufferedWriter.close();
            OS.close();
            InputStream IS = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(IS, StandardCharsets.UTF_8));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                response.append(line);
            }
            bufferedReader.close();
            IS.close();
            httpURLConnection.disconnect();
            return response.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
        @Override
    protected void onPostExecute(String result) {

        if (result == null){
            Toast.makeText(ctx, "Повторите попытку через несколько секунд", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(ctx,result,Toast.LENGTH_LONG).show();
        }
    }
}
