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
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Race;
import model.User;

@WebServlet(
        name = "OrganizerRaceCreateServlet",
        urlPatterns = {"/organizer/race/create"}
)
public class OrganizerRaceCreateServlet
        extends HttpServlet {

    private static final String CREATE_VIEW =
            "/views/organizer/race-create.jsp";

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher(CREATE_VIEW)
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

        String raceName =
                normalize(request.getParameter("raceName"));

        String description =
                normalize(request.getParameter("description"));

        String location =
                normalize(request.getParameter("location"));

        String startDateRaw =
                normalize(request.getParameter("startDate"));

        String endDateRaw =
                normalize(request.getParameter("endDate"));

        String registrationDeadlineRaw =
                normalize(
                        request.getParameter(
                                "registrationDeadline"
                        )
                );

        keepOldValues(
                request,
                raceName,
                description,
                location,
                startDateRaw,
                endDateRaw,
                registrationDeadlineRaw
        );

        String error = validateText(
                raceName,
                description,
                location,
                startDateRaw,
                endDateRaw,
                registrationDeadlineRaw
        );

        if (error != null) {
            request.setAttribute("error", error);

            request.getRequestDispatcher(CREATE_VIEW)
                    .forward(request, response);

            return;
        }

        LocalDateTime startDate;
        LocalDateTime endDate;
        LocalDateTime registrationDeadline;

        try {
            startDate =
                    LocalDateTime.parse(startDateRaw);

            endDate =
                    LocalDateTime.parse(endDateRaw);

            registrationDeadline =
                    LocalDateTime.parse(
                            registrationDeadlineRaw
                    );

        } catch (DateTimeParseException exception) {
            request.setAttribute(
                    "error",
                    "Ngày giờ không đúng định dạng."
            );

            request.getRequestDispatcher(CREATE_VIEW)
                    .forward(request, response);

            return;
        }

        error = validateDate(
                startDate,
                endDate,
                registrationDeadline
        );

        if (error != null) {
            request.setAttribute("error", error);

            request.getRequestDispatcher(CREATE_VIEW)
                    .forward(request, response);

            return;
        }

        Race race = new Race();

        race.setCreatedByUserId(
                organizer.getUserId()
        );

        race.setRaceName(raceName);
        race.setDescription(description);
        race.setLocation(location);
        race.setStartDate(startDate);
        race.setEndDate(endDate);

        race.setRegistrationDeadline(
                registrationDeadline
        );

        RaceDAO raceDAO = new RaceDAO();

        try {
            int newRaceId =
                    raceDAO.createRace(race);

            if (newRaceId <= 0) {
                request.setAttribute(
                        "error",
                        "Tạo giải thất bại."
                );

                request.getRequestDispatcher(CREATE_VIEW)
                        .forward(request, response);

                return;
            }

            session.setAttribute(
                    "successMessage",
                    "Tạo giải chạy thành công."
            );

            response.sendRedirect(
                    request.getContextPath()
                    + "/organizer/races"
            );

        } catch (RuntimeException exception) {
            throw new ServletException(
                    "Không thể tạo giải chạy.",
                    exception
            );
        }
    }

    private String validateText(
            String raceName,
            String description,
            String location,
            String startDate,
            String endDate,
            String registrationDeadline) {

        if (raceName.isEmpty()) {
            return "Tên giải không được để trống.";
        }

        if (raceName.length() > 100) {
            return "Tên giải không được vượt quá 100 ký tự.";
        }

        if (description.length() > 3000) {
            return "Mô tả không được vượt quá 3000 ký tự.";
        }

        if (location.isEmpty()) {
            return "Địa điểm không được để trống.";
        }

        if (location.length() > 255) {
            return "Địa điểm không được vượt quá 255 ký tự.";
        }

        if (startDate.isEmpty()) {
            return "Vui lòng nhập ngày bắt đầu.";
        }

        if (endDate.isEmpty()) {
            return "Vui lòng nhập ngày kết thúc.";
        }

        if (registrationDeadline.isEmpty()) {
            return "Vui lòng nhập hạn đăng ký.";
        }

        return null;
    }

    private String validateDate(
            LocalDateTime startDate,
            LocalDateTime endDate,
            LocalDateTime registrationDeadline) {

        LocalDateTime now =
                LocalDateTime.now();

        if (!startDate.isAfter(now)) {
            return "Ngày bắt đầu phải lớn hơn thời điểm hiện tại.";
        }

        if (endDate.isBefore(startDate)) {
            return "Ngày kết thúc không được trước ngày bắt đầu.";
        }

        if (!registrationDeadline.isAfter(now)) {
            return "Hạn đăng ký phải lớn hơn thời điểm hiện tại.";
        }

        if (registrationDeadline.isAfter(endDate)) {
            return "Hạn đăng ký không được sau ngày kết thúc.";
        }

        return null;
    }

    private void keepOldValues(
            HttpServletRequest request,
            String raceName,
            String description,
            String location,
            String startDate,
            String endDate,
            String registrationDeadline) {

        request.setAttribute(
                "raceNameValue",
                raceName
        );

        request.setAttribute(
                "descriptionValue",
                description
        );

        request.setAttribute(
                "locationValue",
                location
        );

        request.setAttribute(
                "startDateValue",
                startDate
        );

        request.setAttribute(
                "endDateValue",
                endDate
        );

        request.setAttribute(
                "registrationDeadlineValue",
                registrationDeadline
        );
    }

    private String normalize(String value) {
        return value == null
                ? ""
                : value.trim();
    }
}
