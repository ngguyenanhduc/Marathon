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
import model.User;

@WebServlet(
        name = "OrganizerDistanceEditServlet",
        urlPatterns = {"/organizer/distance/edit"}
)
public class OrganizerDistanceEditServlet
        extends HttpServlet {

    private static final String EDIT_VIEW =
            "/views/organizer/distance-edit.jsp";

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        User organizer =
                (User) request.getSession()
                        .getAttribute("user");

        int distanceId = parsePositiveInt(
                request.getParameter("distanceId")
        );

        if (distanceId <= 0) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "distanceId không hợp lệ."
            );
            return;
        }

        DistanceDAO distanceDAO = new DistanceDAO();

        DistanceKM distance =
                distanceDAO.getDistanceByIdAndOrganizer(
                        distanceId,
                        organizer.getUserId()
                );

        if (distance == null) {
            response.sendError(
                    HttpServletResponse.SC_NOT_FOUND,
                    "Không tìm thấy cự ly hoặc bạn không có quyền truy cập."
            );
            return;
        }

        if (!distance.isRaceEditable()) {
            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Giải đã được duyệt nên không thể sửa cự ly."
            );
            return;
        }

        request.setAttribute("distance", distance);

        request.getRequestDispatcher(EDIT_VIEW)
                .forward(request, response);
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session =
                request.getSession(false);

        User organizer =
                (User) session.getAttribute("user");

        int distanceId = parsePositiveInt(
                request.getParameter("distanceId")
        );

        if (distanceId <= 0) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "distanceId không hợp lệ."
            );
            return;
        }

        DistanceDAO distanceDAO = new DistanceDAO();

        DistanceKM currentDistance =
                distanceDAO.getDistanceByIdAndOrganizer(
                        distanceId,
                        organizer.getUserId()
                );

        if (currentDistance == null) {
            response.sendError(
                    HttpServletResponse.SC_NOT_FOUND,
                    "Không tìm thấy cự ly."
            );
            return;
        }

        if (!currentDistance.isRaceEditable()) {
            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Giải đã được duyệt nên không thể sửa cự ly."
            );
            return;
        }

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

        if (distanceName.isEmpty()) {
            error = "Tên cự ly không được để trống.";

        } else if (distanceName.length() > 100) {
            error =
                    "Tên cự ly không được vượt quá 100 ký tự.";
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

                    error =
                            "Số kilomet phải lớn hơn 0.";
                }

            } catch (NumberFormatException exception) {
                error = "Số kilomet không hợp lệ.";
            }
        }

        if (error == null) {
            try {
                maxParticipant =
                        Integer.parseInt(maxParticipantRaw);

                if (maxParticipant <= 0) {
                    error =
                            "Số người tối đa phải lớn hơn 0.";
                }

            } catch (NumberFormatException exception) {
                error =
                        "Số người tối đa không hợp lệ.";
            }
        }

        DistanceKM distance = new DistanceKM();

        distance.setDistanceId(distanceId);
        distance.setRaceId(
                currentDistance.getRaceId()
        );

        distance.setRaceName(
                currentDistance.getRaceName()
        );

        distance.setRaceStatus(
                currentDistance.getRaceStatus()
        );

        distance.setDistanceName(distanceName);
        distance.setDistanceKm(distanceKm);
        distance.setMaxParticipant(maxParticipant);

        if (error == null
                && distanceDAO.existsDistanceNameForUpdate(
                        currentDistance.getRaceId(),
                        distanceName,
                        distanceId
                )) {

            error =
                    "Tên cự ly đã tồn tại trong giải.";
        }

        if (error == null
                && distanceDAO.existsDistanceKmForUpdate(
                        currentDistance.getRaceId(),
                        distanceKm,
                        distanceId
                )) {

            error =
                    "Số kilomet đã tồn tại trong giải.";
        }

        if (error != null) {
            request.setAttribute("error", error);
            request.setAttribute("distance", distance);

            request.setAttribute(
                    "distanceKmValue",
                    distanceKmRaw
            );

            request.setAttribute(
                    "maxParticipantValue",
                    maxParticipantRaw
            );

            request.getRequestDispatcher(EDIT_VIEW)
                    .forward(request, response);

            return;
        }

        boolean updated =
                distanceDAO.updateDistance(
                        distance,
                        organizer.getUserId()
                );

        if (!updated) {
            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Không thể cập nhật cự ly."
            );
            return;
        }

        session.setAttribute(
                "successMessage",
                "Cập nhật cự ly thành công."
        );

        response.sendRedirect(
                request.getContextPath()
                + "/organizer/distances?raceId="
                + currentDistance.getRaceId()
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
