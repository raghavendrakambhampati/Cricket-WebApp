package com.cricket;

import com.cricket.DataAccessObjects.UserDao;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Profile extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("login get");
        Cookie ck[]=req.getCookies();
        int userId= Integer.parseInt(ck[0].getValue());
        //System.out.println("login = "+userId);
        UserDao userDao = new UserDao();
        JSONObject jsonObject = userDao.getUser(userId);
        //System.out.println(jsonObject);
        resp.getWriter().write(String.valueOf(jsonObject));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
}
