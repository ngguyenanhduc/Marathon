/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 * @author Admin 
*/

package controller.organizer;

import dal.DistanceDAO;
import dal.RaceDAO;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.DistanceKM;
import model.Race;
import model.User;

@WebServlet(
        name = "OrganizerDistanceListServlet",
        urlPatterns = {"/organizer/distances"}
)
public class OrganizerDistanceListServlet
        extends HttpServlet {

    private static final String LIST_VIEW =
            "/views/organizer/distance-list.jsp";

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        User organizer =
                (User) request.getSession()
                        .getAttribute("user");

        int raceId = parsePositiveInt(
                request.getParameter("raceId")
        );

        if (raceId <= 0) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "raceId không hợp lệ."
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

        DistanceDAO distanceDAO = new DistanceDAO();

        List<DistanceKM> distances =
                distanceDAO.getDistancesByRace(
                        raceId,
                        organizer.getUserId()
                );

        request.setAttribute("race", race);
        request.setAttribute("distances", distances);

        request.getRequestDispatcher(LIST_VIEW)
                .forward(request, response);
    }

    private int parsePositiveInt(String value) {
        try {
            int number = Integer.parseInt(value);
            return number > 0 ? number : -1;

        } catch (NumberFormatException exception) {
            return -1;
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
