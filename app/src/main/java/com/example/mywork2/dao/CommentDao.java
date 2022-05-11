package com.example.mywork2.dao;

import com.example.mywork2.Util.DBUtil;
import com.example.mywork2.domain.Comment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
/**
 * @author Jing
 * function: used for get particular data from comment table in database
 */
public class CommentDao {
    private AvatarDao avatarDao = new AvatarDao();

    //get all comments from the database
    public ArrayList<Comment> getAllComments(){
        String sql = "select * from Comments order by commentId desc";
        ArrayList<Comment> comments = new ArrayList<>();
        Connection connection = DBUtil.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()){
                String commentId = rs.getString("commentId");
                String username = rs.getString("username");
                int rating = rs.getInt("rating");
                String content = rs.getString("content");
                String time = rs.getString("date");
                byte[] avatar = avatarDao.getAvatarByUsername(username);
                comments.add(new Comment(avatar, commentId, username, rating, content, time));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            DBUtil.close(connection, ps, rs);
        }
        return comments;
    }

    //get comments by username
    public ArrayList<Comment> getCommentsByUsername(String username){
        String sql = "select * from Comments where username=? order by commentId desc";
        ArrayList<Comment> comments = new ArrayList<>();
        Connection connection = DBUtil.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, username);
            rs = ps.executeQuery();
            while(rs.next()){
                String commentId = rs.getString("commentId");
                int rating = rs.getInt("rating");
                String content = rs.getString("content");
                String time = rs.getString("date");
                byte[] avatar = avatarDao.getAvatarByUsername(username);
                comments.add(new Comment(avatar, commentId, username, rating, content, time));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            DBUtil.close(connection, ps, rs);
        }
        return comments;
    }

    //get others' comments except user
    public ArrayList<Comment> getCommentsExceptUsername(String username){
        String sql = "select * from Comments where username!=? order by commentId desc";
        ArrayList<Comment> comments = new ArrayList<>();
        Connection connection = DBUtil.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, username);
            rs = ps.executeQuery();
            while(rs.next()){
                String commentId = rs.getString("commentId");
                String thisUsername = rs.getString("username");
                int rating = rs.getInt("rating");
                String content = rs.getString("content");
                String time = rs.getString("date");
                byte[] avatar = avatarDao.getAvatarByUsername(thisUsername);
                comments.add(new Comment(avatar, commentId, thisUsername, rating, content, time));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            DBUtil.close(connection, ps, rs);
        }
        return comments;
    }

    //add a comment
    //return true if the insert successfully
    //return false if something wrong happened in the database
    public boolean addComment(Comment comment){
        String sql = "insert into Comments(commentId, username, rating, content, date) values (?, ?, ?, ?, ?)";
        Connection connection = DBUtil.getConnection();
        PreparedStatement ps = null;
        boolean result = false;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, comment.getCommentId());
            ps.setString(2, comment.getUsername());
            ps.setInt(3, comment.getRating());
            ps.setString(4, comment.getContent());
            ps.setString(5, comment.getTime());
            result = ps.executeUpdate() == 1 ? true : false;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            DBUtil.close(connection, ps, null);
        }
        return result;
    }

    //remove a comment from the database by its id
    public void removeCommentById(String commentId){
        String sql = "delete from Comments where commentId=?";
        Connection connection = DBUtil.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, commentId);
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            DBUtil.close(connection, ps, rs);
        }
    }

    //get all comments
}
