package servlets;

import agent.AgentManager;
import agent.AgentTask;
import battlefield.BattlefieldManager;
import com.google.gson.Gson;
import constants.Constants;
import decryptionManager.DecryptionManager;
import dto.Specification;
import engine.Engine;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import users.UserManager;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "GetAgentTaskServlet", urlPatterns = "/get-agent-task")
public class GetAgentTaskServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            UserManager userManager = ServletUtils.getUserManager(getServletContext());
            String allieUserName = (String) request.getSession().getAttribute(Constants.ALLIE_TEAM_NAME);
            String agentUserName = (String) request.getSession().getAttribute(Constants.USERNAME);
            Integer amountOfMissionsToTake = userManager.getAgentUserMap().get(agentUserName).getNumOfTasks();
            DecryptionManager decryptionManager = ServletUtils.getDM(getServletContext(), allieUserName);
            synchronized (this) {
                List<AgentTask> agentTaskList = new ArrayList<>();
                for (int i = 0; i < amountOfMissionsToTake; i++) {
                    if (decryptionManager.isEmptyAgentTaskBlockingDeque()) {
                        break;
                    }
                    try {
                        AgentTask agentTask = (AgentTask) decryptionManager.getAgentTaskBlockingDeque().take();
                        agentTaskList.add(agentTask);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                String json = gson.toJson(agentTaskList);
                out.println(json);
                out.flush();
            }
        }
    }
}
