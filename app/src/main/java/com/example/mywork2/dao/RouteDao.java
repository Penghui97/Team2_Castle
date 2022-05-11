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
 * function: used for get particular data from route table in database
 */
public class RouteDao {
    //to get the transport
    private TransportDao transportDao = new TransportDao();

    //get the routes by the journey id
    public ArrayList<Route> getRoutesByJourneyId(String journeyId, boolean isReturn){
        String sql = "select * from Route where journeyId = ? and isReturn = ? order by led";
        ArrayList<Route> routes = new ArrayList<>();
        Connection connection = DBUtil.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, journeyId);
            //if the route is return the 'isReturn' is 1
            //otherwise it is 0
            if(isReturn){
                ps.setString(2, "1");
            }else{
                ps.setString(2, "0");
            }
            rs = ps.executeQuery();
            while(rs.next()){
                Transport transport = transportDao.getTransportById(rs.getString("transportId"));
                String routeId = rs.getString("routeId");
                String start = rs.getString("start");
                String stop = rs.getString("stop");
                int duration = rs.getInt("duration");
                int price = rs.getInt("price");
                int leg = rs.getInt("led");
                routes.add(new Route(routeId, journeyId, transport, start, stop, duration, price, leg));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            //close the resources
            DBUtil.close(connection, ps, rs);
        }
        return routes;
    }
}
