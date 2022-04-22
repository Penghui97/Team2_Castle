package com.example.mywork2.dao;

import com.example.mywork2.Util.DBUtil;
import com.example.mywork2.domain.Comment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CommentDao {

    //get all comments from the database
    public ArrayList<Comment> getAllComments(){
        String sql = "select * from comment";
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
                String time = rs.getString("time");
                comments.add(new Comment(commentId, username, rating, content, time));
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
        String sql = "select * from comment where username=?";
        ArrayList<Comment> comments = new ArrayList<>();
        Connection connection = DBUtil.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            ps.setString(1, username);
            while(rs.next()){
                String commentId = rs.getString("commentId");
                int rating = rs.getInt("rating");
                String content = rs.getString("content");
                String time = rs.getString("time");
                comments.add(new Comment(commentId, username, rating, content, time));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            DBUtil.close(connection, ps, rs);
        }
        return comments;
    }

    public void removeCommentById(String commentId){
//        String sql = "select * from comment where username=?";
//        ArrayList<Comment> comments = new ArrayList<>();
//        Connection connection = DBUtil.getConnection();
//        PreparedStatement ps = null;
//        ResultSet rs = null;
//        try {
//            ps = connection.prepareStatement(sql);
//            rs = ps.executeQuery();
//            ps.setString(1, username);
//            while(rs.next()){
//                String commentId = rs.getString("commentId");
//                int rating = rs.getInt("rating");
//                String content = rs.getString("content");
//                String time = rs.getString("time");
//                comments.add(new Comment(commentId, username, rating, content, time));
//            }
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }finally {
//            DBUtil.close(connection, ps, rs);
//        }
//        return comments;
    }
}
