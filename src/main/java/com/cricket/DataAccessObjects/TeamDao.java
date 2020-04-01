package com.cricket.DataAccessObjects;

import com.cricket.models.Team;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.*;

public class TeamDao {

    String url;
    String username;
    String password;
    private static Connection con;

    public TeamDao() {

        try {
            getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void getConnection() throws SQLException {

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


    public boolean insertTeam(Team teamDetails) {
        try {
            PreparedStatement preparedStatement = con.prepareStatement
                    ("insert into TEAM values(?,?,?)");
            preparedStatement.setString(1, teamDetails.getTeamName());
            preparedStatement.setString(2, teamDetails.getCity());
            preparedStatement.setInt(3, teamDetails.getUserId());
            int i = preparedStatement.executeUpdate();
            return i > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public JSONObject getTeams() {
        ResultSet resultSet = null;
        JSONObject jsonObject = new JSONObject();
        try {
            PreparedStatement ps = con.prepareStatement("select * from TEAM");
            resultSet = ps.executeQuery();

            JSONArray jsonArray = new JSONArray();

            while (resultSet.next()) {
                JSONObject record = new JSONObject();
                record.put("TeamName", resultSet.getString("TEAM_NAME"));
                record.put("city", resultSet.getString("TEAM_CITY"));
                record.put("UserId", resultSet.getInt("USER_ID"));
                jsonArray.put(record);
            }

            jsonObject.put("Teams_data", jsonArray);
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
            PreparedStatement ps = con.prepareStatement("select USER.USER_ID,USER_NAME,USER_MOBILENUMBER,USER_EMAIL,USER_SKILLS,USER_TEAM,USER_RATING.RATING from USER join USER_RATING on USER.USER_ID=USER_RATING.USER_ID where USER_TEAM=(select TEAM_NAME from TEAM where USER_ID=" + userId + ") and USER_ROLE='Player'");
            resultSet = ps.executeQuery();

            JSONArray jsonArray = new JSONArray();

            while (resultSet.next()) {
                JSONObject record = new JSONObject();
                record.put("UserId", resultSet.getInt("USER_ID"));
                record.put("Name", resultSet.getString("USER_NAME"));
                record.put("MobileNumber", resultSet.getInt("USER_MOBILENUMBER"));
                record.put("EmailId", resultSet.getString("USER_EMAIL"));
                record.put("Skills", resultSet.getString("USER_SKILLS"));
                record.put("Rating", resultSet.getString("RATING"));
                jsonArray.put(record);
            }

            jsonObject.put("OwnerTeam_data", jsonArray);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
