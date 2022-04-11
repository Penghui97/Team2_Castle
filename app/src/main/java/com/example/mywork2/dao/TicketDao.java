package com.example.mywork2.dao;

import com.example.mywork2.Util.*;
import com.example.mywork2.domain.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TicketDao {

    //get the user's tickets by his username
    public ArrayList<Ticket> getTicketsByUsername(String username){
        String sql = "select * from Tickets where username = ?";
        ArrayList<Ticket> tickets = new ArrayList<>();
        Connection connection = DBUtil.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, username);
            rs = ps.executeQuery();
            while(rs.next()){
                String ticketId = rs.getString("ticketId");
                String castleName = rs.getString("castleName");
                String journeyId = rs.getString("journeyId");
                String date = rs.getString("date");
                String time = rs.getString("time");
                String returnTime = rs.getString("returnTime");
                int adultQuantity = rs.getInt("adultQuantity");
                int kidsQuantity = rs.getInt("kidsQuantity");
                int totalPrice = rs.getInt("totalPrice");
                boolean isPaid = rs.getInt("isPaid") == 0 ? false : true;
                tickets.add(new Ticket(ticketId, castleName, username, journeyId, date, time, returnTime, adultQuantity, kidsQuantity, totalPrice, isPaid));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            //close the resources
            DBUtil.close(connection, ps, rs);
        }
        return tickets;
    }

    //add a new ticket into the database
    public void addTicket(Ticket ticket){
        String sql = "insert into Tickets(ticketId, castleName, username, journeyId, date, time, returnTime, adultQuantity, kidsQuantity, totalPrice, isPaid) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection connection = DBUtil.getConnection();
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, ticket.getTicketId());
            ps.setString(2, ticket.getCastleName());
            ps.setString(3, ticket.getUsername());
            ps.setString(4, ticket.getJourneyId());
            ps.setString(5, ticket.getDate());
            ps.setString(6, ticket.getTime());
            ps.setString(7, ticket.getReturnTime());
            ps.setInt(8, ticket.getAdultQuantity());
            ps.setInt(9, ticket.getKidsQuantity());
            ps.setInt(10, ticket.getTotalPrice());
            ps.setInt(11, ticket.isPaid() ? 1 : 0);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            //close the resources
            DBUtil.close(connection, ps, null);
        }
    }

    //get a particular ticket by its id
    public Ticket getTicketById(String ticketId){
        String sql = "select * from Tickets where ticketId = ?";
        Ticket ticket = null;
        Connection connection = DBUtil.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, ticketId);
            rs = ps.executeQuery();
            while(rs.next()){
                String castleName = rs.getString("castleName");
                String userName = rs.getString("username");
                String journeyId = rs.getString("journeyId");
                String date = rs.getString("date");
                String time = rs.getString("time");
                String returnTime = rs.getString("returnTime");
                int adultQuantity = rs.getInt("adultQuantity");
                int kidsQuantity = rs.getInt("kidsQuantity");
                int totalPrice = rs.getInt("totalPrice");
                boolean isPaid = rs.getInt("isPaid") == 0 ? false : true;
                ticket = new Ticket(ticketId, castleName, userName, journeyId, date, time, returnTime, adultQuantity, kidsQuantity, totalPrice, isPaid);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            DBUtil.close(connection, ps, rs);
        }
        return ticket;
    }

    //delete the one ticket by its id
    public void removeTicketById(String ticketId){
        String sql = "delete from Tickets where ticketId = ?";
        Connection connection = DBUtil.getConnection();
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, ticketId);
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            DBUtil.close(connection, ps, null);
        }
    }

    //change the paid status of this ticket
    public void payTicketById(String ticketId){
        String sql = "update Tickets set isPaid=1 where ticketId = ?";
        Connection connection = DBUtil.getConnection();
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, ticketId);
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            DBUtil.close(connection, ps, null);
        }
    }
}
