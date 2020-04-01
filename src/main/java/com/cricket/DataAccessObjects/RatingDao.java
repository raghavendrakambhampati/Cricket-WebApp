package com.cricket.DataAccessObjects;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RatingDao {
    String url;
    String username;
    String password;
    private static Connection con;

    public RatingDao() {

        try {
            createConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createConnection() throws SQLException {

        url = "jdbc:mysql://localhost:3306/taskdb";
        username = "dileep";
        password = "Dileep@123";
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        con = DriverManager.getConnection(url, username, password);
    }

    public void updateRating(int userId, int rating) {
        try {
            PreparedStatement ps = con.prepareStatement("update USER_RATING set RATING='" + rating + "'where USER_ID='" + userId + "'");
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
