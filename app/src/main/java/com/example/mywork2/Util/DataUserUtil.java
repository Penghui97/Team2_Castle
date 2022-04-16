package com.example.mywork2.Util;

public class DataUserUtil {
    String[] userNames = new String[200];
    int size1 = 0;
    String [] emails = new String[200];
    int size2 = 0;
    String[] passwords = new String[200];
    int size3 = 0;

    //function to register
    public void register(String userName, String email, String password){
        userNames[size1++] = userName;
        emails[size2++] = email;
        passwords[size3++] = password;

    }

    //function to login
    public boolean login(String userName_or_email, String password) {

        for (int i = 0; i<size1; i++){
            //check if the username or email is right
            if(userNames[i].equals(userName_or_email) || emails[i].equals(userName_or_email))
                return passwords[i].equals(password);
        }
        return false;
    }

    private DataUserUtil(){}

    private static DataUserUtil instance;

    public static DataUserUtil getInstance() {
        if(instance == null)
            instance = new DataUserUtil();
        return instance;
    }


}
