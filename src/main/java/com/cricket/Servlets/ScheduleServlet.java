package com.cricket.Servlets;

import com.cricket.DataAccessObjects.ScheduleDao;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ScheduleServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int numberOfMatchesPerDay = Integer.parseInt(req.getParameter("matchesPerDay"));
        String tournamentStartDate = req.getParameter("matchStartDate");
        ScheduleDao scheduleDao = new ScheduleDao();
        scheduleDao.insertSchedule(numberOfMatchesPerDay,tournamentStartDate);
        resp.getWriter().write("Schedule created");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        JSONObject jsonObject;
        String pathInfo = req.getPathInfo();
        System.out.println(req.getPathInfo());
        String[] splits = pathInfo.split("/");
        if(splits.length==0){
            JSONArray jsonArray;
            ScheduleDao scheduleDao = new ScheduleDao();
            jsonArray = scheduleDao.getSchedule();
            if(jsonArray.length()==0){
                resp.getWriter().write("No Matches");
            }
            else {
                resp.getWriter().write(String.valueOf(jsonArray));
            }
        }
        if(splits.length==2 && splits[1].matches("today")) {
            Date today = Calendar.getInstance().getTime();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String date = simpleDateFormat.format(today);
            System.out.println(date);
            ScheduleDao scheduleDao  =new ScheduleDao();
            jsonObject = scheduleDao.getTodayMatchTeams(date);
            if(jsonObject.length()==0){
                resp.getWriter().write("No Matches");
            }
            else {
                resp.getWriter().write(String.valueOf(jsonObject));
            }
        }
    }
}
