package com.cricket;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginServlet extends HttpServlet {



    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        DatabaseRepository databaseRepository = new DatabaseRepository();

        int userId = Integer.parseInt(req.getParameter("userId"));
        String password = req.getParameter("passwordId");

        String role = databaseRepository.verifyLogin(userId,password);
        Cookie ck=new Cookie("userId",String.valueOf(userId));
        resp.addCookie(ck);
       /* RequestDispatcher requestDispatcher = req.getRequestDispatcher("ProfileServlet");
        requestDispatcher.forward(req,resp);*/
            if(role!=null) {
                resp.getWriter().write(role);
            }
            else{
                resp.getWriter().write("Invalid username or password");

            }

    }

}