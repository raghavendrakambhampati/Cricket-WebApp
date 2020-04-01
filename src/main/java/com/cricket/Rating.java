package com.cricket;

import com.cricket.DataAccessObjects.RatingDao;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Rating extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        int userId = Integer.parseInt(req.getParameter("userId"));
        int rating = Integer.parseInt(req.getParameter("rating"));
        RatingDao ratingDao =  new RatingDao();
        ratingDao.updateRating(userId,rating);
        System.out.println(userId+"Rating"+rating);
// resp.getWriter().write(String.valueOf(jsonObject));
    }
}