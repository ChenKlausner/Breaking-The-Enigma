package servlets;

import com.google.gson.Gson;
import constants.Constants;
import dto.AgentProgressDto;
import dto.AgentResponse;
import dto.AllieResponse;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import users.UserManager;
import utils.ServletUtils;

import java.io.IOException;

@WebServlet(name = "UpdateAgentResponseServlet", urlPatterns = "/agent-response")
public class UpdateAgentResponseServlet extends HttpServlet {
    @Override
    synchronized protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        String agentUserName = (String) request.getSession().getAttribute(Constants.USERNAME);
        String allieUserName = (String) request.getSession().getAttribute(Constants.ALLIE_TEAM_NAME);
        Gson gson = new Gson();
        String agentResponseJson = request.getParameter("agentResponse");
        AgentResponse agentResponse = gson.fromJson(agentResponseJson, AgentResponse.class);
        userManager.getAlliesUserMap().get(allieUserName).addAllieResponse(new AllieResponse(agentResponse.getCandidates(),agentUserName));
    }
}
