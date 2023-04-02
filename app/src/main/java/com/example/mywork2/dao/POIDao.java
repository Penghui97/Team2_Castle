package com.example.mywork2.dao;

import com.example.mywork2.Util.*;
import com.example.mywork2.domain.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
public class POIDao {

    //get the pois by the castle name
    public ArrayList<NearbyPOI> getPOIsByCastleName(String castleName){
        String sql = "select * from NearbyPOI where castleName = ? order by rating desc";
        ArrayList<NearbyPOI> nearbyPOIs = new ArrayList<>();
        Connection connection = DBUtil.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, castleName);
            rs = ps.executeQuery();
            while(rs.next()){
                String poiName = rs.getString("poiName");
                String category = rs.getString("category");
                float rating = rs.getFloat("rating");
                String postcode = rs.getString("postcode");
                nearbyPOIs.add(new NearbyPOI(castleName, poiName, category, rating, postcode));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            //close the resources
            DBUtil.close(connection, ps, rs);
        }
        return nearbyPOIs;
    }
}
