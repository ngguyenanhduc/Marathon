package controller.admin;

import dal.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import model.User;

/**
 * Author: anhdu
 *
 * Version: 1
 *
 * Last Update Date: 17/7/2026
 *
 * Purpose: hien thi va cap nhat trang thai tai khoan nguoi dung
 */
public class AdminUserServlet extends HttpServlet {

    private static final String USER_VIEW = "/views/admin/users.jsp";

    //lay danh sach user theo bo loc va forward den JSP
    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String keyword = trim(request.getParameter("keyword"));
        String roleName = normalize(request.getParameter("roleName"),
                "ADMIN", "ORGANIZER", "RUNNER");
        String status = normalize(request.getParameter("status"),
                "ACTIVE", "INACTIVE", "BANNED");

        UserDAO userDAO = new UserDAO();
        List<User> users = userDAO.getUsersForAdmin(
                keyword, roleName, status);

        request.setAttribute("users", users);
        request.setAttribute("keyword", keyword);
        request.setAttribute("roleName", roleName);
        request.setAttribute("status", status);
        request.getRequestDispatcher(USER_VIEW).forward(request, response);
    }

    //cap nhat trang thai user theo yeu cau cua Admin
    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");

        int userId = parsePositiveInt(request.getParameter("userId"));
        String status = normalize(request.getParameter("status"),
                "ACTIVE", "INACTIVE", "BANNED");

        if (userId <= 0 || status.isEmpty()) {
            session.setAttribute("errorMessage",
                    "Du lieu cap nhat user khong hop le.");
            response.sendRedirect(request.getContextPath() + "/admin/users");
            return;
        }

        if (currentUser != null
                && currentUser.getUserId() == userId
                && !"ACTIVE".equals(status)) {
            session.setAttribute("errorMessage",
                    "Khong the khoa tai khoan Admin dang dang nhap.");
            response.sendRedirect(request.getContextPath() + "/admin/users");
            return;
        }

        UserDAO userDAO = new UserDAO();
        boolean updated = userDAO.updateUserStatus(userId, status);

        if (updated) {
            session.setAttribute("successMessage",
                    "Cap nhat trang thai user thanh cong.");
        } else {
            session.setAttribute("errorMessage",
                    "Khong the cap nhat trang thai user.");
        }

        response.sendRedirect(request.getContextPath() + "/admin/users");
    }

    //chuan hoa chuoi parameter va tranh NullPointerException
    private String trim(String value) {
        return value == null ? "" : value.trim();
    }

    //chi nhan cac gia tri nam trong danh sach cho phep
    private String normalize(String value, String... allowedValues) {
        String normalized = trim(value).toUpperCase();

        for (String allowedValue : allowedValues) {
            if (allowedValue.equals(normalized)) {
                return normalized;
            }
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
