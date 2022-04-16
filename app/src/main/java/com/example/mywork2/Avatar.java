package com.example.mywork2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mywork2.Util.ImageUtil;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Avatar extends AppCompatActivity {

    final int REQUEST_CODE_TAKE = 1;
    final int REQUEST_CODE_CHOOSE = 2;
    ImageView imageView;
    //TextView from_cam, from_gallery, save;
    BottomSheetDialog bottomSheetDialog;
    Uri imageUri;
    private String imageBase64;
    File imageTemp;
    Bitmap bitmap;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avatar);

        //set bottom view
        bottomSheetDialog = new BottomSheetDialog(Avatar.this,R.style.BottomSheetDialogTheme);
        View bottomView = LayoutInflater.from(getApplicationContext()).inflate(
                R.layout.avatar_bottom_sheet,
                findViewById(R.id.avatar_bottom_sheet)
        );

        //set the listener for take a photo
        bottomView.findViewById(R.id.from_camera).setOnClickListener(this::takePhoto);

        //set the listener to choose a photo from album
        bottomView.findViewById(R.id.from_album).setOnClickListener(this::choosePhoto);

        bottomView.findViewById(R.id.save).setOnClickListener(this::save);

        bottomSheetDialog.setContentView(bottomView);

        imageView = findViewById(R.id.avatar);
        imageView.setOnClickListener(this::bottomSheet);
        initData();
        /*from_cam = findViewById(R.id.from_camera);
        from_cam.setOnClickListener(this::takePhoto);

        save = findViewById(R.id.save);
        save.setOnClickListener(this::save);
        */


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

    //override a function to receive the camera authority
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==REQUEST_CODE_TAKE){//take a photo
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                //if we have the authority, take the photo
                doTake();
            else {
                //if no authority, warn "no authority"
                Toast.makeText(this, "No authority to use the camera!", Toast.LENGTH_SHORT).show();
            }

        }else if (requestCode==REQUEST_CODE_CHOOSE){//choose a photo from album
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                //if we have the authority, open the album
                openAlbum();
            else {
                //if no authority, warn "no authority"
                Toast.makeText(this, "No authority to open the album!", Toast.LENGTH_SHORT).show();
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

    //override the function to get the photo we have just taken or choose a photo
    @SuppressLint("Range")
    @RequiresApi(api = Build.VERSION_CODES.O)
    //Penghui Xiao, reference from https://www.jianshu.com/p/b290eecbd87e
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_TAKE) {//take a photo from camera
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
        }else if(requestCode == REQUEST_CODE_CHOOSE) {//choose a photo from album
            if (data != null) {
                handleImageOnApi19(data);
            }

        }
    }
    @TargetApi(21)
    //method to get the image's path if API > 21
    private void handleImageOnApi19(Intent data){
        String imagePath = null;
        Uri uri = data.getData();
        if(DocumentsContract.isDocumentUri(this,uri)){
            String documentId = DocumentsContract.getDocumentId(uri);
            if(TextUtils.equals(uri.getAuthority(),"com.android.providers.media.documents")){
                String id = documentId.split(":")[1];
                String selection = MediaStore.Images.Media._ID+"="+id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);

            }else if(TextUtils.equals(uri.getAuthority(),"com.android.providers.downloads.documents")){
                if(documentId!=null && documentId.startsWith("msf:")){
                    resolveMSDContent(uri,documentId);
                    return;
                }
                assert documentId != null;
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.parseLong(documentId));
                imagePath = getImagePath(contentUri,null);
            }
        }else {
            assert uri != null;
            if("content".equalsIgnoreCase(uri.getScheme()))
                imagePath = getImagePath(uri,null);
            else if("file".equalsIgnoreCase(uri.getScheme()))
                imagePath = uri.getPath();
        }
        //now we have got the path
        displayImage(imagePath);
    }

    private void resolveMSDContent(Uri uri,String id) {
        File file = new File(getCacheDir(),"temp_file"+getContentResolver().getType(uri).split("/")[1]);
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            OutputStream outputStream = new FileOutputStream(file);
            byte[] buffer = new byte[4*1024];
            int read;
            while ((read = inputStream.read(buffer)) != -1){
                outputStream.write(buffer,0,read);
            }
            outputStream.flush();
            BitmapFactory.decodeFile(file.getAbsolutePath());
            imageView.setImageBitmap(bitmap);
            imageBase64 = ImageUtil.imageToBase64(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //method to get the image's path if API < 19
    private void handleApiBefore19(Intent data){
        Uri uri = data.getData();
        String imagePath = getImagePath(uri,null);
        displayImage(imagePath);
    }

    //method to get the image's path
    @SuppressLint("Range")
    private String getImagePath(Uri uri, String selection){
        String path = null;
        Cursor cursor = getContentResolver().query(uri,null,selection,null,null);
        if(cursor!=null){
            if (cursor.moveToFirst()){//move it to the 1st line
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }


    //method to show the photo
    private void displayImage(String imagePath){

        if(imagePath!=null){
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            imageView.setImageBitmap(bitmap);
            imageBase64 = ImageUtil.imageToBase64(bitmap);
        }
    }

    //method to choose a photo from album
    public void choosePhoto(View view){
        //check if there is authority to open the album
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            //open the album
            openAlbum();
        }else {
            //apply for the authority, the request code is 2
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CODE_CHOOSE);
        }
    }

    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,REQUEST_CODE_CHOOSE);
    }


    //method to save the photo
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void save(View view){
        SharedPreferences spfRecord = getSharedPreferences("spfRecord", MODE_PRIVATE);
        SharedPreferences.Editor edit = spfRecord.edit();

        edit.putString("image_64", imageBase64);
        edit.apply();



        this.finish();
        bottomSheetDialog.dismiss();


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

    /**
     * create by J. Cheng
     * @param view
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void bottomSheet(View view){

        bottomSheetDialog.show();
    }

}