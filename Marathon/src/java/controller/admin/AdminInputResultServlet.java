package controller.admin;

import dal.ResultDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

/**
 * Author: anhdu
 *
 * Version: 1
 *
 * Last Update Date: 17/7/2026
 *
 * Purpose: xu ly nhap ket qua chay cho Admin
 */
public class AdminInputResultServlet extends HttpServlet {

    private static final String INPUT_RESULT_VIEW
            = "/views/admin/input-result.jsp";

    //hien thi form nhap ket qua
    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher(INPUT_RESULT_VIEW)
                .forward(request, response);
    }

    //doc form, validate va luu ket qua FINISHED
    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();

        int registrationId = parsePositiveInt(
                request.getParameter("registrationId"));
        String finishTimeRaw = trim(request.getParameter("finishTime"));

        if (registrationId <= 0 || finishTimeRaw.isEmpty()) {
            session.setAttribute("errorMessage",
                    "Du lieu ket qua khong hop le.");
            response.sendRedirect(request.getContextPath()
                    + "/admin/input-result");
            return;
        }

        LocalTime finishTime;
        try {
            finishTime = LocalTime.parse(finishTimeRaw);
        } catch (DateTimeParseException exception) {
            session.setAttribute("errorMessage",
                    "Thoi gian hoan thanh phai co dang HH:mm hoac HH:mm:ss.");
            response.sendRedirect(request.getContextPath()
                    + "/admin/input-result");
            return;
        }

        ResultDAO resultDAO = new ResultDAO();
        boolean saved = resultDAO.saveFinishedResult(
                registrationId, finishTime);

        if (saved) {
            session.setAttribute("successMessage",
                    "Nhap ket qua va cap nhat thu hang thanh cong.");
        } else {
            session.setAttribute("errorMessage",
                    "Chi co the nhap ket qua cho don APPROVED hop le.");
        }

        response.sendRedirect(request.getContextPath()
                + "/admin/input-result");
    }

    //chuan hoa chuoi parameter va tranh NullPointerException
    private String trim(String value) {
        return value == null ? "" : value.trim();
    }

    //parse so nguyen duong, sai dinh dang thi tra ve -1
    private int parsePositiveInt(String value) {
        try {
            int number = Integer.parseInt(value);
            return number > 0 ? number : -1;
        } catch (NumberFormatException exception) {
            return -1;
        }
    }
}
