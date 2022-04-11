package com.example.mywork2.dao;

import com.example.mywork2.Util.*;
import com.example.mywork2.domain.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class JourneyDao {
    //to get the routes of this journey
    private RouteDao routeDao = new RouteDao();
    //to get the castle of this journey
    private CastleDao castleDao = new CastleDao();

    //get all journeys by the castle's name
    public ArrayList<Journey> getJourneysByCastleName(String castleName){
        String sql = "select * from Journeys where castleName = ?";
        ArrayList<Journey> journeys = new ArrayList<>();
        Connection connection = DBUtil.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, castleName);
            rs = ps.executeQuery();
            while (rs.next()) {
                String journeyId = rs.getString("journeyId");
                Castle castle = castleDao.getCastleByName(castleName);
                ArrayList<Route> routes = routeDao.getRoutesByJourneyId(journeyId, false);
                ArrayList<Route> returnRoutes = routeDao.getRoutesByJourneyId(journeyId, true);
                journeys.add(new Journey(journeyId, castle, routes, returnRoutes));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            //close the resources
            DBUtil.close(connection, ps, rs);
        }
        return journeys;
    }

    //get the particular journey by its id
    public Journey getJourneyById(String journeyId){
        String sql = "select * from Journeys where journeyId = ?";
        Journey journey = null;
        Connection connection = DBUtil.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, journeyId);
            rs = ps.executeQuery();
            while (rs.next()) {
                String castleName = rs.getString("castleName");
                Castle castle = castleDao.getCastleByName(castleName);
                ArrayList<Route> routes = routeDao.getRoutesByJourneyId(journeyId, false);
                ArrayList<Route> returnRoutes = routeDao.getRoutesByJourneyId(journeyId, true);
                journey = new Journey(journeyId, castle, routes, returnRoutes);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            //close the resources
            DBUtil.close(connection, ps, rs);
        }
        return journey;
    }
}
