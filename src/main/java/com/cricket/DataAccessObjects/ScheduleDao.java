package com.cricket.DataAccessObjects;

import com.cricket.models.Schedule;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ScheduleDao {

    String url;
    String username;
    String password;
    private static Connection con;
    public ScheduleDao() {

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

    public void insertSchedule(int numberOfMatchesPerDay, String date) {
        List<Integer> listOfTeamIds = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("select USER_ID from TEAM");
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                listOfTeamIds.add(resultSet.getInt("USER_ID"));
            }
            int matchId = 1;
            System.out.println(listOfTeamIds);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(simpleDateFormat.parse(date));
            if (listOfTeamIds.size() % 2 != 0) {
                for (int i = 0; i < listOfTeamIds.size() * 2; i = i + 2) {
                    createSchedule(new Schedule(matchId,listOfTeamIds.get(i % listOfTeamIds.size()),listOfTeamIds.get((i + 1) % listOfTeamIds.size()),Date.valueOf(date)));
                    calendar.setTime(simpleDateFormat.parse(date));
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                    date = simpleDateFormat.format(calendar.getTime());
                    matchId += 1;
                }
            } else {
                for (int i = 0; i < listOfTeamIds.size(); i = i + 2) {
                    createSchedule(new Schedule(matchId,listOfTeamIds.get(i % listOfTeamIds.size()),listOfTeamIds.get((i + 1) % listOfTeamIds.size()),Date.valueOf(date)));
                    calendar.setTime(simpleDateFormat.parse(date));
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                    date = simpleDateFormat.format(calendar.getTime());
                    matchId += 1;
                }
                int terminateLoop = 2;
                for (int i = 0; terminateLoop < listOfTeamIds.size(); i++) {
                    createSchedule(new Schedule(matchId,listOfTeamIds.get(i),listOfTeamIds.get(terminateLoop),Date.valueOf(date)));
                    terminateLoop += 1;
                    calendar.setTime(simpleDateFormat.parse(date));
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                    date = simpleDateFormat.format(calendar.getTime());
                    matchId += 1;
                }
            }
        } catch (SQLException | ParseException e) {
            e.printStackTrace();
        }
    }

    public void  createSchedule(Schedule schedule){
        try {
            PreparedStatement preparedStatement = con.prepareStatement("insert into SCHEDULE values(?,?,?,?)");
            preparedStatement.setInt(1, schedule.getMatchId());
            preparedStatement.setInt(2, schedule.getTeam1Id());
            preparedStatement.setInt(3, schedule.getTeam2Id());
            preparedStatement.setDate(4, schedule.getMatchDate());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public JSONArray getSchedule() {
        JSONArray jsonArray=null;
        try{
            PreparedStatement preparedStatement = con.prepareStatement("select MATCH_ID,TEAM_NAME AS TEAM1_NAME,MATCH_DATE from SCHEDULE JOIN TEAM on SCHEDULE.TEAM1_ID=TEAM.USER_ID");
            PreparedStatement preparedStatement1 = con.prepareStatement("select TEAM_NAME AS TEAM2_NAME from SCHEDULE JOIN TEAM on SCHEDULE.TEAM2_ID=TEAM.USER_ID");
            ResultSet resultSet = preparedStatement.executeQuery();
            ResultSet resultSet1 = preparedStatement1.executeQuery();
            jsonArray = generateSchedule(resultSet,resultSet1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jsonArray;
    }

    private JSONArray generateSchedule(ResultSet resultSet, ResultSet resultSet1) {
        JSONArray jsonArray = new JSONArray();
        try{
            while(resultSet.next() && resultSet1.next()){
                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.put("MATCH_ID",resultSet.getInt("MATCH_ID"));
                jsonObject1.put("Team1_Name",resultSet.getString("TEAM1_NAME"));
                jsonObject1.put("Team2_Name",resultSet1.getString("TEAM2_NAME"));
                jsonObject1.put("Match_Date",resultSet.getDate("MATCH_DATE"));
                jsonArray.put(jsonObject1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jsonArray;
    }

    public JSONObject getTodayMatchTeams(String date) {
        JSONObject jsonObject = new JSONObject();
        try {
            PreparedStatement preparedStatement = con.prepareStatement("select TEAM_NAME,USER_ID from TEAM where USER_ID=(select TEAM1_ID from SCHEDULE where MATCH_DATE='" + date + "')");
            ResultSet resultSet = preparedStatement.executeQuery();
            JSONArray jsonArray = new JSONArray();
            while (resultSet.next()) {
                JSONObject record = new JSONObject();
                record.put("TeamName", resultSet.getString("TEAM_NAME"));
                record.put("TeamId", resultSet.getString("USER_ID"));
                jsonArray.put(record);
            }
            PreparedStatement preparedStatement1 = con.prepareStatement("select TEAM_NAME,USER_ID from TEAM where USER_ID=(select TEAM2_ID from SCHEDULE where MATCH_DATE='" + date + "')");
            ResultSet resultSet1 = preparedStatement1.executeQuery();
            while (resultSet1.next()) {
                JSONObject record = new JSONObject();
                record.put("TeamName", resultSet1.getString("TEAM_NAME"));
                record.put("TeamId", resultSet1.getString("USER_ID"));
                jsonArray.put(record);
            }
            jsonObject.put("Todays_Teams", jsonArray);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
