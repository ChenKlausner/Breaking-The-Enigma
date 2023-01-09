package servlets;

import battlefield.Battlefield;
import battlefield.BattlefieldManager;
import com.google.gson.Gson;
import constants.Constants;
import dto.WinnerResponse;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import users.UserManager;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "CheckIfFoundWinnerServlet", urlPatterns = "/allie-check-winner")
public class CheckIfFoundWinnerServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            UserManager userManager = ServletUtils.getUserManager(getServletContext());
            String allieUserName = (String) request.getSession().getAttribute(Constants.USERNAME);
            String uBoatName = request.getParameter("UBoatName");
            userManager.getAlliesUserMap().get(allieUserName).setDone(true);
            BattlefieldManager battlefieldManager = ServletUtils.getBattlefieldManager(getServletContext());
            Battlefield battlefield = battlefieldManager.getBattlefieldMap().get(uBoatName);
            WinnerResponse winnerResponse = new WinnerResponse(battlefield.isFoundWinner(), battlefield.getWinnerName());
            Gson gson = new Gson();
            String json = gson.toJson(winnerResponse);
            out.println(json);
            out.flush();
        }
    }
}
