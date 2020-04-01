package com.cricket;

import com.cricket.DataAccessObjects.InningsDao;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Score extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int score = Integer.parseInt(req.getParameter("score"));
        int wickets = Integer.parseInt(req.getParameter("wickets"));
        int teamId = Integer.parseInt(req.getParameter("TeamId"));
        //System.out.println(teamId);
        InningsDao inningsDao  = new InningsDao();
        boolean insertionStatus = inningsDao.updateScore(score,wickets,teamId);
        if(insertionStatus) {
            resp.getWriter().write("Score Updated Successfully");
        }
        else{
            resp.getWriter().write("Score Not updated");
        }
    }
}
