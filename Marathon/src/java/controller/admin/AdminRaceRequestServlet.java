package controller.admin;

import dal.RaceDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import model.Race;
import model.User;

/**
 * Author: anhdu
 *
 * Version: 1
 *
 * Last Update Date: 17/7/2026
 *
 * Purpose: hien thi va xu ly duyet giai chay cho Admin
 */
public class AdminRaceRequestServlet extends HttpServlet {

    private static final String RACE_REQUEST_VIEW
            = "/views/admin/race-requests.jsp";

    //lay cac giai PENDING va forward den JSP
    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        RaceDAO raceDAO = new RaceDAO();
        List<Race> races = raceDAO.getPendingRacesForAdmin();

        request.setAttribute("races", races);
        request.getRequestDispatcher(RACE_REQUEST_VIEW)
                .forward(request, response);
    }

    //duyet hoac tu choi giai chay dang cho Admin xu ly
    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        User admin = (User) session.getAttribute("user");

        int raceId = parsePositiveInt(request.getParameter("raceId"));
        String action = normalizeAction(request.getParameter("action"));

        if (raceId <= 0 || action.isEmpty()) {
            session.setAttribute("errorMessage",
                    "Du lieu duyet giai khong hop le.");
            response.sendRedirect(request.getContextPath()
                    + "/admin/race-requests");
            return;
        }

        RaceDAO raceDAO = new RaceDAO();
        boolean reviewed = raceDAO.reviewRaceByAdmin(
                raceId, admin.getUserId(), action);

        if (reviewed) {
            session.setAttribute("successMessage",
                    "Da xu ly yeu cau giai chay.");
        } else {
            session.setAttribute("errorMessage",
                    "Khong the xu ly giai nay. Co the giai da duoc duyet.");
        }

        response.sendRedirect(request.getContextPath()
                + "/admin/race-requests");
    }

    //chi chap nhan APPROVED hoac REJECTED
    private String normalizeAction(String value) {
        if ("APPROVED".equalsIgnoreCase(value)) {
            return "APPROVED";
        }

        if ("REJECTED".equalsIgnoreCase(value)) {
            return "REJECTED";
        }

        return "";
    }

    //parse so nguyen duong, sai dinh dang thi tra ve -1
    private int parsePositiveInt(String value) {
        try {
            int number = Integer.parseInt(value);
            return number > 0 ? number : -1;
        } catch (NumberFormatException exception) {
            return -1;
        }
    }
}
