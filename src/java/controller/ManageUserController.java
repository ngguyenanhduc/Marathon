/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;


import dal.RaceDAL;
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
public class ManageUserController extends HttpServlet {
   
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
            out.println("<title>Servlet ManageUserController</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ManageUserController at " + request.getContextPath () + "</h1>");
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
       // Bước 1: Đọc tham số từ giao diện gửi về
        int raceId = Integer.parseInt(request.getParameter("raceId"));
        String action = request.getParameter("action"); // Nhận giá trị "APPROVED" hoặc "REJECTED"
        
        // Giả định ID của Admin phê duyệt lấy từ session (tạm thời để 1)
        int adminId = 1; 
        
        // Bước 2: Gọi RaceDAL để cập nhật trạng thái giải chạy và người duyệt
        RaceDAL raceDAL = new RaceDAL();
        boolean isSuccess = raceDAL.reviewRace(raceId, adminId, action);
        
        // Bước 3: Quay trở lại trang quản lý giải chạy của Admin
        if (isSuccess) {
            response.sendRedirect(request.getContextPath() + "/admin/races.jsp?status=" + action);
        } else {
            response.sendRedirect(request.getContextPath() + "/admin/races.jsp?message=failed");
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
