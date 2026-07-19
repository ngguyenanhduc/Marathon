package controller.publicpage;

import dal.RaceDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import model.Race;

/**
 * Author: PhucNTHE173021
 *
 * Version: 1
 *
 * Last Update Date: 17/7/2026
 *
 * Purpose: hien thi va tim kiem danh sach giai chay cong khai
 */
public class RaceListServlet extends HttpServlet {

    private static final String RACE_LIST_VIEW
            = "/views/public/race-list.jsp";

    //lay danh sach giai cong khai theo tu khoa va forward den JSP
    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        //tu khoa rong se lay toan bo giai cong khai
        String keyword = request.getParameter("keyword");
        keyword = keyword == null ? "" : keyword.trim();

        //goi DAO de lay danh sach giai khong bi PENDING hoac REJECTED
        RaceDAO raceDAO = new RaceDAO();
        List<Race> races = raceDAO.getPublicRaces(keyword);

        request.setAttribute("keyword", keyword);
        request.setAttribute("races", races);

        //forward du lieu sang trang danh sach giai chay
        request.getRequestDispatcher(RACE_LIST_VIEW)
                .forward(request, response);
    }
}
