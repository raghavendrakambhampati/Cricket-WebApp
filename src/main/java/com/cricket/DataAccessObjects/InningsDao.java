package com.cricket.DataAccessObjects;

import com.cricket.models.Innings;
import com.cricket.models.Match;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InningsDao {

    String url;
    String username;
    String password;
    private static Connection con;

    public InningsDao() {

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

    public boolean updateInnings(int firstInnings, int secondInnings) {
        List<Integer> list = new ArrayList<>();
        list.add(firstInnings);
        list.add(secondInnings);
        int insertionStatus=0;
        try {
            for (int i = 0; i < list.size(); i++) {
                Innings innings = new Innings(list.get(i),0,0);
                PreparedStatement preparedStatement = con.prepareStatement("insert into INNINGS (TEAM_ID,SCORE,WICKETS) values(?,?,?)");
                preparedStatement.setInt(1, innings.getTeamId());
                preparedStatement.setInt(2,innings.getScore());
                preparedStatement.setInt(3, innings.getWickets());
                insertionStatus = preparedStatement.executeUpdate();
            }
            PreparedStatement preparedStatement = con.prepareStatement("select ID from INNINGS where TEAM_ID between '"+firstInnings+"' and '"+secondInnings+"'");
            ResultSet rs = preparedStatement.executeQuery();
            List<Integer> inningsList = new ArrayList<>();
            int count=0;
            while(rs.next()){
                inningsList.add(count,rs.getInt(1));
                System.out.println(rs.getInt(1));
                count++;
            }
            MatchDao matchDao  = new MatchDao();
            matchDao.addMatch(new Match(inningsList.get(0),inningsList.get(1),0,0,0));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return insertionStatus>0;
    }


    public boolean updateScore(int score, int wickets, int teamId) {
        int res = 0;
        try {
            PreparedStatement preparedStatement = con.prepareStatement("update INNINGS set SCORE='"+score+"',WICKETS='"+wickets+"'where TEAM_ID="+teamId);
            res = preparedStatement.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return res>0;
    }
}
