package servlets;

import battlefield.Battlefield;
import battlefield.BattlefieldManager;
import constants.Constants;
import dto.AgentProgressDto;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import users.UserManager;
import utils.ServletUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@WebServlet(name = "AllieConfirmCleanDataServlet", urlPatterns = "/ok-clean-press")
public class AllieConfirmCleanDataServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        String allieUserName = (String) request.getSession().getAttribute(Constants.USERNAME);
        String uboatUserName = request.getParameter("UBoatName");
        userManager.getAlliesUserMap().get(allieUserName).setCleanData(true);
        userManager.getAlliesUserMap().get(allieUserName).setReady(false);
        userManager.getAlliesUserMap().get(allieUserName).cleanAllieResponseList();
        userManager.getAlliesUserMap().get(allieUserName).setTaskSize(0);

        Set<String> agentMembers = userManager.getAlliesUserMap().get(allieUserName).getAgentMembers();
        for (String agent : agentMembers) {
            userManager.getAgentUserMap().get(agent).setAgentProgress(null);
        }

        BattlefieldManager battlefieldManager = ServletUtils.getBattlefieldManager(getServletContext());
        Battlefield battlefield = battlefieldManager.getBattlefieldMap().get(uboatUserName);
        battlefield.removeAllieTeam(allieUserName);
        battlefield.setWinnerName("");
        battlefield.setFoundWinner(false);

        if (getServletContext().getAttribute(allieUserName) != null){
            getServletContext().removeAttribute(allieUserName);
        }
    }
}
