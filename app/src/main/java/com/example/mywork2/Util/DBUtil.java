package com.example.mywork2.Util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * this class is for connecting the remote database
 * using jdbc
 */
public class DBUtil {

    //the ip is from cs-db.ncl.ac.uk
    //note: we need to use the campus network(USB WIFI) to connect the remote database
//    private static String url = "jdbc:mysql://10.3.224.183:3306/csc8019_team02";
//    private static String user = "csc8019_team02";
//    private static String password = "Vex,Fur,;Box";

    //local test ip

    private static String url = "jdbc:mysql://10.0.2.2:3306/csc8019_team02";
    private static String user = "root";
    private static String password = "333";

    //load the driver
    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    //connect the Java with Mysql
    //and return the connection
    public static Connection getConnection(){
        //connect the database
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    //close the three resources
    public static void close(Connection connection, Statement statement, ResultSet resultSet){
        //close the connection
        if(connection != null){
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        //close the statement
        if(statement != null){
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        //close the resultSet
        if(resultSet != null){
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
