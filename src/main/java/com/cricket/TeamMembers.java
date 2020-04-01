package com.cricket;

import com.cricket.DataAccessObjects.TeamDao;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TeamMembers extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String teamId = req.getParameter("teamId");
        System.out.println(teamId);
        TeamDao teamDao = new TeamDao();
        JSONObject jsonObject = teamDao.getTeam(Integer.parseInt(teamId));
        resp.getWriter().write(String.valueOf(jsonObject));
    }
}
