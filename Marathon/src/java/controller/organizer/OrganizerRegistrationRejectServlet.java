/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
/**
 *
 * @author MinhTQHE190232
 */
package controller.organizer;

import dal.RegistrationDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;
import model.Registration;



@WebServlet(
        name = "OrganizerRegistrationRejectServlet",
        urlPatterns = {"/organizer/registration/reject"}
)
public class OrganizerRegistrationRejectServlet
        extends HttpServlet {

@Override
protected void doPost(
        HttpServletRequest request,
        HttpServletResponse response)
        throws ServletException, IOException {

    HttpSession session = request.getSession();

    User organizer =
            (User) session.getAttribute("user");

    if (organizer == null) {
        response.sendRedirect(
                request.getContextPath() + "/login"
        );
        return;
    }

    int registrationId = parsePositiveInt(
            request.getParameter("registrationId")
    );

    if (registrationId <= 0) {
        response.sendError(
                HttpServletResponse.SC_BAD_REQUEST,
                "registrationId không hợp lệ."
        );
        return;
    }

    RegistrationDAO registrationDAO =
            new RegistrationDAO();

    Registration registration =
            registrationDAO
                    .getRegistrationByIdAndOrganizer(
                            registrationId,
                            organizer.getUserId()
                    );

    if (registration == null) {
        response.sendError(
                HttpServletResponse.SC_NOT_FOUND,
                "Không tìm thấy đăng ký hoặc "
                + "bạn không có quyền xử lý."
        );
        return;
    }

    int actualRaceId =
            registration.getRaceId();

    boolean rejected =
            registrationDAO.rejectRegistration(
                    registrationId,
                    organizer.getUserId()
            );

    if (rejected) {
        session.setAttribute(
                "successMessage",
                "Đã từ chối đăng ký thành công."
        );
    } else {
        session.setAttribute(
                "errorMessage",
                "Không thể từ chối đăng ký vì "
                + "đăng ký đã được xử lý."
        );
    }

    response.sendRedirect(
            request.getContextPath()
            + "/organizer/registrations"
            + "?raceId="
            + actualRaceId
    );
}

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.sendError(
                HttpServletResponse.SC_METHOD_NOT_ALLOWED,
                "Chức năng này chỉ chấp nhận phương thức POST."
        );
    }

    private int parsePositiveInt(String value) {

        if (value == null || value.isBlank()) {
            return -1;
        }

        try {
            int number = Integer.parseInt(value);

            return number > 0 ? number : -1;

        } catch (NumberFormatException exception) {
            return -1;
        }
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
