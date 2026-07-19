/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
/**
 *
 * @author MinhTQHE190232
 */

package controller.organizer;

import dal.DistanceDAO;
import dal.RaceDAO;
import dal.RegistrationDAO;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.DistanceKM;
import model.Race;
import model.Registration;
import model.User;


@WebServlet(
        name = "OrganizerRegistrationListServlet",
        urlPatterns = {"/organizer/registrations"}
)

public class OrganizerRegistrationListServlet

        extends HttpServlet {

    private static final String LIST_VIEW =
            "/views/organizer/registration-list.jsp";



    private static final Set<String> VALID_STATUSES =
            Set.of(
                   "PENDING",
                    "APPROVED",
                    "REJECTED",
                    "CANCELLED"

            );



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



        /*

         * Kiểm tra giải có thực sự thuộc Organizer

         * đang đăng nhập hay không.

         */

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



        String status = normalizeStatus(

                request.getParameter("status")

        );



        Integer distanceId = parseOptionalPositiveInt(

                request.getParameter("distanceId")

        );



        /*

         * Nếu client có truyền distanceId,

         * phải kiểm tra cự ly đó thuộc giải hiện tại.

         */

        DistanceDAO distanceDAO = new DistanceDAO();



        List<DistanceKM> distances =

                distanceDAO.getDistancesByRace(

                        raceId,

                        organizer.getUserId()

                );



        if (distanceId != null

                && !containsDistance(

                        distances,

                        distanceId)) {



            response.sendError(

                    HttpServletResponse.SC_BAD_REQUEST,

                    "Cự ly lọc không thuộc giải này."

            );

            return;

        }



        RegistrationDAO registrationDAO =

                new RegistrationDAO();



        List<Registration> registrations =

                registrationDAO.getRegistrationsByRace(

                        raceId,

                        organizer.getUserId(),

                        status,

                        distanceId

                );



        request.setAttribute("race", race);

        request.setAttribute("distances", distances);

        request.setAttribute(

                "registrations",

                registrations

        );



        request.setAttribute(

                "selectedStatus",

                status == null ? "" : status

        );



        request.setAttribute(

                "selectedDistanceId",

                distanceId

        );



        request.getRequestDispatcher(LIST_VIEW)

                .forward(request, response);

    }



    private String normalizeStatus(String value) {



        if (value == null || value.isBlank()) {

            return null;

        }



        String normalized =

                value.trim().toUpperCase();



        if (!VALID_STATUSES.contains(normalized)) {

            return null;

        }



        return normalized;

    }



    private Integer parseOptionalPositiveInt(

            String value) {



        if (value == null || value.isBlank()) {

            return null;

        }



        try {

            int number = Integer.parseInt(value);



            return number > 0 ? number : null;



        } catch (NumberFormatException exception) {

            return null;

        }

    }



    private int parsePositiveInt(String value) {



        try {

            int number = Integer.parseInt(value);



            return number > 0 ? number : -1;



        } catch (NumberFormatException exception) {

            return -1;

        }

    }



    private boolean containsDistance(

            List<DistanceKM> distances,

            int distanceId) {



        for (DistanceKM distance : distances) {

            if (distance.getDistanceId()

                    == distanceId) {



                return true;

            }

        }



        return false;

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
