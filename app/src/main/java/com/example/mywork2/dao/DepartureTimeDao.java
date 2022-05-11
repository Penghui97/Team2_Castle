package com.example.mywork2.dao;

import com.example.mywork2.Util.DBUtil;
import com.example.mywork2.domain.DepartureTime;
import com.example.mywork2.domain.Time;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author Jing
 * function: this class is for get the depart time of the buses or the trains
 * at the particular route
 */
public class DepartureTimeDao {

    public DepartureTime getDepartureTimeByRouteId(String routeId, String arriveTime){
        String sql = "select * from Departures where routeId = ? order by depNo";
        DepartureTime res = null;
        Connection connection = DBUtil.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, routeId);
            rs = ps.executeQuery();
            ArrayList<DepartureTime> list = new ArrayList<>();
            while (rs.next()) {
                int depNo = rs.getInt("depNo");
                String depTime = rs.getString("depTime");
                list.add(new DepartureTime(routeId, depNo, depTime));
            }
            for(DepartureTime departureTime : list){
                if(biggerThan(departureTime.getDepTime(), arriveTime)){
                    res = departureTime;
                    break;
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            DBUtil.close(connection, ps, rs);
        }
        return res;
    }

    public boolean biggerThan(String strTime1, String strTime2){
        Time time1 = new Time(strTime1);
        Time time2 = new Time(strTime2);
        if(time1.getHour() > time2.getHour()){
            return true;
        }else if(time1.getHour() < time2.getHour()){
            return false;
        }else{
            if(time1.getMinute() > time2.getMinute()) return true;
            else return false;
        }
    }
}
