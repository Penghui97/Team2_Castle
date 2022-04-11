package com.example.mywork2.dao;

import com.example.mywork2.Util.*;
import com.example.mywork2.domain.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserDao {
    //to get the user's tickets
    private TicketDao ticketDao = new TicketDao();

    // get a particular user by his username
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
                String profileId = rs.getString("profileId");
                String nickname = rs.getString("nickname");
                String email = rs.getString("email");
                String password = rs.getString("password");
                ArrayList<Ticket> tickets = ticketDao.getTicketsByUsername(username);
                user = new User(username, profileId, nickname, email, password, tickets);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            //close the resources
            DBUtil.close(connection, ps, rs);
        }
        return user;
    }
}
