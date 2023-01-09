package servlets;

import com.google.gson.Gson;
import constants.Constants;
import dto.AgentDataDto;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import users.Agent;
import users.UserManager;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@WebServlet(name = "GetTeamAgentDataServlet", urlPatterns = "/agentTeamRefresher")
public class GetTeamAgentDataServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            UserManager userManager = ServletUtils.getUserManager(getServletContext());
            String usernameFromParameter = (String)request.getSession().getAttribute(Constants.USERNAME);
            List<AgentDataDto> agentDataDtoList = new ArrayList<>();
            for (Map.Entry<String, Agent> entry : userManager.getAgentUserMap().entrySet()) {
                Agent agent = entry.getValue();
                if (agent.getAlliesTeamName().equals(usernameFromParameter)){
                    agentDataDtoList.add(new AgentDataDto(agent.getUserName(),agent.getNumOfThreads(),agent.getNumOfTasks()));
                }
            }
            String json = gson.toJson(agentDataDtoList);
            out.println(json);
            out.flush();
        }
    }
}
