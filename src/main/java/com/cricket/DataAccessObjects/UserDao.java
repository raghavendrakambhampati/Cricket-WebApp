package com.cricket.DataAccessObjects;

import com.cricket.models.User;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.*;

public class UserDao {
    String url;
    String username;
    String password;
    private static Connection con;

    public UserDao() {

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

    public boolean insertUser(User user) {
        try {
            PreparedStatement ps = con.prepareStatement
                    ("insert into USER values(?,?,?,?,?,?,?,?,?)");
            int userId = user.getUserId();
            PreparedStatement preparedStatement = con.prepareStatement
                    ("insert into USER_RATING values(?,?)");
            ps.setString(1, user.getName());
            ps.setInt(2, user.getUserId());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPassword());
            ps.setString(5, user.getGender());
            ps.setInt(6, user.getMobileNumber());
            ps.setString(7, user.getRole());
            ps.setString(8, user.getSkills());
            ps.setString(9, null);
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, 0);
            int i = ps.executeUpdate();
            preparedStatement.executeUpdate();
            return i > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public JSONObject getAllUsers() {
        ResultSet resultSet = null;
        JSONObject jsonObject = new JSONObject();
        try {
            PreparedStatement ps = con.prepareStatement("select USER_ID,USER_NAME,USER_SKILLS from USER where USER_ROLE='Player'");
            resultSet = ps.executeQuery();
            JSONArray jsonArray = new JSONArray();
            while (resultSet.next()) {
                JSONObject record = new JSONObject();
                record.put("UserId", resultSet.getInt("USER_ID"));
                record.put("Name", resultSet.getString("USER_NAME"));
                record.put("Skills", resultSet.getString("USER_SKILLS"));
                jsonArray.put(record);
            }
            jsonObject.put("Players_Details", jsonArray);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public JSONObject getUser(int userId) {
        PreparedStatement ps = null;
        JSONObject jsonObject = null;
        try {
            ps = con.prepareStatement("select * from USER where USER_ID=" + userId);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                jsonObject = new JSONObject();
                jsonObject.put("UserId", resultSet.getInt("USER_ID"));
                jsonObject.put("Name", resultSet.getString("USER_NAME"));
                jsonObject.put("Email", resultSet.getString("USER_EMAIL"));
                jsonObject.put("Gender", resultSet.getString("USER_GENDER"));
                jsonObject.put("Skills", resultSet.getString("USER_SKILLS"));
                jsonObject.put("Team", resultSet.getString("USER_TEAM"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public JSONObject getTeam(int userId) {
        ResultSet resultSet = null;
        JSONObject jsonObject = new JSONObject();
        try {
            PreparedStatement ps = con.prepareStatement("select USER.USER_ID,USER_NAME,USER_MOBILENUMBER,USER_EMAIL,USER_SKILLS,USER_TEAM,USER_RATING.RATING from USER join USER_RATING on USER.USER_ID=USER_RATING.USER_ID where USER_TEAM=(select USER_TEAM from USER where USER_ID=" + userId + ") and USER_ROLE='Player'");
            resultSet = ps.executeQuery();

            JSONArray jsonArray = new JSONArray();

            while (resultSet.next()) {
                JSONObject record = new JSONObject();
                record.put("UserId", resultSet.getInt("USER.USER_ID"));
                record.put("Name", resultSet.getString("USER_NAME"));
                record.put("MobileNumber", resultSet.getInt("USER_MOBILENUMBER"));
                record.put("EmailId", resultSet.getString("USER_EMAIL"));
                record.put("Skills", resultSet.getString("USER_SKILLS"));
                record.put("Team", resultSet.getString("USER_TEAM"));
                record.put("Rating", resultSet.getInt("RATING"));
                jsonArray.put(record);
            }

            jsonObject.put("Players_Team", jsonArray);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
