/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */


/**
 *
 * @author MinhTQHE190232
 */
package controller.organizer;

import dal.RaceDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Race;
import model.User;

@WebServlet(
        name = "OrganizerRaceDetailServlet",
        urlPatterns = {"/organizer/race/detail"}
)
public class OrganizerRaceDetailServlet
        extends HttpServlet {

    private static final String DETAIL_VIEW =
            "/views/organizer/race-detail.jsp";

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        User organizer =
                (User) request.getSession()
                        .getAttribute("user");

        String raceIdRaw =
                request.getParameter("raceId");

        int raceId;

        try {
            raceId = Integer.parseInt(raceIdRaw);
        } catch (NumberFormatException exception) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "raceId không hợp lệ."
            );
            return;
        }

        if (raceId <= 0) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "raceId phải lớn hơn 0."
            );
            return;
        }

        RaceDAO raceDAO = new RaceDAO();

        Race race =
                raceDAO.getRaceByIdAndOrganizer(
                        raceId,
                        organizer.getUserId()
                );

        if (race == null) {
            response.sendError(
                    HttpServletResponse.SC_NOT_FOUND,
                    "Không tìm thấy giải hoặc bạn không có quyền truy cập."
            );
            return;
        }

        request.setAttribute("race", race);

        request.getRequestDispatcher(DETAIL_VIEW)
                .forward(request, response);
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
    //    processRequest(request, response);
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
