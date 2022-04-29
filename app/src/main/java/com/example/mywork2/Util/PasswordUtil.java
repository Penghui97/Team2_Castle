package com.example.mywork2.Util;

public class PasswordUtil {
    //convert string to hex
    //Penghui Xiao, reference from https://blog.csdn.net/qq_29752857/article/details/118220714
    public static String str2Hex(String string) {
        char[] chars = "0123456789ABCDEF".toCharArray();
        StringBuilder sBuilder = new StringBuilder("");
        byte[] bs = string.getBytes();
        int bit;
        for (byte b : bs) {
            bit = (b & 0x0f0) >> 4;
            sBuilder.append(chars[bit]);
            bit = b & 0x0f;
            sBuilder.append(chars[bit]);

        }
        return sBuilder.toString().trim();
    }

    //convert hex to string
    //Penghui Xiao, reference from https://blog.csdn.net/qq_29752857/article/details/118220714
    public static String hex2Str(String hex) {
        String string = "0123456789ABCDEF";
        char[] hexs = hex.toCharArray();
        byte[] bytes = new byte[hex.length()/2];
        int n;
        for(int i =0;i<bytes.length;i++) {
            n=string.indexOf(hexs[2*i])*16;
            n+=string.indexOf(hexs[2*i+1]);
            bytes[i] = (byte) (n & 0xff);
        }
        return new String(bytes);
    }
}
