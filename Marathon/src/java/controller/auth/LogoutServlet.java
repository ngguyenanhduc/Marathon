package controller.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Author: PhucNTHE173021
 *
 * Version: 1
 *
 * Last Update Date: 17/7/2026
 *
 * Purpose: huy session va dang xuat nguoi dung khoi he thong
 */
public class LogoutServlet extends HttpServlet {

    //huy session hien tai va dua nguoi dung ve trang dang nhap
    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        //chi huy session khi session dang ton tai
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        response.sendRedirect(request.getContextPath() + "/login");
    }

    //cho phep form POST su dung cung xu ly dang xuat voi GET
    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
