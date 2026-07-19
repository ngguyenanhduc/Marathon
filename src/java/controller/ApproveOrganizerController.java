/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;

import dal.UserDAL;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author anhdu
 */
public class ApproveOrganizerController extends HttpServlet {
   
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
            out.println("<title>Servlet ApproveOrganizerController</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ApproveOrganizerController at " + request.getContextPath () + "</h1>");
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
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
        // Bước 1: Lấy các tham số truyền từ Form JSP lên
        int requestId = Integer.parseInt(request.getParameter("requestId"));
        String action = request.getParameter("action"); // Sẽ nhận giá trị "APPROVED" hoặc "REJECTED"
        
        // Bước 2: Xác định Admin nào đang thực hiện phê duyệt
        // Trong thực tế, bạn sẽ lấy từ Session sau khi Admin đăng nhập:
        // int adminId = ((User) request.getSession().getAttribute("account")).getUserId();
        int adminId = 1; // Tạm thời để cứng ID = 1 để test nếu chưa làm chức năng Login

        // Bước 3: Gọi lớp DAL để xử lý cập nhật Database
        UserDAL userDAL = new UserDAL();
        boolean isSuccess = userDAL.handleOrganizerRequest(requestId, adminId, action);
        
        // Bước 4: Điều hướng Admin quay trở lại trang danh sách yêu cầu chờ duyệt
        if (isSuccess) {
            // Nếu thành công, chuyển hướng kèm thông báo thành công (nếu muốn)
            response.sendRedirect(request.getContextPath() + "/admin/requests.jsp?message=success");
        } else {
            // Nếu lỗi (ví dụ lỗi SQL), chuyển hướng kèm thông báo lỗi
            response.sendRedirect(request.getContextPath() + "/admin/requests.jsp?message=error");
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
