package com.reaver.greenkras.async;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.widget.Toast;
import com.reaver.greenkras.DBHelper;
import com.reaver.greenkras.MainActivity;
import com.reaver.greenkras.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import dmax.dialog.SpotsDialog;

public class AsyncAddData extends AsyncTask<String,Void,String> {
    @SuppressLint("StaticFieldLeak")
    private Context ctx;
    private String Saved_Login;
    private SpotsDialog progressDialog;

    public AsyncAddData(Context ctx){
        this.ctx=ctx;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new SpotsDialog(ctx, R.style.Upload);
        progressDialog.show();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected String doInBackground(String... params) {
        String send_tree = "https://greenkras.ru/addtree.php";
        String send_scrub = "https://greenkras.ru/addscrub.php";
        String send_parterres = "https://greenkras.ru/addparterres.php";
        String send_park = "https://greenkras.ru/addparks.php";
        String send_missing = "https://greenkras.ru/delder.php";
        String send_mail = "https://greenkras.ru/updmail.php";
        String send_pass = "https://greenkras.ru/updpas.php";
        String key = "Request_a9Xi7EF&%gAMkPkFWVpxIe3Bvx%%t*Z0uXU69#0YbbsUwjo@n$";
        String method = params[0];

        SharedPreferences pref;
        if (method.equals("SendTree")){
            pref = ctx.getSharedPreferences("login", Context.MODE_PRIVATE);
            String login = pref.getString(Saved_Login, "");
            String Tree_nameder = params[1];
            String Tree_hp = params[2];
            String Tree_sost = params[3];
            String Tree_diamst = params[4];
            String Tree_vis = params[5];
            String Tree_diamkro = params[6];
            String Tree_coordix = params[7];
            String Tree_coordiy = params[8];
            String weight = params[9];
            String img_str;
            try {
                if (weight != null){
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    Bitmap bitmap = BitmapFactory.decodeFile(weight);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 70  , stream);
                    byte[] image = stream.toByteArray();
                    img_str = Base64.encodeToString(image, Base64.DEFAULT);
                }
                else {
                    img_str = "";
                }
                URL url = new URL (send_tree);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, StandardCharsets.UTF_8));
                String data = URLEncoder.encode("Tree_nameder","UTF-8") +"="+ URLEncoder.encode(Tree_nameder,"UTF-8")+"&"+
                        URLEncoder.encode("Tree_hp","UTF-8") +"="+ URLEncoder.encode(Tree_hp, "UTF-8") +"&"+
                        URLEncoder.encode("Tree_sost","UTF-8") +"="+ URLEncoder.encode(Tree_sost,"UTF-8") +"&"+
                        URLEncoder.encode("Tree_diamst","UTF-8") +"="+ URLEncoder.encode(Tree_diamst,"UTF-8") +"&"+
                        URLEncoder.encode("Tree_vis","UTF-8") +"="+ URLEncoder.encode(Tree_vis,"UTF-8") +"&"+
                        URLEncoder.encode("Tree_diamkro","UTF-8") +"="+ URLEncoder.encode(Tree_diamkro,"UTF-8") +"&"+
                        URLEncoder.encode("Tree_coordix","UTF-8") +"="+ URLEncoder.encode(Tree_coordix,"UTF-8") +"&"+
                        URLEncoder.encode("Tree_coordiy","UTF-8") +"="+ URLEncoder.encode(Tree_coordiy,"UTF-8") +"&"+
                        URLEncoder.encode("login","UTF-8") +"="+ URLEncoder.encode(login,"UTF-8") +"&"+
                        URLEncoder.encode("key","UTF-8") +"="+ URLEncoder.encode(key,"UTF-8") +"&"+
                        URLEncoder.encode("imagerrs","UTF-8") +"="+ URLEncoder.encode(img_str,"UTF-8");
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

        if (method.equals("SendScrub")){
            pref = ctx.getSharedPreferences("login", Context.MODE_PRIVATE);
            String login = pref.getString(Saved_Login, "");
            String Scrub_nameschub = params[1];
            String Scrub_ploshad = params[2];
            String Scrub_hp = params[3];
            String Scrub_tip = params[4];
            String Scrub_coordix = params[5];
            String Scrub_coordiy = params[6];
            String weight = params[7];
            String img_str;
            try {
                if (weight != null){
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    Bitmap bitmap = BitmapFactory.decodeFile(weight);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
                    byte[] image = stream.toByteArray();
                    img_str = Base64.encodeToString(image, Base64.DEFAULT);
                }
                else {
                    img_str = "";
                }
                URL url = new URL (send_scrub);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, StandardCharsets.UTF_8));
                String data = URLEncoder.encode("Scrub_namescrub","UTF-8") +"="+ URLEncoder.encode(Scrub_nameschub,"UTF-8")+"&"+
                        URLEncoder.encode("Scrub_ploshad","UTF-8") +"="+ URLEncoder.encode(Scrub_ploshad, "UTF-8") +"&"+
                        URLEncoder.encode("Scrub_hp","UTF-8") +"="+ URLEncoder.encode(Scrub_hp,"UTF-8") +"&"+
                        URLEncoder.encode("Scrub_tip","UTF-8") +"="+ URLEncoder.encode(Scrub_tip,"UTF-8") +"&"+
                        URLEncoder.encode("Scrub_coordix","UTF-8") +"="+ URLEncoder.encode(Scrub_coordix,"UTF-8") +"&"+
                        URLEncoder.encode("Scrub_coordiy","UTF-8") +"="+ URLEncoder.encode(Scrub_coordiy,"UTF-8") +"&"+
                        URLEncoder.encode("login","UTF-8") +"="+ URLEncoder.encode(login,"UTF-8") +"&"+
                        URLEncoder.encode("key","UTF-8") +"="+ URLEncoder.encode(key,"UTF-8") +"&"+
                        URLEncoder.encode("imagerrs","UTF-8") +"="+ URLEncoder.encode(img_str,"UTF-8");
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

        if (method.equals("SendParterres")){
            pref = ctx.getSharedPreferences("login", Context.MODE_PRIVATE);
            String login = pref.getString(Saved_Login, "");
            String Parterres_nameparterres = params[1];
            String Parterres_ploshad = params[2];
            String Parterres_hp = params[3];
            String Parterres_tip = params[4];
            String Parterres_coordix = params[5];
            String Parterres_coordiy = params[6];
            String weight = params[7];
            String img_str;
            try {
                if (weight != null){
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    Bitmap bitmap = BitmapFactory.decodeFile(weight);
                    bitmap.compress(Bitmap.CompressFormat.JPEG , 70, stream);
                    byte[] image = stream.toByteArray();
                    img_str = Base64.encodeToString(image, Base64.DEFAULT);
                }
                else {
                    img_str = "";
                }
                URL url = new URL (send_parterres);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, StandardCharsets.UTF_8));
                String data = URLEncoder.encode("Parterres_nameparterres","UTF-8") +"="+ URLEncoder.encode(Parterres_nameparterres,"UTF-8")+"&"+
                        URLEncoder.encode("Parterres_ploshad","UTF-8") +"="+ URLEncoder.encode(Parterres_ploshad, "UTF-8") +"&"+
                        URLEncoder.encode("Parterres_hp","UTF-8") +"="+ URLEncoder.encode(Parterres_hp,"UTF-8") +"&"+
                        URLEncoder.encode("Parterres_tip","UTF-8") +"="+ URLEncoder.encode(Parterres_tip,"UTF-8") +"&"+
                        URLEncoder.encode("Parterres_coordix","UTF-8") +"="+ URLEncoder.encode(Parterres_coordix,"UTF-8") +"&"+
                        URLEncoder.encode("Parterres_coordiy","UTF-8") +"="+ URLEncoder.encode(Parterres_coordiy,"UTF-8") +"&"+
                        URLEncoder.encode("login","UTF-8") +"="+ URLEncoder.encode(login,"UTF-8") +"&"+
                        URLEncoder.encode("key","UTF-8") +"="+ URLEncoder.encode(key,"UTF-8") +"&"+
                        URLEncoder.encode("imagerrs","UTF-8") +"="+ URLEncoder.encode(img_str,"UTF-8");
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

        if (method.equals("SendPark")){
            pref = ctx.getSharedPreferences("login", Context.MODE_PRIVATE);
            String login = pref.getString(Saved_Login, "");
            String Park_name = params[1];
            String Park_polygon = params[2];
            try {
                URL url = new URL (send_park);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, StandardCharsets.UTF_8));
                String data = URLEncoder.encode("Park_name","UTF-8") +"="+ URLEncoder.encode(Park_name,"UTF-8") +"&"+
                        URLEncoder.encode("Park_polygon","UTF-8") +"="+ URLEncoder.encode(Park_polygon,"UTF-8")+"&"+
                        URLEncoder.encode("login","UTF-8") +"="+ URLEncoder.encode(login,"UTF-8") +"&"+
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

        if (method.equals("UploadPark")){
            DBHelper dbHelper = new DBHelper(ctx);
            SQLiteDatabase database = dbHelper.getWritableDatabase();
            database.delete(DBHelper.TABLE_CONTACTS4, null, null);
            String download= "https://greenkras.ru/parkout.php";
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
                String finalJson = buffer.toString();
                JSONObject parentObject = new JSONObject(finalJson);
                JSONArray parentArray = parentObject.getJSONArray("addparks");
                for(int i=0; i<parentArray.length(); i++) {
                    JSONObject finalObject = parentArray.getJSONObject(i);
                    String Park_name  = finalObject.getString("Park_name");
                    String cord  = finalObject.getString("cord");
                    String login  = finalObject.getString("login");
                    String vote_yes  = finalObject.getString("vote_yes");
                    String vote_no  = finalObject.getString("vote_no");
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(DBHelper.KEY_PARKNAME, Park_name);
                    contentValues.put(DBHelper.KEY_PARKCORD, cord);
                    contentValues.put(DBHelper.KEY_PARKLOGIN, login);
                    contentValues.put(DBHelper.KEY_VOTEYES, vote_yes);
                    contentValues.put(DBHelper.KEY_VOTENO, vote_no);
                    database.insert(DBHelper.TABLE_CONTACTS4, null, contentValues);
                }
                dbHelper.close();
                return null;
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }

        if (method.equals("Missing")){
            String coordix = params[1];
            String coordiy = params[2];
            String res = params[3];
            pref = ctx.getSharedPreferences("login", Context.MODE_PRIVATE);
            String login = pref.getString(Saved_Login, "");
            try {
                URL url = new URL (send_missing);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, StandardCharsets.UTF_8));
                String data = URLEncoder.encode("login","UTF-8") +"="+ URLEncoder.encode(login,"UTF-8") +"&"+
                        URLEncoder.encode("Tree_coordix","UTF-8") +"="+ URLEncoder.encode(coordix,"UTF-8") +"&"+
                        URLEncoder.encode("Tree_coordiy","UTF-8") +"="+ URLEncoder.encode(coordiy,"UTF-8") +"&"+
                        URLEncoder.encode("sost","UTF-8") +"="+ URLEncoder.encode(res,"UTF-8") +"&"+
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

        if (method.equals("Email")){
            String mail = params[1];
            pref = ctx.getSharedPreferences("login", Context.MODE_PRIVATE);
            String login = pref.getString(Saved_Login, "");
            try {
                URL url = new URL (send_mail);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, StandardCharsets.UTF_8));
                String data = URLEncoder.encode("mail","UTF-8") +"="+ URLEncoder.encode(mail,"UTF-8") +"&"+
                        URLEncoder.encode("login","UTF-8") +"="+ URLEncoder.encode(login,"UTF-8") +"&"+
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

        if (method.equals("Password")){
            String pass1 = params[1];
            String pass2 = params[2];
            pref = ctx.getSharedPreferences("login", Context.MODE_PRIVATE);
            String login = pref.getString(Saved_Login, "");
            try {
                URL url = new URL (send_pass);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, StandardCharsets.UTF_8));
                String data = URLEncoder.encode("pas1","UTF-8") +"="+ URLEncoder.encode(pass1,"UTF-8") +"&"+
                        URLEncoder.encode("pas2","UTF-8") +"="+ URLEncoder.encode(pass2,"UTF-8") +"&"+
                        URLEncoder.encode("login","UTF-8") +"="+ URLEncoder.encode(login,"UTF-8") +"&"+
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
        if (progressDialog.isShowing()) {
            progressDialog.cancel();
        }
        if (result == null){
            Toast.makeText(ctx, "Повторите попытку через несколько секунд", Toast.LENGTH_LONG).show();
        }
        else if (result.equals("Вы успешно добавили данные!")) {
            Toast.makeText(ctx,result,Toast.LENGTH_LONG).show();
            Intent intent = new Intent(ctx, MainActivity.class);
            ctx.startActivity(intent);
            ((Activity)ctx).finish();

        }

        else {
            Toast.makeText(ctx,result,Toast.LENGTH_LONG).show();
        }
    }
}
