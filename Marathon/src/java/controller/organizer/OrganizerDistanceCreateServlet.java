/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
/**
 *
 * @author MinhTQHE190232
 * Desc: Them cu ly
 */

package controller.organizer;

import dal.DistanceDAO;
import dal.RaceDAO;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.DistanceKM;
import model.Race;
import model.User;

@WebServlet(
        name = "OrganizerDistanceCreateServlet",
        urlPatterns = {"/organizer/distance/create"}
)
public class OrganizerDistanceCreateServlet
        extends HttpServlet {

    private static final String CREATE_VIEW =
            "/views/organizer/distance-create.jsp";

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
                    "Không tìm thấy giải."
            );
            return;
        }

        if (!race.isEditable()) {
            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Không thể thêm cự ly vào giải đã được duyệt."
            );
            return;
        }

        request.setAttribute("race", race);

        request.getRequestDispatcher(CREATE_VIEW)
                .forward(request, response);
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);

        User organizer =
                (User) session.getAttribute("user");

        int raceId = parsePositiveInt(
                request.getParameter("raceId")
        );

        String distanceName =
                normalize(
                        request.getParameter("distanceName")
                );

        String distanceKmRaw =
                normalize(
                        request.getParameter("distanceKm")
                );

        String maxParticipantRaw =
                normalize(
                        request.getParameter("maxParticipant")
                );

        String error = null;

        BigDecimal distanceKm = null;
        int maxParticipant = 0;

        if (raceId <= 0) {
            error = "raceId không hợp lệ.";

        } else if (distanceName.isEmpty()) {
            error = "Tên cự ly không được để trống.";

        } else if (distanceName.length() > 50) {
            error = "Tên cự ly không được vượt quá 50 ký tự.";
        }

        if (error == null) {
            try {
                distanceKm =
                        new BigDecimal(distanceKmRaw)
                                .setScale(
                                        2,
                                        RoundingMode.HALF_UP
                                );

                if (distanceKm.compareTo(
                        BigDecimal.ZERO) <= 0) {

                    error = "Số kilomet phải lớn hơn 0.";
                }

            } catch (NumberFormatException exception) {
                error = "Số kilomet không hợp lệ.";
            }
        }

        if (error == null) {
            try {
                maxParticipant =
                        Integer.parseInt(
                                maxParticipantRaw
                        );

                if (maxParticipant <= 0) {
                    error =
                            "Số người tối đa phải lớn hơn 0.";
                }

            } catch (NumberFormatException exception) {
                error =
                        "Số người tối đa không hợp lệ.";
            }
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
                    "Không tìm thấy giải."
            );
            return;
        }

        if (!race.isEditable()) {
            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Không thể thêm cự ly vào giải này."
            );
            return;
        }

        DistanceKM distance = new DistanceKM();

        distance.setRaceId(raceId);
        distance.setDistanceName(distanceName);
        distance.setDistanceKm(distanceKm);
        distance.setMaxParticipant(maxParticipant);

        DistanceDAO distanceDAO = new DistanceDAO();

        if (error == null
                && distanceDAO.existsDistanceName(
                        raceId,
                        distanceName
                )) {

            error =
                    "Tên cự ly đã tồn tại trong giải.";
        }

        if (error == null
                && distanceDAO.existsDistanceKm(
                        raceId,
                        distanceKm
                )) {

            error =
                    "Số kilomet đã tồn tại trong giải.";
        }

        if (error != null) {
            request.setAttribute("error", error);
            request.setAttribute("race", race);
            request.setAttribute("distance", distance);

            request.setAttribute(
                    "distanceKmValue",
                    distanceKmRaw
            );

            request.setAttribute(
                    "maxParticipantValue",
                    maxParticipantRaw
            );

            request.getRequestDispatcher(CREATE_VIEW)
                    .forward(request, response);

            return;
        }

        boolean created =
                distanceDAO.createDistance(
                        distance,
                        organizer.getUserId()
                );

        if (!created) {
            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Không thể thêm cự ly."
            );
            return;
        }

        session.setAttribute(
                "successMessage",
                "Thêm cự ly thành công."
        );

        response.sendRedirect(
                request.getContextPath()
                + "/organizer/distances?raceId="
                + raceId
        );
    }

    private int parsePositiveInt(String value) {
        try {
            int number = Integer.parseInt(value);
            return number > 0 ? number : -1;

        } catch (NumberFormatException exception) {
            return -1;
        }
    }

    private String normalize(String value) {
        return value == null
                ? ""
                : value.trim();
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
