package com.cricket.Servlets;

import com.cricket.DataAccessObjects.InningsDao;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class InningsServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int firstInnings = Integer.parseInt(req.getParameter("FirstInnings"));
        int secondInnings = Integer.parseInt(req.getParameter("SecondInnings"));
        InningsDao inningsDao = new InningsDao();
         boolean insertionStatus = inningsDao.updateInnings(firstInnings,secondInnings);
         if(insertionStatus){
             resp.getWriter().write("Match Starts in 15 minutes !! Stay-Tuned");
         }
    }
}
