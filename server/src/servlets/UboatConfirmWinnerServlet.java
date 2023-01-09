package servlets;

import battlefield.Battlefield;
import battlefield.BattlefieldManager;
import constants.Constants;
import enums.ContestStatus;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;

import java.io.IOException;

@WebServlet(name = "UboatConfirmWinnerServlet", urlPatterns = "/ok-uboat-press")
public class UboatConfirmWinnerServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String usernameFromParameter = (String) request.getSession().getAttribute(Constants.USERNAME);
        BattlefieldManager battlefieldManager = ServletUtils.getBattlefieldManager(getServletContext());
        Battlefield battlefield = battlefieldManager.getBattlefieldMap().get(usernameFromParameter);
        battlefield.setContestStatus(ContestStatus.WAITING);
        battlefield.setSecretMsg("");
        battlefield.setInputMsg("");
        battlefield.setUBoatReady(false);
        battlefield.setReadyToStartContest(false);
        battlefield.clearResponseList();
    }
}
