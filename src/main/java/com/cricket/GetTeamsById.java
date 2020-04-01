package com.cricket;

import com.cricket.DataAccessObjects.TeamDao;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class GetTeamsById extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Cookie ck[]=req.getCookies();
        int userId= Integer.parseInt(ck[0].getValue());
        System.out.println(userId);
        System.out.println(req.getPathInfo());
        TeamDao teamDao = new TeamDao();
        JSONObject jsonObject = teamDao.getTeam(userId);
        resp.getWriter().write(String.valueOf(jsonObject));
    }
}
