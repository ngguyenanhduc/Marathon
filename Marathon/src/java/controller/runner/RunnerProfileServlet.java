package controller.runner;

import dal.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import model.User;
import util.ValidationUtil;

/**
 * Author: PhucNTHE173021
 *
 * Version: 1
 *
 * Last Update Date: 17/7/2026
 *
 * Purpose: hien thi va cap nhat ho so ca nhan cua Runner
 */
public class RunnerProfileServlet extends HttpServlet {

    private static final String PROFILE_VIEW
            = "/views/runner/profile.jsp";

    //nap ho so moi nhat cua Runner tu database va hien thi tren JSP
    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        User sessionUser = (User) request.getSession()
                .getAttribute("user");

        //khong dung du lieu cu trong session khi hien thi ho so
        UserDAO userDAO = new UserDAO();
        User profileUser = userDAO.getUserById(sessionUser.getUserId());

        if (profileUser == null) {
            //huy session khi tai khoan khong con ton tai trong database
            request.getSession().invalidate();
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        request.setAttribute("profileUser", profileUser);

        //forward ho so sang JSP de hien thi
        request.getRequestDispatcher(PROFILE_VIEW)
                .forward(request, response);
    }

    //validate va cap nhat ho ten, email, so dien thoai cua Runner
    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        User sessionUser = (User) session.getAttribute("user");

        //Runner chi duoc thay doi ho ten, email va so dien thoai
        String fullName = trim(request.getParameter("fullName"));
        String email = trim(request.getParameter("email"));
        String phone = trim(request.getParameter("phone"));

        User profileUser = new User();
        profileUser.setUserId(sessionUser.getUserId());
        profileUser.setUserName(sessionUser.getUserName());
        profileUser.setFullName(fullName);
        profileUser.setEmail(email);
        profileUser.setPhone(phone);
        request.setAttribute("profileUser", profileUser);

        String error = ValidationUtil.validateProfile(
                fullName, email, phone);

        //hien lai form khi thong tin ho so khong hop le
        if (error != null) {
            forwardWithError(request, response, error);
            return;
        }

        UserDAO userDAO = new UserDAO();

        //email moi khong duoc trung voi mot tai khoan khac
        if (userDAO.emailExists(email, sessionUser.getUserId())) {
            forwardWithError(request, response,
                    "Email da duoc su dung boi tai khoan khac.");
            return;
        }

        //cap nhat ho so sau khi tat ca validation da thanh cong
        if (!userDAO.updateProfile(profileUser)) {
            forwardWithError(request, response,
                    "Khong the cap nhat ho so. Vui long thu lai.");
            return;
        }

        //nap lai User de session co du lieu moi nhat tu database
        User updatedUser = userDAO.getUserById(sessionUser.getUserId());
        session.setAttribute("user", updatedUser);
        session.setAttribute("successMessage",
                "Cap nhat ho so thanh cong.");
        response.sendRedirect(request.getContextPath() + "/runner/profile");
    }

    //chuan hoa chuoi parameter va tranh NullPointerException
    private String trim(String value) {
        return value == null ? "" : value.trim();
    }

    //forward lai trang ho so kem thong bao loi
    private void forwardWithError(HttpServletRequest request,
            HttpServletResponse response,
            String error)
            throws ServletException, IOException {
        request.setAttribute("error", error);
        request.getRequestDispatcher(PROFILE_VIEW)
                .forward(request, response);
    }
}
