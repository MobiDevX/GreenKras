package com.reaver.greenkras.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.exifinterface.media.ExifInterface;
import androidx.fragment.app.DialogFragment;
import com.reaver.greenkras.R;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import static android.app.Activity.RESULT_OK;
import static android.os.Environment.DIRECTORY_PICTURES;


public class PhotoDialog extends DialogFragment implements DialogInterface.OnClickListener{
    private String patchToFile, weight1, name;
    private Bitmap bitmap;
    private TextView addPhoto, imageAdd1;
    private ImageView imageAdd, image;
    private static int CODE_CAM = 1;
    private static int CODE_GALLERY = 2;

    public PhotoDialog(String weight1) {
        this.weight1 = weight1;

    }

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.photo_dialog, null);
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme)
                .setPositiveButton(R.string.add, this)
                .setNeutralButton(R.string.cancel,this)
                .setView(view);
        addPhoto = view.findViewById(R.id.addPhoto);
        imageAdd = view.findViewById(R.id.addImage);
        imageAdd1 = view.findViewById(R.id.addPhoto1);
        image = view.findViewById(R.id.image);
        if (weight1 != null){
            Bitmap bitmap = BitmapFactory.decodeFile(weight1);
            bitmap= RotateBitmap(bitmap, weight1);
            Bitmap result = Bitmap.createScaledBitmap(bitmap,
                    750, 1000, false);
            image.setImageBitmap(result);
            addPhoto.setVisibility(View.INVISIBLE);
            imageAdd.setVisibility(View.INVISIBLE);
            imageAdd1.setVisibility(View.INVISIBLE);
            image.setVisibility(View.VISIBLE);
        }
        imageAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });
        Log.e("Кадры:", "Create");

        return adb.create();
    }

    private void disoatchPictureTakerAction(){
        Intent takePic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePic.resolveActivity(requireContext().getPackageManager()) != null){
            File photoFile;
            photoFile = createPhotoFile();
            if (photoFile!=null) {
                patchToFile = photoFile.getAbsolutePath();
                Uri photoUri = FileProvider.getUriForFile(requireContext(), "com.reaver.greenkras.fileprovider", photoFile);
                try {
                    ExifInterface exif = new ExifInterface(patchToFile);
                    exif.setAttribute(ExifInterface.TAG_ORIENTATION, String.valueOf(90));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                takePic.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePic, CODE_CAM);
            }
            Log.e("Кадры:", "disoatchPictureTakerAction");
        }
    }

    @SuppressLint("SimpleDateFormat")
    private File createPhotoFile(){
        name = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File storageDir = Environment.getExternalStoragePublicDirectory(DIRECTORY_PICTURES);
        File image = null;
        try {
            image = File.createTempFile(name, ".jpg", storageDir);
        }catch (IOException e){
            Log.d("mylog", "Excep : " +e.toString());
        }
        Log.e("Кадры:", "createPhotoFile");
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if(requestCode == CODE_CAM){
                bitmap = BitmapFactory.decodeFile(patchToFile);
                bitmap = RotateBitmap(bitmap,patchToFile);
                Bitmap result = Bitmap.createScaledBitmap(bitmap,
                        750, 1000, false);
                image.setImageBitmap(result);
                addPhoto.setVisibility(View.INVISIBLE);
                imageAdd.setVisibility(View.INVISIBLE);
                imageAdd1.setVisibility(View.INVISIBLE);
                image.setVisibility(View.VISIBLE);
                Log.e("Кадры:", "result");
            }
            else if(requestCode == CODE_GALLERY){
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
                image.setImageBitmap(bitmap);
                addPhoto.setVisibility(View.INVISIBLE);
                imageAdd.setVisibility(View.INVISIBLE);
                imageAdd1.setVisibility(View.INVISIBLE);
                image.setVisibility(View.VISIBLE);
            }
            else {
                addPhoto.setVisibility(View.VISIBLE);
                imageAdd.setVisibility(View.VISIBLE);
                imageAdd1.setVisibility(View.VISIBLE);
                image.setVisibility(View.INVISIBLE);
                bitmap= null;
                patchToFile = null;
            }
        }
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
            Log.e("Кадры:", "getImagePathFromInputStreamUri");
        }
        return filePath;
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
        Log.e("Кадры:", "createTemporalFileFrom");
        return targetFile;
    }

    @SuppressLint("SimpleDateFormat")
    private File createTemporalFile() {
        name = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        Log.e("Кадры:", "createTemporalFile");
        return new File(Environment.getExternalStoragePublicDirectory(DIRECTORY_PICTURES), name+".jpg"); // context needed
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
        int orientation = exifInterface.getAttributeInt(android.media.ExifInterface.TAG_ORIENTATION, android.media.ExifInterface.ORIENTATION_UNDEFINED);
        Matrix matrix = new Matrix();
        switch (orientation){
            case android.media.ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case android.media.ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case android.media.ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(270);
                break;
            default:
        }
        Log.e("Кадры:", "RotateBitmap");
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }



    private void SelectImage() {
        TextView camera, gallery, clear, back;
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        @SuppressLint("InflateParams") final View view = inflater.inflate(R.layout.photo_choice_dialog, null);
         final AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.MyDialogTheme);
        camera = view.findViewById(R.id.camera);
        gallery = view.findViewById(R.id.gallery);
        clear = view.findViewById(R.id.clear);
        back = view.findViewById(R.id.back);
        builder.setTitle("Добавить изображение");
        builder.setView(view);

        final AlertDialog alert = builder.show();

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Держите телефон вертикально", Toast.LENGTH_LONG).show();
                disoatchPictureTakerAction();
                alert.dismiss();
            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("IntentReset")
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Выберите фото дерева"), CODE_GALLERY);
                alert.dismiss();
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bitmap = null;
                patchToFile = null;
                weight1 = null;
                addPhoto.setVisibility(View.VISIBLE);
                imageAdd.setVisibility(View.VISIBLE);
                imageAdd1.setVisibility(View.VISIBLE);
                image.setVisibility(View.INVISIBLE);
                imageAdd.setImageDrawable(getResources().getDrawable( R.drawable.ic_add));
                alert.dismiss();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });
        Log.e("Кадры:", "SelectImage");
    }

    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case Dialog.BUTTON_POSITIVE:{
                Intent intent = new Intent();
                if(patchToFile == null){
                    patchToFile = weight1;
                }
                    intent.putExtra("1", patchToFile);
                    Objects.requireNonNull(getTargetFragment()).onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                }
            case Dialog.BUTTON_NEUTRAL:
                dialog.dismiss();
                break;
        }
        Log.e("Кадры:", "onClick");
    }
}
