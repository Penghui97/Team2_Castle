package com.example.mywork2.Util;

import com.example.mywork2.domain.User;

/**
 * this class is for storing the current user object
 */
public class UserThreadLocal {
    private UserThreadLocal(){}

    private static final ThreadLocal<User> LOCAL = new ThreadLocal<>();

    public static void put(User sysUser){
        LOCAL.set(sysUser);
    }

    public static User get(){
        return LOCAL.get();
    }

    public static void remove(){
        LOCAL.remove();
    }

}