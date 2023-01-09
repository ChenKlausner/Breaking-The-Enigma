package servlets;

import com.google.gson.Gson;
import constants.Constants;
import dto.AgentProgressDto;
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
import java.util.Set;

@WebServlet(name = "GetTeamAgentsProgressServlet", urlPatterns = "/alliApp-agent-team-progress")
public class GetTeamAgentsProgressServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            UserManager userManager = ServletUtils.getUserManager(getServletContext());
            String allieUserName = (String) request.getSession().getAttribute(Constants.USERNAME);
            Set<String> agentMembers = userManager.getAlliesUserMap().get(allieUserName).getAgentMembers();
            List<AgentProgressDto> agentProgressDtoList = new ArrayList<>();
            for (String agent : agentMembers) {
                AgentProgressDto agentProgressDto = userManager.getAgentUserMap().get(agent).getAgentProgress();
                if (agentProgressDto != null) {
                    agentProgressDtoList.add(agentProgressDto);
                }
            }
            Gson gson = new Gson();
            String json = gson.toJson(agentProgressDtoList);
            out.println(json);
            out.flush();
        }
    }
}
