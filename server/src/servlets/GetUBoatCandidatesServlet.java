package servlets;

import battlefield.Battlefield;
import battlefield.BattlefieldManager;
import com.google.gson.Gson;
import constants.Constants;
import dto.UBoatResponse;
import enums.ContestStatus;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javafx.application.Platform;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "GetUBoatCandidatesServlet", urlPatterns = "/get-uBoat-candidates")
public class GetUBoatCandidatesServlet extends HttpServlet {
    @Override
    synchronized protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            String uboatUserName = (String) request.getSession().getAttribute(Constants.USERNAME);
            BattlefieldManager battlefieldManager = ServletUtils.getBattlefieldManager(getServletContext());
            Battlefield battlefield = battlefieldManager.getBattlefieldMap().get(uboatUserName);
            Gson gson = new Gson();
            UBoatResponse uBoatResponse = null;
            if (!battlefield.isEmptyUBoatResponseList()) {
                uBoatResponse = battlefield.takeUBoaResponse();
            }

            if (uBoatResponse != null) {
                for (int i = 0; i < uBoatResponse.getCandidates().size(); i++) {
                    String allieName = uBoatResponse.getAllieUserName();
                    String candidate = uBoatResponse.getCandidates().get(i).getDecryptMsg();
                    if (candidate.equals(battlefield.getInputMsg().toUpperCase()) && !battlefield.isFoundWinner()) {
                        uBoatResponse.setWinner(true);
                        battlefield.setWinnerName(allieName);
                        battlefield.setFoundWinner(true);
                        battlefield.setContestStatus(ContestStatus.DONE);
                        break;
                    }
                }
            }

            String json = gson.toJson(uBoatResponse);
            out.println(json);
            out.flush();
        }
    }
}
