package com.example.mywork2.dao;

import com.example.mywork2.Util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AvatarDao {

    //add a new avatar into database
    //or overload the old avatar
    public void addAvatar(String username, byte[] avatar){
        //if the user don't have a avatar
        String sql = "insert into Avatars(image, username) values(?,?)";
        //if the user has a old avatar
        byte[] oldAvatar = getAvatarByUsername(username);
        if(oldAvatar == null || oldAvatar.length == 0){
            sql = "update Avatars set image = ? where username = ?";
        }
        Connection connection = DBUtil.getConnection();
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setBytes(1, avatar);
            ps.setString(2, username);
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            DBUtil.close(connection, ps, null);
        }
    }

    //get the avatar by username
    public byte[] getAvatarByUsername(String username){
        String sql = "select * from Avatars where username=?";
        Connection connection = DBUtil.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        byte[] bytes = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, username);
            rs = ps.executeQuery();
            rs.next();
            bytes = rs.getBytes("image");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            DBUtil.close(connection, ps, rs);
        }
        return bytes;
    }
}
