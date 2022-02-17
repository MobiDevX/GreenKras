package com.reaver.greenkras.ui.personal;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.exifinterface.media.ExifInterface;
import androidx.fragment.app.Fragment;
import com.reaver.greenkras.DBHelper;
import com.reaver.greenkras.R;
import com.reaver.greenkras.async.AsyncAddData;
import com.reaver.greenkras.async.AsyncUserInfo;
import com.squareup.picasso.Picasso;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

import static android.app.Activity.RESULT_OK;
import static android.os.Environment.DIRECTORY_PICTURES;


public class Account extends Fragment {
    private static int CODE_GALLERY = 1;
    private String Saved_Login, login;
    private  CircleImageView photoUser;
    private  String patchToFile;
    private Button apply;
    private String avatar;
    private Bitmap bitmap;
    private LinearLayout laEmail, laPassword;
    private Integer visibilityPassword = 1;
    private Integer visibilityEmail = 1;
    private EditText ET_password, ET_password2, ET_email;
    private ImageView visible;
    private Integer visib = 0;
    private SpotsDialog progressDialog;

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        TextView login1 = view.findViewById(R.id.login);
        photoUser = view.findViewById(R.id.avatar);
        apply = view.findViewById(R.id.Apply);
        ProgressBar progress = view.findViewById(R.id.progressLvl);
        TextView twLvl = view.findViewById(R.id.twLvl);
        TextView twTit = view.findViewById(R.id.twTit);
        TextView twEmail = view.findViewById(R.id.twEmail);
        TextView twPassword = view.findViewById(R.id.twPassword);
        TextView twWarn = view.findViewById(R.id.twWarn);
        TextView twAllVot = view.findViewById(R.id.twAllVot);
        TextView twAll = view.findViewById(R.id.twAll);
        TextView twSpark = view.findViewById(R.id.twSpark);
        TextView twSparter = view.findViewById(R.id.twSparter);
        TextView twSkust = view.findViewById(R.id.twSkust);
        TextView twSder = view.findViewById(R.id.twSder);
        TextView twOst = view.findViewById(R.id.twOst);
        DBHelper dbHelper = new DBHelper(getContext());
        laEmail = view.findViewById(R.id.laEmail);
        laPassword = view.findViewById(R.id.laPassword);
        SharedPreferences sPref = requireContext().getSharedPreferences("login", Context.MODE_PRIVATE);
        login = sPref.getString(Saved_Login, "");
        login1.setText(login);
        ET_password = view.findViewById(R.id.Password);
        ET_password2 = view.findViewById(R.id.Password2);
        ET_email = view.findViewById(R.id.Email);
        visible = view.findViewById(R.id.visibleOn);

        if(Build.VERSION.SDK_INT >= 23){
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);

        }

        visible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (visib == 0) {
                    visible.setImageResource(R.drawable.ic_visibility_off);
                    ET_password.setTransformationMethod(null);
                    ET_password.setSelection(ET_password.length());
                    ET_password2.setTransformationMethod(null);
                    ET_password2.setSelection(ET_password2.length());
                    visib=1;
                }
                else {
                    visible.setImageResource(R.drawable.ic_visibility);
                    ET_password.setTransformationMethod(new PasswordTransformationMethod());
                    ET_password.setSelection(ET_password.length());
                    ET_password2.setTransformationMethod(new PasswordTransformationMethod());
                    ET_password2.setSelection(ET_password2.length());
                    visib=0;
                }
            }
        });

        ET_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                apply.setVisibility(View.VISIBLE);
            }
        });

        ET_password2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                apply.setVisibility(View.VISIBLE);
            }
        });

        SQLiteDatabase database = dbHelper.getWritableDatabase();
        Cursor cursor = database.query(DBHelper.TABLE_CONTACTS6, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            int titC = cursor.getColumnIndex(DBHelper.KEY_TITUL6);
            int avatarC = cursor.getColumnIndex(DBHelper.KEY_AVATAR6);
            int lvlC = cursor.getColumnIndex(DBHelper.KEY_LVL6);
            int procC = cursor.getColumnIndex(DBHelper.KEY_PROC6);
            int ostobjC = cursor.getColumnIndex(DBHelper.KEY_OST6);
            int derC = cursor.getColumnIndex(DBHelper.KEY_SCHETDER6);
            int kustC = cursor.getColumnIndex(DBHelper.KEY_SCHETKUST6);
            int cvetC = cursor.getColumnIndex(DBHelper.KEY_SCHETPART6);
            int parkC = cursor.getColumnIndex(DBHelper.KEY_SCHETPARK6);
            int allC = cursor.getColumnIndex(DBHelper.KEY_ALLOBJ6);
            int voteC = cursor.getColumnIndex(DBHelper.KEY_ALLVOTERS6);
            int warnC = cursor.getColumnIndex(DBHelper.KEY_WARN6);
            do {
                avatar = cursor.getString(avatarC);
                String lvl = cursor.getString(lvlC);
                String titul = cursor.getString(titC);
                String ostobj = cursor.getString(ostobjC);
                String der = cursor.getString(derC);
                String kust = cursor.getString(kustC);
                String cvet = cursor.getString(cvetC);
                String park = cursor.getString(parkC);
                String all = cursor.getString(allC);
                String vote = cursor.getString(voteC);
                String warn = cursor.getString(warnC);
                String proc = cursor.getString(procC);
                int progresslvl = Integer.parseInt(proc.trim());
                twTit.setText("Титул: "+titul);
                twOst.setText("Объектов до уровня: " + ostobj);
                twLvl.setText(lvl + " уровень");
                twSder.setText("Деревьев отмечено: " + der);
                twSkust.setText("Кустарников отмечено: " + kust);
                twSparter.setText("Цветников отмечено: " + cvet);
                twSpark.setText("Парков отмечено: " + park);
                twAll.setText("Всего объектов: " + all);
                twAllVot.setText("Голосов учтено: " + vote);
                twWarn.setText("Добавил недостоверных объектов : " + warn);
                progress.setProgress(progresslvl);
                UpdateAvatar();
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        dbHelper.close();
        twEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ET_password.setText("");
                ET_password2.setText("");
                ET_email.setText("");
                if (visibilityEmail == 0){
                    laPassword.setVisibility(View.GONE);
                    laEmail.setVisibility(View.GONE);
                    visibilityEmail = 1;
                } else {
                    laPassword.setVisibility(View.GONE);
                    laEmail.setVisibility(View.VISIBLE);
                    visibilityEmail = 0;
                }
                visibilityPassword = 1;
                apply.setVisibility(View.GONE);
            }

        });
        twPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ET_password.setText("");
                ET_password2.setText("");
                ET_email.setText("");
                if (visibilityPassword == 0){
                    laPassword.setVisibility(View.GONE);
                    laEmail.setVisibility(View.GONE);
                    visibilityPassword = 1;
                } else {
                    laPassword.setVisibility(View.VISIBLE);
                    laEmail.setVisibility(View.GONE);
                    visibilityPassword = 0;
                }
                visibilityEmail = 1;
                apply.setVisibility(View.GONE);
            }
        });
        final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
            apply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (bitmap != null){
                        AsyncPhoto asyncPhoto = new AsyncPhoto();
                        asyncPhoto.execute(patchToFile);
                        Picasso.get().invalidate(avatar);
                    }
                    if (visibilityPassword == 0){
                        String pas1 = ET_password.getText().toString();
                        String pas2 = ET_password2.getText().toString();
                        if (pas1.equals(pas2) && pas2.length()>6){
                        AsyncAddData asyncAddData = new AsyncAddData(getContext());
                        asyncAddData.execute("Password", pas1, pas2);
                        } else
                            Toast.makeText(getContext(),"Пароли должены совпадать и быть больше 7и символов",Toast.LENGTH_LONG).show();
                    }
                    if (visibilityEmail == 0){
                        String email = ET_email.getText().toString();

                        if (email.trim().matches(emailPattern)) {
                            AsyncAddData asyncAddData = new AsyncAddData(getContext());
                            asyncAddData.execute("Email", email);
                        } else
                            Toast.makeText(getContext(),"Введите настоящий Email",Toast.LENGTH_LONG).show();
                    }
                    apply.setVisibility(View.GONE);
                    AsyncUserInfo asyncUserInfo = new AsyncUserInfo(getContext());
                    asyncUserInfo.execute("UploadUser");

                }
            });
            photoUser.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("IntentReset")
            @Override
            public void onClick(View v) {
                int permissionStatus = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Выберите изображение"), CODE_GALLERY);}
                else {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                }
            }
        });
        return view;
    }

    @SuppressLint("StaticFieldLeak")
    public class AsyncPhoto extends AsyncTask<String,Integer,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new SpotsDialog(getContext(), R.style.Download);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String Avatar = "https://greenkras.ru/updavatar.php";
            String key = "Request_a9Xi7EF&%gAMkPkFWVpxIe3Bvx%%t*Z0uXU69#0YbbsUwjo@n$";
            String weight = params[0];
            String img_str;
            try {
                if (weight != null) {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    Bitmap bitmap = BitmapFactory.decodeFile(weight);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
                    byte[] image = stream.toByteArray();
                    img_str = Base64.encodeToString(image, Base64.DEFAULT);
                } else {
                    img_str = "";
                }
                URL url = new URL(Avatar);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, StandardCharsets.UTF_8));
                String data = URLEncoder.encode("login", "UTF-8") + "=" + URLEncoder.encode(login, "UTF-8") + "&" +
                        URLEncoder.encode("key", "UTF-8") + "=" + URLEncoder.encode(key, "UTF-8") + "&" +
                        URLEncoder.encode("avatarlegendaobaange", "UTF-8") + "=" + URLEncoder.encode(img_str, "UTF-8");
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

        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if(requestCode == CODE_GALLERY){
                assert data != null;
                Uri image_uri = data.getData();
                assert image_uri != null;
                patchToFile = getImagePathFromInputStreamUri(image_uri);
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), image_uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                bitmap = RotateBitmap(bitmap, patchToFile);
                photoUser.setImageBitmap(bitmap);
                apply.setVisibility(View.VISIBLE);
            }
        }
    }

    private File createTemporalFile() {
        @SuppressLint("SimpleDateFormat") String name = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(Environment.getExternalStoragePublicDirectory(DIRECTORY_PICTURES), name +".jpg"); // context needed
    }

    private File createTemporalFileFrom(InputStream inputStream) throws IOException {
        File targetFile = null;
        if (inputStream != null) {
            int read;
            byte[] buffer = new byte[8 * 1024];
            targetFile = createTemporalFile();
            OutputStream outputStream = new FileOutputStream(targetFile);
            while ((read = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
            }
            outputStream.flush();
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return targetFile;
    }

    private String getImagePathFromInputStreamUri(Uri uri) {
        InputStream inputStream = null;
        String filePath = null;

        if (uri.getAuthority() != null) {
            try {
                inputStream = requireContext().getContentResolver().openInputStream(uri); // context needed
                File photoFile = createTemporalFileFrom(inputStream);
                filePath = photoFile.getPath();

            } catch (IOException ignored) {
            } finally {
                try {
                    assert inputStream != null;
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return filePath;
    }

    @SuppressLint("ExifInterface")
    private Bitmap RotateBitmap(Bitmap bitmap, String patchToFile)
    {
        android.media.ExifInterface exifInterface = null;
        try{
            exifInterface = new android.media.ExifInterface(patchToFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert exifInterface != null;
        int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
        Matrix matrix = new Matrix();
        switch (orientation){
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(270);
                break;
            default:
        }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    private void UpdateAvatar(){
        Picasso.get()
                .load(avatar)
                .error(R.drawable.shinobu)
                .into(photoUser);
    }
}


