package controller.runner;

import dal.RegistrationDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import model.Registration;
import model.User;

/**
 * Author: PhucNTHE173021
 *
 * Version: 1
 *
 * Last Update Date: 17/7/2026
 *
 * Purpose: hien thi lich su dang ky giai cua Runner
 */
public class RunnerRegistrationListServlet extends HttpServlet {

    private static final String REGISTRATION_LIST_VIEW
            = "/views/runner/registration-list.jsp";

    //lay lich su dang ky cua Runner va forward den JSP
    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        User user = (User) request.getSession().getAttribute("user");

        //chi truy van cac don thuoc ve Runner trong session
        RegistrationDAO registrationDAO = new RegistrationDAO();
        List<Registration> registrations
                = registrationDAO.getRegistrationsByRunner(user.getUserId());

        request.setAttribute("registrations", registrations);

        //forward danh sach don va ket qua neu da duoc cap nhat
        request.getRequestDispatcher(REGISTRATION_LIST_VIEW)
                .forward(request, response);
    }
}
