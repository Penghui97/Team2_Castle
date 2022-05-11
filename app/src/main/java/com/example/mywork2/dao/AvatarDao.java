package com.example.mywork2.dao;

import com.example.mywork2.Util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Jing
 * function: used for get particular data from avatar table in database
 */
public class AvatarDao {

    //add a new avatar into database
    //or overload the old avatar
    public void addAvatar(String username, byte[] avatar) {
        String sql = "";
        byte[] oldAvatar = getAvatarByUsername(username);
        if (oldAvatar == null || oldAvatar.length == 0) {
            //if the user don't have a avatar
            sql = "insert into Avatars(username, image) values(?,?)";
            Connection connection = DBUtil.getConnection();
            PreparedStatement ps = null;
            try {
                ps = connection.prepareStatement(sql);
                ps.setString(1, username);
                ps.setBytes(2, avatar);
                int i = ps.executeUpdate();
                int k = i;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                DBUtil.close(connection, ps, null);
            }
        } else {
            //if the user has a old avatar
            sql = "update Avatars set image = ? where username = ?";
            Connection connection = DBUtil.getConnection();
            PreparedStatement ps = null;
            try {
                ps = connection.prepareStatement(sql);
                ps.setBytes(1, avatar);
                ps.setString(2, username);
                int i = ps.executeUpdate();
                int k = i;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                DBUtil.close(connection, ps, null);
            }
        }
    }

    //get the avatar by username
    public byte[] getAvatarByUsername(String username) {
        String sql = "select * from Avatars where username=?";
        Connection connection = DBUtil.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        byte[] bytes = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, username);
            rs = ps.executeQuery();
            while (rs.next()) {
                bytes = rs.getBytes("image");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            DBUtil.close(connection, ps, rs);
        }
        return bytes;
    }
}
