/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller.auth;

import dal.UserDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;

/**
 *
 * @author MinhTQ
 * Des: log tam thoi cua Organ, se xoa sau khi hoan chinh
 */
@WebServlet(name="DevOrganizerLoginServlet", urlPatterns={"/dev-login-organizer"})
public class DevOrganizerLoginServlet extends HttpServlet {
   
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet DevOrganizerLoginServlet</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet DevOrganizerLoginServlet at " + request.getContextPath () + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    } 

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
@Override
protected void doGet(
        HttpServletRequest request,
        HttpServletResponse response)
        throws ServletException, IOException {

    String username = request.getParameter("username");

    if (username == null || username.trim().isEmpty()) {
        response.setContentType("text/html;charset=UTF-8");

        try (PrintWriter out = response.getWriter()) {
            out.println("""
                <!DOCTYPE html>
                <html lang="vi">
                <head>
                    <meta charset="UTF-8">
                    <title>Đăng nhập Organizer thử nghiệm</title>
                </head>
                <body>
                    <h2>Đăng nhập Organizer</h2>

                    <form method="get"
                          action="dev-login-organizer">

                        <label for="username">
                            Username:
                        </label>

                        <input
                            type="text"
                            id="username"
                            name="username"
                            required>

                        <button type="submit">
                            Đăng nhập
                        </button>
                    </form>
                </body>
                </html>
                """);
        }

        return;
    }

    username = username.trim();

    UserDAO userDAO = new UserDAO();

    User organizer =
            userDAO.getUserByUsername(username);

    if (organizer == null) {
        response.sendError(
                HttpServletResponse.SC_NOT_FOUND,
                "Không tìm thấy tài khoản: " + username
        );
        return;
    }

    if (!organizer.isOrganizer()) {
        response.sendError(
                HttpServletResponse.SC_FORBIDDEN,
                "Tài khoản không có quyền ORGANIZER."
        );
        return;
    }

    if (!"ACTIVE".equalsIgnoreCase(
            organizer.getStatus())) {

        response.sendError(
                HttpServletResponse.SC_FORBIDDEN,
                "Tài khoản Organizer không hoạt động."
        );
        return;
    }

    HttpSession oldSession =
            request.getSession(false);

    if (oldSession != null) {
        oldSession.invalidate();
    }

    HttpSession newSession =
            request.getSession(true);

    newSession.setAttribute(
            "user",
            organizer
    );

    response.sendRedirect(
            request.getContextPath()
            + "/organizer/races"
    );
}

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
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
