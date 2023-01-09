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



@WebServlet(name = "UpdateAgentProgressServlet", urlPatterns = "/agent-progress")
public class UpdateAgentProgressServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Gson gson = new Gson();
        String agentProgressJson = request.getParameter("agentProgressDto");
        String agentUserName = (String) request.getSession().getAttribute(Constants.USERNAME);
        AgentProgressDto agentProgressDto = gson.fromJson(agentProgressJson, AgentProgressDto.class);
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        userManager.getAgentUserMap().get(agentUserName).setAgentProgress(agentProgressDto);
    }
}
