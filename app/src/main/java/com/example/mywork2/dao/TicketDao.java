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
                int quantity = rs.getInt("quantity");
                int totalPrice = rs.getInt("totalPrice");
                boolean isPaid = rs.getInt("isPaid") != 0;
                tickets.add(new Ticket(ticketId, castleName, username, journeyId, date, time, returnTime, quantity, totalPrice, isPaid));
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
        String sql = "insert into Tickets(ticketId, castleName, username, journeyId, date, time, returnTime, quantity, totalPrice, isPaid) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
            ps.setInt(8, ticket.getQuantity());
            ps.setInt(9, ticket.getTotalPrice());
            ps.setInt(10, ticket.isPaid() ? 1 : 0);
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
                int quantity = rs.getInt("quantity");
                int totalPrice = rs.getInt("totalPrice");
                boolean isPaid = rs.getInt("isPaid") == 0 ? false : true;
                ticket = new Ticket(ticketId, castleName, userName, journeyId, date, time, returnTime, quantity, totalPrice, isPaid);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            DBUtil.close(connection, ps, rs);
        }
        return ticket;
    }

    //delete the one ticket by its id
    public void removeAllTicketsById(String ticketId){
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

    //remove particular ticket number
    //if left num is 0
    //remove all the ticket
    //if left num is negative num
    //return -1
    //if left num is positive num
    //return the left num
    public int removeTicketsById(String ticketId, int num){
        String sql = "update Tickets set quantity=?,totalPrice=? where ticketId =?";
        //check the num
        Ticket ticket = getTicketById(ticketId);
        int leftNum = ticket.removePartTickets(num);
        if(leftNum == 0) {
            removeAllTicketsById(ticketId);
            return 0;
        }
        if(leftNum < 0){
            return -1;
        }
        //change the ticket num in the database
        Connection connection = DBUtil.getConnection();
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, leftNum + "");
            ps.setString(2, ticket.getTotalPrice() + "");
            ps.setString(3, ticketId);
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            DBUtil.close(connection, ps, null);
        }
        return leftNum;
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
