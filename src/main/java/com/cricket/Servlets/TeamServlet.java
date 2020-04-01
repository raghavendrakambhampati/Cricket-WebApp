package com.cricket.Servlets;

import com.cricket.DataAccessObjects.TeamDao;
import com.cricket.models.Team;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class TeamServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        String teamName = req.getParameter("teamName");
        String city = req.getParameter("city");
        TeamDao teamDao = new TeamDao();
        Cookie ck[]=req.getCookies();
        int userId= Integer.parseInt(ck[0].getValue());
        boolean insertionResult = teamDao.insertTeam(new Team(teamName, userId, city));

        if(insertionResult){
            resp.getWriter().write("Data Inserted successfully");
        }
        else{
            resp.getWriter().write("Data not inserted properly");
        }
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        TeamDao teamDao = new TeamDao();
        JSONObject jsonObject ;
        String pathInfo = req.getPathInfo();
        String[] splits = pathInfo.split("/");
       for(int i=0;i<splits.length;i++){
           System.out.println(splits[i]+" "+i);
       }
        System.out.println(splits.length);
       if(splits.length==0) {
           jsonObject= teamDao.getTeams();
           resp.getWriter().write(String.valueOf(jsonObject));
       }
       if(splits.length==2){
            int userId = Integer.parseInt(splits[1]);
            jsonObject = teamDao.getUser(userId);
            resp.getWriter().write(String.valueOf(jsonObject));
       }
       if(splits.length==3 && splits[2].matches("members")){
           int userId = Integer.parseInt(splits[1]);
           jsonObject  = teamDao.getTeam(userId);
           resp.getWriter().write(String.valueOf(jsonObject));
       }
    }
}
