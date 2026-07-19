package controller.publicpage;

import dal.DistanceDAO;
import dal.RaceDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import model.DistanceKM;
import model.Race;

/**
 * Author: PhucNTHE173021
 *
 * Version: 1
 *
 * Last Update Date: 17/7/2026
 *
 * Purpose: hien thi chi tiet giai va danh sach cu ly cong khai
 */
public class RaceDetailServlet extends HttpServlet {

    private static final String RACE_DETAIL_VIEW
            = "/views/public/race-detail.jsp";

    //lay chi tiet giai, danh sach cu ly va trang thai mo dang ky
    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        //raceId bat buoc la mot so nguyen duong
        int raceId;
        try {
            raceId = Integer.parseInt(request.getParameter("id"));
            if (raceId <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException exception) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Race id khong hop le.");
            return;
        }

        //lay giai cong khai, khong lay giai PENDING hoac REJECTED
        RaceDAO raceDAO = new RaceDAO();
        Race race = raceDAO.getPublicRaceById(raceId);

        if (race == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND,
                    "Khong tim thay giai chay.");
            return;
        }

        //lay cac cu ly thuoc giai de hien thi cho Guest va Runner
        DistanceDAO distanceDAO = new DistanceDAO();
        List<DistanceKM> distances
                = distanceDAO.getPublicDistancesByRace(raceId);

        LocalDateTime now = LocalDateTime.now();

        //chi mo dang ky khi giai hop le va chua qua han dang ky
        boolean registrationOpen
                = ("APPROVED".equalsIgnoreCase(race.getStatus())
                || "OPEN".equalsIgnoreCase(race.getStatus()))
                && race.getRegistrationDeadline() != null
                && !now.isAfter(race.getRegistrationDeadline())
                && race.getStartDate() != null
                && now.isBefore(race.getStartDate());

        request.setAttribute("race", race);
        request.setAttribute("distances", distances);
        request.setAttribute("registrationOpen", registrationOpen);

        //forward toan bo du lieu chi tiet sang JSP
        request.getRequestDispatcher(RACE_DETAIL_VIEW)
                .forward(request, response);
    }
}
