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
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.DistanceKM;
import model.User;

@WebServlet(
        name = "OrganizerDistanceDeleteServlet",
        urlPatterns = {"/organizer/distance/delete"}
)
public class OrganizerDistanceDeleteServlet
        extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.sendError(
                HttpServletResponse.SC_METHOD_NOT_ALLOWED,
                "Vui lòng sử dụng phương thức POST để xóa cự ly."
        );
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

        int raceId = distance.getRaceId();

        if (!distance.isRaceEditable()) {
            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Không thể xóa cự ly vì giải không còn ở trạng thái cho phép chỉnh sửa."
            );
            return;
        }

        boolean deleted =
                distanceDAO.deleteDistance(
                        distanceId,
                        organizer.getUserId()
                );

        if (!deleted) {
            session.setAttribute(
                    "errorMessage",
                    "Không thể xóa cự ly. Cự ly có thể đã có người đăng ký."
            );

            response.sendRedirect(
                    request.getContextPath()
                    + "/organizer/distances?raceId="
                    + raceId
            );
            return;
        }

        session.setAttribute(
                "successMessage",
                "Xóa cự ly thành công."
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

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
