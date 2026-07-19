package controller.auth;

import dal.UserDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import model.User;

/**
 * Author: PhucNTHE173021
 *
 * Version: 1
 *
 * Last Update Date: 17/7/2026
 *
 * Purpose: xu ly dang nhap va luu User vao session
 */
public class LoginServlet extends HttpServlet {

    private static final String LOGIN_VIEW = "/views/auth/login.jsp";

    //hien thi trang dang nhap khi nguoi dung chua co session
    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        //neu da dang nhap thi khong hien lai form login
        HttpSession session = request.getSession(false);
        User user = session == null
                ? null : (User) session.getAttribute("user");

        if (user != null) {
            redirectByRole(request, response, user);
            return;
        }

        RequestDispatcher dispatcher
                = request.getRequestDispatcher(LOGIN_VIEW);
        dispatcher.forward(request, response);
    }

    //kiem tra thong tin dang nhap va tao session
    //cho nguoi dung hop le
    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        //doc du lieu tu form dang nhap
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        username = username == null ? "" : username.trim();

        if (username.isEmpty() || password == null || password.isEmpty()) {
            forwardWithError(request, response, username,
                    "Vui long nhap day du username va mat khau.");
            return;
        }

        //lay tai khoan tu database va so sanh mat khau theo bai mau
        UserDAO userDAO = new UserDAO();
        User loginUser = userDAO.getUserByUsername(username);

        if (loginUser == null
                || !password.equals(loginUser.getPassword())) {
            forwardWithError(request, response, username,
                    "Username hoac mat khau khong dung.");
            return;
        }

        //chi tai khoan ACTIVE moi duoc tao session dang nhap
        if (!"ACTIVE".equalsIgnoreCase(loginUser.getStatus())) {
            forwardWithError(request, response, username,
                    "Tai khoan hien khong hoat dong.");
            return;
        }

        //huy session cu de tao mot phien dang nhap sach
        HttpSession oldSession = request.getSession(false);
        if (oldSession != null) {
            oldSession.invalidate();
        }

        HttpSession newSession = request.getSession(true);
        newSession.setAttribute("user", loginUser);
        newSession.setMaxInactiveInterval(30 * 60);

        //dieu huong den module phu hop sau khi dang nhap thanh cong
        redirectByRole(request, response, loginUser);
    }

    //forward lai form va giu username khi dang nhap that bai
    private void forwardWithError(HttpServletRequest request,
            HttpServletResponse response,
            String username,
            String error)
            throws ServletException, IOException {

        request.setAttribute("username", username);
        request.setAttribute("error", error);
        request.getRequestDispatcher(LOGIN_VIEW).forward(request, response);
    }

    //dieu huong nguoi dung theo role dang co trong database
    private void redirectByRole(HttpServletRequest request,
            HttpServletResponse response,
            User user) throws IOException {

        String contextPath = request.getContextPath();

        if (user.isOrganizer()) {
            response.sendRedirect(contextPath + "/organizer/races");
        } else if (user.isAdmin()) {
            response.sendRedirect(contextPath + "/admin/users");
        } else {
            response.sendRedirect(contextPath + "/races");
        }
    }
}
