package controller.admin;

import dal.OrganizerRequestDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import model.OrganizerRequest;
import model.User;

/**
 * Author: anhdu
 *
 * Version: 1
 *
 * Last Update Date: 17/7/2026
 *
 * Purpose: hien thi va xu ly yeu cau tro thanh Organizer
 */
public class AdminOrganizerRequestServlet extends HttpServlet {

    private static final String ORGANIZER_REQUEST_VIEW
            = "/views/admin/organizer-requests.jsp";

    //lay cac yeu cau Organizer PENDING va forward den JSP
    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        OrganizerRequestDAO requestDAO = new OrganizerRequestDAO();
        List<OrganizerRequest> requests
                = requestDAO.getPendingRequestsForAdmin();

        request.setAttribute("requests", requests);
        request.getRequestDispatcher(ORGANIZER_REQUEST_VIEW)
                .forward(request, response);
    }

    //duyet hoac tu choi yeu cau tro thanh Organizer
    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        User admin = (User) session.getAttribute("user");

        int requestId = parsePositiveInt(request.getParameter("requestId"));
        String action = normalizeAction(request.getParameter("action"));

        if (requestId <= 0 || action.isEmpty()) {
            session.setAttribute("errorMessage",
                    "Du lieu duyet Organizer khong hop le.");
            response.sendRedirect(request.getContextPath()
                    + "/admin/organizer-requests");
            return;
        }

        OrganizerRequestDAO requestDAO = new OrganizerRequestDAO();
        boolean reviewed = requestDAO.reviewRequestByAdmin(
                requestId, admin.getUserId(), action);

        if (reviewed) {
            session.setAttribute("successMessage",
                    "Da xu ly yeu cau Organizer.");
        } else {
            session.setAttribute("errorMessage",
                    "Khong the xu ly yeu cau. Co the yeu cau da duoc duyet.");
        }

        response.sendRedirect(request.getContextPath()
                + "/admin/organizer-requests");
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
