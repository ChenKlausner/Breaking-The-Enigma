package servlets;

import com.google.gson.Gson;
import constants.Constants;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import users.UserManager;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "CheckIfAgentNeedToStopServlet", urlPatterns = "/contest-done")
public class CheckIfAgentNeedToStopServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            UserManager userManager = ServletUtils.getUserManager(getServletContext());
            String allieUserName = (String) request.getSession().getAttribute(Constants.ALLIE_TEAM_NAME);
            Boolean done =  userManager.getAlliesUserMap().get(allieUserName).isDone();
            Gson gson = new Gson();
            String json = gson.toJson(done);
            out.println(json);
            out.flush();
        }
    }
}
