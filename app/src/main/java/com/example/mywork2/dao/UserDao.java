package com.example.mywork2.dao;

import com.example.mywork2.Util.*;
import com.example.mywork2.domain.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
/**
 * @author Jing
 * function: used for get particular data from user table in database
 */
public class UserDao {


    // get a particular user by his username
    // if no user in the database has this username
    // return null
    public User getUserByUsername(String username){
        String sql = "select * from Users where username = ?";
        User user = null;
        Connection connection = DBUtil.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, username);
            rs = ps.executeQuery();
            while(rs.next()){
                String nickname = rs.getString("nickname");
                String email = rs.getString("email");
                String password = rs.getString("password");
                user = new User(username, nickname, email, password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            //close the resources
            DBUtil.close(connection, ps, rs);
        }
        return user;
    }

    // get a particular user by his email
    // if no user in the database has this email
    // return null
    public User getUserByEmail(String email){
        String sql = "select * from Users where email = ?";
        User user = null;
        Connection connection = DBUtil.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, email);
            rs = ps.executeQuery();
            while(rs.next()){
                String username = rs.getString("username");
                String nickname = rs.getString("nickname");
                String password = rs.getString("password");
                user = new User(username, nickname, email, password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            //close the resources
            DBUtil.close(connection, ps, rs);
        }
        return user;
    }

    //if the username of this user has existed in the database
    //the result will be 1
    //if the email exist in the database
    //the result will be 2
    //if the user is added successfully
    //the result will be 0
    //if there is something wrong about the code
    //the result will be -1
    public int addUser(User user){
        //check the user by his username
        //if he exists return 1
        if(getUserByUsername(user.getUsername()) != null) return 1;
        //check the user by his email
        //if he exists return 2
        if(getUserByEmail(user.getEmail()) != null) return 2;
        //add the new user
        String sql = "insert into Users(username, nickname, email, password) values(?, ?, ?, ?)";
        int res = -1;
        Connection connection = DBUtil.getConnection();
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getNickname());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPassword());
            res = ps.executeUpdate() == 1 ? 0 : -1;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            //close the resources
            DBUtil.close(connection, ps, null);
        }
        return res;
    }

    //delete a user by his username
    //if the username exists in the database
    //delete it return true
    //if it doesn't exist return false
    public boolean deleteUserByUsername(String username){
        if(getUserByUsername(username) == null) return false;
        String sql = "delete from Users where username = ?";
        boolean res = false;
        Connection connection = DBUtil.getConnection();
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, username);
            res = ps.executeUpdate() == 1 ? true : false;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            //close the resources
            DBUtil.close(connection, ps, null);
        }
        return res;
    }

    //the new information could be written in the newUser object as parameter
    // ** however the username can't be changed **

    //if the username doesn't exit
    //return 1
    //if the new email has been used
    //return 2
    //if it exists the old data will be override by the parameter
    //and return 0
    //if there is something wrong with the code
    //return -1
    public int updateUser(User newUser){
        User oldUser = getUserByUsername(newUser.getUsername());
        if(oldUser == null) return 1;
        //if the email is changed
        if(!oldUser.getEmail().equals(newUser.getEmail())){
            if(getUserByEmail(newUser.getEmail()) != null) return 2;
        }
        String sql = "update Users set nickname = ?,email = ?,password = ? where username = ?";
        int res = -1;
        Connection connection = DBUtil.getConnection();
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, newUser.getNickname());
            ps.setString(2, newUser.getEmail());
            ps.setString(3, newUser.getPassword());
            ps.setString(4, newUser.getUsername());
            res = ps.executeUpdate() == 1 ? 0 : -1;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            //close the resources
            DBUtil.close(connection, ps, null);
        }
        return res;
    }
}