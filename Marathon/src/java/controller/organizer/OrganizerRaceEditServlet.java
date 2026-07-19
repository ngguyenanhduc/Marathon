/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 *Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
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
        name = "OrganizerRaceEditServlet",
        urlPatterns = {"/organizer/race/edit"}
)
public class OrganizerRaceEditServlet
        extends HttpServlet {

    private static final String EDIT_VIEW =
            "/views/organizer/race-edit.jsp";

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
                    "Giải đã được duyệt nên không thể chỉnh sửa."
            );
            return;
        }

        request.setAttribute("race", race);

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

        String error = validateText(
                raceName,
                description,
                location,
                startDateRaw,
                endDateRaw,
                registrationDeadlineRaw
        );

        LocalDateTime startDate = null;
        LocalDateTime endDate = null;
        LocalDateTime registrationDeadline = null;

        if (error == null) {
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
                error = "Ngày giờ không đúng định dạng.";
            }
        }

        if (error == null) {
            error = validateDate(
                    startDate,
                    endDate,
                    registrationDeadline
            );
        }

        Race race = new Race();

        race.setRaceId(raceId);
        race.setRaceName(raceName);
        race.setDescription(description);
        race.setLocation(location);
        race.setStartDate(startDate);
        race.setEndDate(endDate);
        race.setRegistrationDeadline(
                registrationDeadline
        );

        if (error != null) {
            request.setAttribute("error", error);
            request.setAttribute("race", race);

            request.getRequestDispatcher(EDIT_VIEW)
                    .forward(request, response);

            return;
        }

        RaceDAO raceDAO = new RaceDAO();

        boolean updated =
                raceDAO.updateRace(
                        race,
                        organizer.getUserId()
                );

        if (!updated) {
            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Không thể cập nhật giải. Giải có thể đã được duyệt hoặc không thuộc quyền quản lý của bạn."
            );
            return;
        }

        session.setAttribute(
                "successMessage",
                "Cập nhật giải chạy thành công."
        );

        response.sendRedirect(
                request.getContextPath()
                + "/organizer/race/detail?raceId="
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
