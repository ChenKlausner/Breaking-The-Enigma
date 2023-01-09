package servlets;

import battlefield.Battlefield;
import battlefield.BattlefieldManager;
import com.google.gson.Gson;
import constants.Constants;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "IsAgentNeedToStartWorkingServlet", urlPatterns = "/start-agent-work")
public class IsAgentNeedToStartWorkingServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            String allieTeamName = (String)request.getSession().getAttribute(Constants.ALLIE_TEAM_NAME);
            boolean result = getServletContext().getAttribute(allieTeamName) != null;
            String json = gson.toJson(result);
            out.println(json);
            out.flush();
        }
    }
}
