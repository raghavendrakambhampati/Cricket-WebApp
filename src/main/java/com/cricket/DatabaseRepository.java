package com.cricket;

import com.cricket.models.User;

import javax.servlet.http.HttpServlet;
import java.sql.*;

class DatabaseRepository extends HttpServlet {
    public String verifyLogin(int userId, String password) {
        User user = null;
        String role = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection
                    ("jdbc:mysql://localhost:3306/taskdb", "dileep", "Dileep@123");
            PreparedStatement stmt = con.prepareStatement("select USER_ROLE from USER where USER_ID='" + userId + "' AND USER_PASSWORD='" + password + "'");
            ResultSet resultSet = stmt.executeQuery();
            //System.out.println(resultSet);
            while (resultSet.next()) {
                role = resultSet.getString(1);
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return role;
    }

}