/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Filter.java to edit this template
 */
package filter;

import java.io.IOException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;

/**
 *
 * @author Admin
 */
@WebFilter(filterName = "organizer", urlPatterns = {"/organizer/*"})
public class OrganizerAuthorizationFilter implements Filter {
        @Override
    public void init(FilterConfig filterConfig)
            throws ServletException {
    }

    @Override
    public void doFilter(
            ServletRequest servletRequest,
            ServletResponse servletResponse,
            FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest request =
                (HttpServletRequest) servletRequest;

        HttpServletResponse response =
                (HttpServletResponse) servletResponse;

        HttpSession session =
                request.getSession(false);

        if (session == null) {
            response.sendRedirect(
                    request.getContextPath()
                    + "/index.html"
            );
            return;
        }

        User user =
                (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect(
                    request.getContextPath()
                    + "/index.html"
            );
            return;
        }

        if (!user.isOrganizer()) {
            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Bạn không có quyền truy cập chức năng Organizer."
            );
            return;
        }

        if (!"ACTIVE".equalsIgnoreCase(user.getStatus())) {
            session.invalidate();

            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Tài khoản của bạn không hoạt động."
            );
            return;
        }

        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
