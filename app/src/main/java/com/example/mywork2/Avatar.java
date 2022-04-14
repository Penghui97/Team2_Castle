package com.example.mywork2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mywork2.Util.ImageUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class Avatar extends AppCompatActivity {

    final int REQUEST_CODE_TAKE_PHOTO = 0;
    final int REQUEST_CODE_TAKE = 1;
    final int REQUEST_CODE_GALLERY = 2;
    ImageView imageView;
    Button from_cam, from_gallery, save;
    Uri imageUri;
    private String imageBase64;
    File imageTemp;
    Bitmap bitmap;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avatar);


        imageView = findViewById(R.id.avatar);
        from_cam = findViewById(R.id.from_camera);
        from_cam.setOnClickListener(this::takePhoto);

        save = findViewById(R.id.save);
        save.setOnClickListener(this::save);
        initData();


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onResume() {
        super.onResume();
    }

    //take a photo from camera
    public void takePhoto(View view){
        //check if there is authority to use the camera
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
            //take a photo
            doTake();
        }else {
            //apply for the authority, the request code is 1
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},REQUEST_CODE_TAKE);
        }
    }

    //override a function to receive the authority
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                //if we have the authority, take the photo
                doTake();
            else {
                //if no authority, warn "no authority"
                Toast.makeText(this, "No authority to use the camera!", Toast.LENGTH_SHORT).show();
            }

        }
    }

    //take a photo
    public void doTake(){
        //create a temp file to save the photo
        imageTemp = new File(getExternalCacheDir(),"imageOut.jpeg");
        //check if there is already a cache photo. If yes, delete it.
        if(imageTemp.exists())
            imageTemp.delete();
        try {
            imageTemp.createNewFile();
        }catch (IOException e){
            e.printStackTrace();
        }
        if(Build.VERSION.SDK_INT > 24){
            //contentProvider
            imageUri = FileProvider.getUriForFile(this,"com.example.mywork2.fileprovider",imageTemp);
        }else {
            imageUri = Uri.fromFile(imageTemp);
        }

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        startActivityForResult(intent,REQUEST_CODE_TAKE);
    }

    //override the function to get the photo we have just taken
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_TAKE) {
            if(resultCode == RESULT_OK){
                //get the photo
                try (InputStream inputStream = getContentResolver().openInputStream(imageUri)) {
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    imageView.setImageBitmap(bitmap);
                    //Save the photo
                    //save the String
                    imageBase64 = ImageUtil.imageToBase64(bitmap);


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //method to save the photo
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void save(View view){
        SharedPreferences spfRecord = getSharedPreferences("spfRecord", MODE_PRIVATE);
        SharedPreferences.Editor edit = spfRecord.edit();

        edit.putString("image_64", imageBase64);
        edit.apply();



        this.finish();


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void getDataFromSpf() {
        SharedPreferences spfRecord = getSharedPreferences("spfRecord", MODE_PRIVATE);
        String image64 = spfRecord.getString("image_64","");
        imageView.setImageBitmap(ImageUtil.base64ToImage(image64));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initData(){
        getDataFromSpf();
    }

}