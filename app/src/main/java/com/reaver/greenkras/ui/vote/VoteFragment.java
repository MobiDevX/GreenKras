package com.reaver.greenkras.ui.vote;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.reaver.greenkras.DBHelper;
import com.reaver.greenkras.R;
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
import java.util.ArrayList;
import dmax.dialog.SpotsDialog;

public class VoteFragment extends Fragment {
    private DBHelper dbHelper;
    private static final int ADD_VOTE_CODE = 1;
    private DBHelper sqlite;
    private VoteAdapter voteAdapter;
    private SpotsDialog progressDialog;

    @SuppressLint("CutPasteId")
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_vote, container, false);
        RecyclerView recyclerview = view.findViewById(R.id.recycler_view);
        ArrayList<Item> itemArrayList = new ArrayList<>();
        sqlite = new DBHelper(getContext());
        SQLiteDatabase sq = sqlite.getWritableDatabase();
        sq.delete("parkname", null, null);
        AsyncTask<String, Integer, StringBuffer> asyncTask = new AsyncVote();
        asyncTask.execute();
        voteAdapter=new VoteAdapter(itemArrayList);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        recyclerView.setAdapter(voteAdapter);
        voteDatos();
        voteAdapter.setOnItemClickListener(new VoteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Item item) {
                DialogFragment fragment = new VoteDialog(getContext(),item.parkname,item.cords, item.CenterX, item.CenterY, item.parklogin,
                        item.parkcount, item.voteyes, item.voteno);
                fragment.setTargetFragment(VoteFragment.this, ADD_VOTE_CODE);
                assert getFragmentManager() != null;
                fragment.show(getFragmentManager(), fragment.getClass().getName());
            }
        });
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener( new View.OnKeyListener()
        {
            @Override
            public boolean onKey( View v, int keyCode, KeyEvent event )
            {
                return keyCode == KeyEvent.KEYCODE_BACK;
            }
        } );
        return view;
    }

    private void voteDatos(){
        SQLiteDatabase sqLiteDatabase =sqlite.getWritableDatabase();
        Item item;
        @SuppressLint("Recycle") Cursor cursor = sqLiteDatabase.rawQuery("select * from parkname", null);
        while (cursor.moveToNext()){
            item = new Item();
            item.setId(cursor.getInt(0));
            item.setParkname(cursor.getString(1));
            item.setCords(cursor.getString(2));
            item.setCenterX(cursor.getString(3));
            item.setCenterY(cursor.getString(4));
            item.setParklogin(cursor.getString(5));
            item.setVoteyes(cursor.getString(6));
            item.setVoteno(cursor.getString(7));
            int Y = Integer.parseInt(cursor.getString(6));
            int N = Integer.parseInt(cursor.getString(7));
            int votes = Y+N;
            String r = Integer.toString(votes);
            item.setParkcount(r);
            voteAdapter.agregarItem(item);
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class AsyncVote extends AsyncTask<String,Integer,StringBuffer> {
        String Saved_Login;
        SharedPreferences Pref;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new SpotsDialog(requireContext(), R.style.Download);
            progressDialog.show();
            progressDialog.setCancelable(false);
        }

        @Override
        protected StringBuffer doInBackground(String... params) {
            String download= "https://greenkras.ru/parkout.php";
            String key = "Request_a9Xi7EF&%gAMkPkFWVpxIe3Bvx%%t*Z0uXU69#0YbbsUwjo@n$";
            Pref = requireContext().getSharedPreferences("login", Context.MODE_PRIVATE);
            String login = Pref.getString(Saved_Login, "");
            try {
                URL url = new URL (download);
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

            return null;
        }


        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate();
        }

        @Override
        protected void onPostExecute(StringBuffer result) {
            if (result == null) {
                Toast.makeText(getContext(), "Повторите попытку через несколько секунд", Toast.LENGTH_LONG).show();
            } else {
                try {
                    String finalJson = result.toString();
                    JSONObject parentObject = new JSONObject(finalJson);
                    JSONArray parentArray = parentObject.getJSONArray("addparks");
                    for(int i=0; i<parentArray.length(); i++) {
                        JSONObject finalObject = parentArray.getJSONObject(i);
                        String Park_name  = finalObject.getString("Park_name");
                        String cord  = finalObject.getString("cord");
                        String login  = finalObject.getString("login");
                        String centerx = finalObject.getString("cenetrx");
                        String centery = finalObject.getString("cenetry");
                        String vote_yes  = finalObject.getString("vote_yes");
                        String vote_no  = finalObject.getString("vote_no");
                        dbHelper = new DBHelper(getContext());
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(DBHelper.KEY_PARKNAME, Park_name);
                        contentValues.put(DBHelper.KEY_PARKCORD, cord);
                        contentValues.put(DBHelper.KEY_CENTERX, centerx);
                        contentValues.put(DBHelper.KEY_CENTERY, centery);
                        contentValues.put(DBHelper.KEY_PARKLOGIN, login);
                        contentValues.put(DBHelper.KEY_VOTEYES, vote_yes);
                        contentValues.put(DBHelper.KEY_VOTENO, vote_no);
                        SQLiteDatabase database = dbHelper.getWritableDatabase();
                        database.insert(DBHelper.TABLE_CONTACTS4, null, contentValues);
                        dbHelper.close();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            progressDialog.dismiss();
            voteDatos();
        }
    }
}
