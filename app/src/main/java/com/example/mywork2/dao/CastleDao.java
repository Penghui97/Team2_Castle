package com.example.mywork2.dao;

import com.example.mywork2.Util.*;
import com.example.mywork2.domain.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CastleDao {
    //to get the poi of this castle
    private POIDao poiDao = new POIDao();

    //get all the castles from the database
    public ArrayList<Castle> getAllCastles(){
        String sql = "select * from Castles";
        ArrayList<Castle> castles = new ArrayList<>();
        Connection connection = DBUtil.getConnection();

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                String castleName = rs.getString("castleName");
                String postcode = rs.getString("postcode");
                int adultPrice = rs.getInt("adultPrice");
                int kidsPrice = rs.getInt("kidsPrice");

                ArrayList<NearbyPOI> pois = poiDao.getPOIsByCastleName(castleName);

                castles.add(new Castle(castleName, postcode, adultPrice, kidsPrice, pois));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return castles;
    }


    //get a particular castle by its name
    public Castle getCastleByName(String name){
        String sql = "select * from Castles where castleName = ?";
        Castle castle = null;
        Connection connection = DBUtil.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, name);
            rs = ps.executeQuery();
            while(rs.next()){
                String castleName = rs.getString("castleName");
                String postcode = rs.getString("postcode");
                int adultPrice = rs.getInt("adultPrice");
                int kidsPrice = rs.getInt("kidsPrice");

                ArrayList<NearbyPOI> pois = poiDao.getPOIsByCastleName(castleName);
                castle = new Castle(castleName, postcode, adultPrice, kidsPrice, pois);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            //close the resources
            DBUtil.close(connection, ps, rs);
        }
        return castle;
    }

}
