package servlets;

import battlefield.Battlefield;
import battlefield.BattlefieldManager;
import constants.Constants;
import dto.AllieDataDto;
import enums.ContestStatus;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import users.Allie;
import users.UserManager;
import utils.ServletUtils;
import java.io.IOException;
import java.util.Set;


@WebServlet(name = "SetUBoatReadyServlet", urlPatterns = "/uboat_ready")
public class SetUBoatReadyServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String usernameFromParameter = (String) request.getSession().getAttribute(Constants.USERNAME);
        BattlefieldManager battlefieldManager = ServletUtils.getBattlefieldManager(getServletContext());
        String inputMsg = request.getParameter("inputMsg");
        String secretMsg = request.getParameter("secretMsg");
        Battlefield battlefield = battlefieldManager.getBattlefieldMap().get(usernameFromParameter);
        battlefield.setInputMsg(inputMsg);
        battlefield.setSecretMsg(secretMsg);
        battlefield.setUBoatReady(true);
        battlefield.setWinnerName("");
        battlefield.setFoundWinner(false);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
