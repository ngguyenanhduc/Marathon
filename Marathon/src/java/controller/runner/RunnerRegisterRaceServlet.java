package controller.runner;

import dal.DistanceDAO;
import dal.RaceDAO;
import dal.RegistrationDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;
import model.DistanceKM;
import model.Race;
import model.User;

/**
 * Author: PhucNTHE173021
 *
 * Version: 1
 *
 * Last Update Date: 17/7/2026
 *
 * Purpose: tao don dang ky cu ly cho Runner
 */
public class RunnerRegisterRaceServlet extends HttpServlet {

    //khong hien thi form rieng, GET se quay ve danh sach giai
    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/races");
    }

    //kiem tra dieu kien va tao don dang ky cu ly cho Runner
    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        //distanceId bat buoc la mot so nguyen duong
        int distanceId;
        try {
            distanceId = Integer.parseInt(
                    request.getParameter("distanceId"));
            if (distanceId <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException exception) {
            session.setAttribute("errorMessage", "Cu ly khong hop le.");
            response.sendRedirect(request.getContextPath() + "/races");
            return;
        }

        //lay cu ly cong khai de tranh dang ky vao giai chua cong bo
        DistanceDAO distanceDAO = new DistanceDAO();
        DistanceKM distance = distanceDAO.getPublicDistanceById(distanceId);

        if (distance == null) {
            session.setAttribute("errorMessage", "Khong tim thay cu ly.");
            response.sendRedirect(request.getContextPath() + "/races");
            return;
        }

        //lay thong tin giai de kiem tra trang thai va thoi gian dang ky
        RaceDAO raceDAO = new RaceDAO();
        Race race = raceDAO.getPublicRaceById(distance.getRaceId());

        if (race == null) {
            session.setAttribute("errorMessage", "Khong tim thay giai chay.");
            response.sendRedirect(request.getContextPath() + "/races");
            return;
        }

        String redirectDetail = request.getContextPath()
                + "/race-detail?id=" + race.getRaceId();
        String status = race.getStatus();

        //chi giai APPROVED hoac OPEN moi nhan dang ky
        if (!("APPROVED".equalsIgnoreCase(status)
                || "OPEN".equalsIgnoreCase(status))) {
            session.setAttribute("errorMessage",
                    "Giai chay hien khong mo dang ky.");
            response.sendRedirect(redirectDetail);
            return;
        }

        LocalDateTime now = LocalDateTime.now();

        //chan dang ky khi da qua deadline hoac giai da bat dau
        if (race.getRegistrationDeadline() == null
                || now.isAfter(race.getRegistrationDeadline())) {
            session.setAttribute("errorMessage",
                    "Giai chay da het han dang ky.");
            response.sendRedirect(redirectDetail);
            return;
        }

        if (race.getStartDate() == null
                || !now.isBefore(race.getStartDate())) {
            session.setAttribute("errorMessage",
                    "Giai chay da bat dau.");
            response.sendRedirect(redirectDetail);
            return;
        }

        //so Runner da duyet khong duoc vuot qua gioi han cu ly
        if (distance.getApprovedRegistrationCount()
                >= distance.getMaxParticipant()) {
            session.setAttribute("errorMessage", "Cu ly da du nguoi.");
            response.sendRedirect(redirectDetail);
            return;
        }

        //kiem tra don trung truoc khi tao
        //van giu unique constraint trong database
        RegistrationDAO registrationDAO = new RegistrationDAO();
        if (registrationDAO.hasRegistration(user.getUserId(), distanceId)) {
            session.setAttribute("errorMessage",
                    "Ban da dang ky cu ly nay truoc do.");
            response.sendRedirect(redirectDetail);
            return;
        }

        boolean created = registrationDAO.createRegistration(
                user.getUserId(), distanceId);

        if (!created) {
            session.setAttribute("errorMessage",
                    "Khong the dang ky cu ly. Vui long kiem tra lai.");
            response.sendRedirect(redirectDetail);
            return;
        }

        //redirect den lich su dang ky sau khi tao don thanh cong
        session.setAttribute("successMessage",
                "Dang ky cu ly thanh cong. Don dang cho Organizer duyet.");
        response.sendRedirect(
                request.getContextPath() + "/runner/registrations");
    }
}
