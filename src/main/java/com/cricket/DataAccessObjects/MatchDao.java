package com.cricket.DataAccessObjects;

import com.cricket.models.Match;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MatchDao {
    String url;
    String username;
    String password;
    private static Connection con;

    public MatchDao() {

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

    public void addMatch(Match match) {
        try {
            PreparedStatement preparedStatement1 = con.prepareStatement("insert into CRICKET_MATCH (FIRST_INNINGS ,SECOND_INNINGS,MATCH_RESULT,MAN_OF_THE_MATCH,SCORE_DIFFERENCE) values(?,?,?,?,?)");
            preparedStatement1.setInt(1, match.getFirstInnings());
            preparedStatement1.setInt(2, match.getSecondInnings());
            preparedStatement1.setInt(3, match.getResult());
            preparedStatement1.setInt(5, match.getScoreDifference());
            preparedStatement1.setInt(4, match.getManOfTheMatch());
            preparedStatement1.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
