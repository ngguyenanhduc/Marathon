package controller.auth;

import dal.RoleDAO;
import dal.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import model.Role;
import model.User;
import util.ValidationUtil;

/**
 * Author: PhucNTHE173021
 *
 * Version: 1
 *
 * Last Update Date: 17/7/2026
 *
 * Purpose: hien thi form va tao tai khoan Runner moi
 */
public class RegisterServlet extends HttpServlet {

    private static final String REGISTER_VIEW = "/views/auth/register.jsp";

    //hien thi form dang ky tai khoan Runner
    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher(REGISTER_VIEW).forward(request, response);
    }

    //validate du lieu va tao tai khoan Runner moi trong database
    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        //doc va chuan hoa du lieu form truoc khi validate
        String username = trim(request.getParameter("username"));
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String fullName = trim(request.getParameter("fullName"));
        String email = trim(request.getParameter("email"));
        String phone = trim(request.getParameter("phone"));

        User formUser = new User();
        formUser.setUserName(username);
        formUser.setFullName(fullName);
        formUser.setEmail(email);
        formUser.setPhone(phone);
        request.setAttribute("formUser", formUser);

        String error = ValidationUtil.validateRegistration(
                username, password, confirmPassword,
                fullName, email, phone);

        //dung xu ly va hien lai form neu du lieu khong hop le
        if (error != null) {
            forwardWithError(request, response, error);
            return;
        }

        UserDAO userDAO = new UserDAO();

        //kiem tra du lieu trung truoc khi thuc hien INSERT
        if (userDAO.usernameExists(username)) {
            forwardWithError(request, response,
                    "Username da ton tai trong he thong.");
            return;
        }

        if (userDAO.emailExists(email, null)) {
            forwardWithError(request, response,
                    "Email da duoc su dung boi tai khoan khac.");
            return;
        }

        //lay role RUNNER theo ten thay vi gan cung roleId
        RoleDAO roleDAO = new RoleDAO();
        Role runnerRole = roleDAO.getRoleByName("RUNNER");

        if (runnerRole == null) {
            forwardWithError(request, response,
                    "Khong tim thay role RUNNER trong database.");
            return;
        }

        formUser.setPassword(password);

        //tao tai khoan voi role RUNNER va status ACTIVE
        boolean created = userDAO.createRunner(
                formUser, runnerRole.getRoleId());

        if (!created) {
            forwardWithError(request, response,
                    "Khong the tao tai khoan. Vui long thu lai.");
            return;
        }

        //dung Post-Redirect-Get de tranh gui lai form khi refresh
        request.getSession().setAttribute("successMessage",
                "Dang ky tai khoan thanh cong. Ban co the dang nhap.");
        response.sendRedirect(request.getContextPath() + "/login");
    }

    //chuan hoa chuoi parameter va tranh NullPointerException
    private String trim(String value) {
        return value == null ? "" : value.trim();
    }

    //forward lai form dang ky kem thong bao loi
    private void forwardWithError(HttpServletRequest request,
            HttpServletResponse response,
            String error)
            throws ServletException, IOException {
        request.setAttribute("error", error);
        request.getRequestDispatcher(REGISTER_VIEW)
                .forward(request, response);
    }
}
