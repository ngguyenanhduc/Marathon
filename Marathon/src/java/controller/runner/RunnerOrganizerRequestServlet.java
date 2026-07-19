package controller.runner;

import dal.OrganizerRequestDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import model.OrganizerRequest;
import model.User;

/**
 * Author: PhucNTHE173021
 *
 * Version: 1
 *
 * Last Update Date: 17/7/2026
 *
 * Purpose: gui va theo doi yeu cau tro thanh Organizer
 */
public class RunnerOrganizerRequestServlet extends HttpServlet {

    private static final String REQUEST_VIEW
            = "/views/runner/organizer-request.jsp";

    //hien thi yeu cau Organizer moi nhat cua Runner
    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        User user = (User) request.getSession().getAttribute("user");

        //lay yeu cau moi nhat de Runner theo doi trang thai xu ly
        OrganizerRequestDAO requestDAO = new OrganizerRequestDAO();
        OrganizerRequest organizerRequest
                = requestDAO.getLatestRequestByRunner(user.getUserId());

        request.setAttribute("organizerRequest", organizerRequest);

        //forward du lieu yeu cau sang JSP
        request.getRequestDispatcher(REQUEST_VIEW)
                .forward(request, response);
    }

    //validate ly do va gui yeu cau tro thanh Organizer den Admin
    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        String reason = request.getParameter("reason");
        reason = reason == null ? "" : reason.trim();

        //gioi han do dai ly do truoc khi ghi vao database
        if (reason.length() > 1000) {
            request.setAttribute("error",
                    "Ly do khong duoc vuot qua 1000 ky tu.");
            request.setAttribute("reason", reason);
            doGet(request, response);
            return;
        }

        //khong cho tao them yeu cau khi dang co yeu cau PENDING
        OrganizerRequestDAO requestDAO = new OrganizerRequestDAO();
        if (requestDAO.hasPendingRequest(user.getUserId())) {
            session.setAttribute("errorMessage",
                    "Ban dang co mot yeu cau cho Admin xu ly.");
            response.sendRedirect(request.getContextPath()
                    + "/runner/organizer-request");
            return;
        }

        boolean created = requestDAO.createRequest(
                user.getUserId(), reason.isEmpty() ? null : reason);

        //luu flash message vao session cho request sau
        if (!created) {
            session.setAttribute("errorMessage",
                    "Khong the gui yeu cau. Vui long thu lai.");
        } else {
            session.setAttribute("successMessage",
                    "Da gui yeu cau Organizer den Admin.");
        }

        //redirect de tranh gui trung yeu cau khi refresh trang
        response.sendRedirect(request.getContextPath()
                + "/runner/organizer-request");
    }
}
