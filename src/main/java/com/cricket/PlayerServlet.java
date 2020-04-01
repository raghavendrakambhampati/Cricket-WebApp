package com.cricket;
import com.cricket.DataAccessObjects.UserDao;
import org.json.JSONObject;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class PlayerServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Cookie ck[] = req.getCookies();
        //System.out.println(ck[0].getValue());
//         int randomId = Integer.parseInt(ck[0].getValue());


        int userId = Integer.parseInt(ck[0].getValue());
        UserDao userDao = new UserDao();
        JSONObject jsonObject = userDao.getTeam(userId);
        resp.getWriter().write(String.valueOf(jsonObject));
        //System.out.println(userId);
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        DatabaseRepository databaseRepository = new DatabaseRepository();
        //System.out.println("REached ");
        int userId = Integer.parseInt(req.getParameter("userId"));
        Cookie ck=new Cookie("userId",String.valueOf(userId));
        resp.addCookie(ck);
    }
}