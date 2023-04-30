package com.example.mywork2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.content.res.ResourcesCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
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
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mywork2.Util.ImageUtil;
import com.example.mywork2.dao.AvatarDao;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
/**
 * @author Penghui Xiao
 * function: upload avatar from camera or album; update avatar from local cache or database.
 * modification date and description can be found in github repository history
 */
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
    TextView savePhoto;
    String username;
    int version;

    //receive the data from the database
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 0x11) {
                imageView.setImageBitmap(ImageUtil.base64ToImage(imageBase64));
                getIMGFromDB();
                imageView.setImageBitmap(ImageUtil.base64ToImage(imageBase64));
                initData();
            }else if (msg.what == 0x22){
                noAvatar();
            }
        }
    };

    private void noAvatar() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.noavatar).setNegativeButton("OK"
                , (dialogInterface,i) -> dialogInterface.dismiss()).show();
        bottomSheetDialog.dismiss();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avatar);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        username = (String)extras.get("username");

        //set bottom view
        bottomSheetDialog = new BottomSheetDialog(Avatar.this,R.style.BottomSheetDialogTheme);
        View bottomView = LayoutInflater.from(getApplicationContext()).inflate(
                R.layout.bottom_sheet_avatar,
                findViewById(R.id.avatar_bottom_sheet)
        );
        SharedPreferences spfRecord = getSharedPreferences("spfRecord"+username, MODE_PRIVATE);
        version = spfRecord.getInt("imageVersion",0);
        Log.e("version",version+"");
        //set the listener for take a photo
        bottomView.findViewById(R.id.from_camera).setOnClickListener(this::takePhoto);

        //set the listener to choose a photo from album
        bottomView.findViewById(R.id.from_album).setOnClickListener(this::choosePhoto);

        savePhoto = bottomView.findViewById(R.id.save);

        //if no photo chosen and you click save, it will close the bottom sheet
        savePhoto.setOnClickListener(view -> {
            bottomSheetDialog.dismiss();
        });

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
                    //now we can save the photo from camera
                    savePhoto.setOnClickListener(this::save);
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
    //method to get the image's path if API > 19
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

    //deal with file path error
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


    //method to show the photo from album
    private void displayImage(String imagePath){

        if(imagePath!=null){
            bitmap = BitmapFactory.decodeFile(imagePath);

            imageView.setImageBitmap(bitmap);
            //now we can save the photo from album
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                savePhoto.setOnClickListener(this::save);
            }
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

    //open album
    private void openAlbum() {
        //open the Dir
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        //set IMG type
        intent.setType("image/*");
        startActivityForResult(intent,REQUEST_CODE_CHOOSE);
    }


    //method to save the photo
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void save(View view){
        SharedPreferences spfRecord = getSharedPreferences("spfRecord"+username, MODE_PRIVATE);
        SharedPreferences.Editor edit = spfRecord.edit();
        edit.putString("image_64", imageBase64);

        edit.apply();
        //save avatar in db
        saveToDB();

        //if no photo chosen and you click save, it will close the bottom sheet
        savePhoto.setOnClickListener(view1 -> {
            bottomSheetDialog.dismiss();
        });
        this.finish();
        bottomSheetDialog.dismiss();

    }

    //save avatar to database
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void saveToDB() {
        byte [] bytes = ImageUtil.Base64ToByteArray(imageBase64);
        byte[] little = ImageUtil.imageToByteArrayLittle(bitmap);
        new Thread(()->{
            AvatarDao avatarDao = new AvatarDao();
            avatarDao.addAvatar(username,bytes,little);
            version = avatarDao.getAvatarVersionByUsername(username);
            SharedPreferences spfRecord = getSharedPreferences("spfRecord"+username, MODE_PRIVATE);
            SharedPreferences.Editor edit = spfRecord.edit();
            edit.putInt("imageVersion",version);
            edit.apply();
        }).start();
    }

    //get avatar from local cache
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void getDataFromSpf() {
        SharedPreferences spfRecord = getSharedPreferences("spfRecord"+username, MODE_PRIVATE);
        String image64 = spfRecord.getString("image_64","");
        SharedPreferences.Editor edit = spfRecord.edit();
        if (image64.length()!=0) {
            imageView.setImageBitmap(ImageUtil.base64ToImage(image64));
        }
        new Thread(()->{
            AvatarDao avatarDao = new AvatarDao();
            int temp = avatarDao.getAvatarVersionByUsername(username);
            if (temp!=0&&temp!=version) {
                version = temp;
                edit.putInt("imageVersion",temp);
                edit.apply();
                getSmallIMGFromDB();
            }
            else if (temp==0) {
                Message message = handler.obtainMessage();
                message.what = 0x22;
                handler.sendMessage(message);
            }
        }).start();
    }

    private void getSmallIMGFromDB() {//获取缩略图并更新version缓存
        new Thread(()->{
            AvatarDao avatarDao = new AvatarDao();
            byte[] small = avatarDao.getAvatarByUsername(username);
            imageBase64 = ImageUtil.ByteArray2Base64(small);
            Message message = handler.obtainMessage();
            message.what = 0x11;
            handler.sendMessage(message);
        }).start();
    }

    private void getIMGFromDB() {//获取高清图并更新图片缓存
        new Thread(()->{
            AvatarDao avatarDao = new AvatarDao();
            byte[] bytes = avatarDao.getAvatarByUsername(username);
            if(bytes!=null){//if the user has an avatar in DB
                imageBase64 = ImageUtil.ByteArray2Base64(bytes);
                SharedPreferences spfRecord = getSharedPreferences("spfRecord"+username, MODE_PRIVATE);
                SharedPreferences.Editor edit = spfRecord.edit();
                edit.putString("image_64", imageBase64);
                edit.apply();
            }
        }).start();
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