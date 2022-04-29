package com.example.mywork2.Util;
/*
By Penghui Xiao
This class is used to convert an image to byte
Reference from a blog: https://www.cnblogs.com/lizm166/p/15788518.html
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

public class ImageUtil {

    //static method to convert Base64 to byte Array
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static byte[] Base64ToByteArray(String bitmap64) {
        return Base64.getDecoder().decode(bitmap64);
    }

    //static method to convert byte Array to Base64
    public static String ByteArray2Base64 (byte[] bytes){
        String baseStr = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            baseStr = Base64.getEncoder().encodeToString(bytes);
        }
        return baseStr;
    }

    //static method to convert IMG to byte[]
    public static byte[] imageToByteArray(Bitmap bitmap) {
        //define an output stream
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        //compress img
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,byteArrayOutputStream);
        //create a byte array to receive the output.
        return byteArrayOutputStream.toByteArray();
    }


    //static method to convert IMG to Base64
    public static String imageToBase64(Bitmap bitmap) {
//        //define an output stream
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        //compress img
//        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
//        //create a byte array to receive the output.
        byte[] buffer = imageToByteArray(bitmap);
        String baseStr = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            baseStr = ByteArray2Base64(buffer);
        }
        return baseStr;
    }

    //static method to convert byte array to IMG
    public static Bitmap byteArray2Img(byte[] bytes) {
        return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
    }

    //static method to convert Base64 to IMG
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static Bitmap base64ToImage(String bitmap64){
        return byteArray2Img(Base64ToByteArray(bitmap64));
    }
}
