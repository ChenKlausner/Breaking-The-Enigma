package servlets;

import battlefield.Battlefield;
import battlefield.BattlefieldManager;
import com.google.gson.Gson;
import constants.Constants;
import dto.BattlefieldDto;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import users.UserManager;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@WebServlet(name = "GetAgentContestDataServlet", urlPatterns = "/agent-contest-data")
public class GetAgentContestDataServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        String usernameFromParameter = (String) request.getSession().getAttribute(Constants.USERNAME);
        String userType = userManager.getUsersMap().get(usernameFromParameter);
        String allieUserName;
        if (userType.equals("Agent")) {
            allieUserName = (String) request.getSession().getAttribute(Constants.ALLIE_TEAM_NAME);
        } else {
            allieUserName = usernameFromParameter;
        }
        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            BattlefieldManager battlefieldManager = ServletUtils.getBattlefieldManager(getServletContext());
            BattlefieldDto battlefieldDto = null;
            for (Map.Entry<String, Battlefield> entry : battlefieldManager.getBattlefieldMap().entrySet()) {
                Battlefield battlefield = entry.getValue();
                if (battlefield.getSignedAllies().contains(allieUserName)) {
                    battlefieldDto = new BattlefieldDto(battlefield.getBattleName(), battlefield.getuBoatUserName(), battlefield.getContestStatus(), battlefield.getDifficultyLevel(), battlefield.getListedTeamsVsNeededTeams());
                }
            }
            String json = gson.toJson(battlefieldDto);
            out.println(json);
            out.flush();
        }
    }
}
