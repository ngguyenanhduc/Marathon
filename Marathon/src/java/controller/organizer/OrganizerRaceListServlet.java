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
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Race;
import model.User;

@WebServlet(
        name = "OrganizerRaceListServlet",
        urlPatterns = {"/organizer/races"}
)
public class OrganizerRaceListServlet
        extends HttpServlet {

 @Override
protected void doGet(
        HttpServletRequest request,
        HttpServletResponse response)
        throws ServletException, IOException {

    User organizer =
            (User) request.getSession()
                    .getAttribute("user");

    RaceDAO raceDAO = new RaceDAO();

    List<Race> races =
            raceDAO.getRacesByOrganizerId(
                    organizer.getUserId()
            );

    request.setAttribute("races", races);

    request.getRequestDispatcher(
            "/views/organizer/race-list.jsp"
    ).forward(request, response);
}
}