package com.example.mywork2.dao;

import com.example.mywork2.Util.*;
import com.example.mywork2.domain.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * @author Jing
 * function: used for get particular data from transport table in database
 */
public class TransportDao {

    //get a particular transport by its id
    public Transport getTransportById(String transportId){
        String sql = "select * from Transport where transportId = ?";
        Transport transport = null;
        Connection connection = DBUtil.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, transportId);
            rs = ps.executeQuery();
            while(rs.next()){
                String operator = rs.getString("operator");
                String type = rs.getString("type");
                transport = new Transport(transportId, operator, type);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            //close the resources
            DBUtil.close(connection, ps, rs);
        }
        return transport;
    }
}
