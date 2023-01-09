package servlets;

import battlefield.Battlefield;
import battlefield.BattlefieldManager;
import com.google.gson.Gson;
import constants.Constants;
import enums.ContestStatus;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import users.Allie;
import users.UserManager;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

@WebServlet(name = "CheckIfAllieLogoutServlet", urlPatterns = "/allie-logout")
public class CheckIfAllieLogoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            String uBoatUserName = request.getParameter("UBoatName");
            BattlefieldManager battlefieldManager = ServletUtils.getBattlefieldManager(getServletContext());
            boolean result = false;
            if (battlefieldManager.getBattlefieldMap().get(uBoatUserName) == null) {
                result = true;
                UserManager userManager = ServletUtils.getUserManager(getServletContext());
                String allieUserName = (String) request.getSession().getAttribute(Constants.USERNAME);
                userManager.getAlliesUserMap().get(allieUserName).setCleanData(true);
                userManager.getAlliesUserMap().get(allieUserName).setReady(false);
                userManager.getAlliesUserMap().get(allieUserName).cleanAllieResponseList();

                userManager.getAlliesUserMap().get(allieUserName).setTaskSize(0);

                Set<String> agentMembers = userManager.getAlliesUserMap().get(allieUserName).getAgentMembers();
                for (String agent : agentMembers) {
                    userManager.getAgentUserMap().get(agent).setAgentProgress(null);
                }

                if (getServletContext().getAttribute(allieUserName) != null){
                    getServletContext().removeAttribute(allieUserName);
                }
            }

            String json = gson.toJson(result);
            out.println(json);
            out.flush();
        }
    }
}
