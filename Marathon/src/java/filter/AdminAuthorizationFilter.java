package filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import model.User;

/**
 * Author: anhdu
 *
 * Version: 1
 *
 * Last Update Date: 17/7/2026
 *
 * Purpose: bao ve cac URL chi danh cho Admin
 */
public class AdminAuthorizationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    //kiem tra session, role va trang thai truoc khi vao URL Admin
    @Override
    public void doFilter(ServletRequest servletRequest,
            ServletResponse servletResponse,
            FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest request
                = (HttpServletRequest) servletRequest;
        HttpServletResponse response
                = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession(false);

        //chua dang nhap thi dua nguoi dung ve trang login
        User user = session == null
                ? null : (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        //tai khoan da dang nhap nhung sai role se nhan HTTP 403
        if (!user.isAdmin()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN,
                    "Ban khong co quyen truy cap chuc nang Admin.");
            return;
        }

        //huy session cua tai khoan Admin khong con ACTIVE
        if (!"ACTIVE".equalsIgnoreCase(user.getStatus())) {
            session.invalidate();
            response.sendError(HttpServletResponse.SC_FORBIDDEN,
                    "Tai khoan hien khong hoat dong.");
            return;
        }

        //cho request tiep tuc den servlet khi du dieu kien
        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
