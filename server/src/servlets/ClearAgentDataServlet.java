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

@WebServlet(name = "ClearAgentDataServlet", urlPatterns = "/clean-data")
public class ClearAgentDataServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            UserManager userManager = ServletUtils.getUserManager(getServletContext());
            String allieUserName = (String) request.getSession().getAttribute(Constants.ALLIE_TEAM_NAME);
            Boolean clean =  userManager.getAlliesUserMap().get(allieUserName).isCleanData();
            Gson gson = new Gson();
            String json = gson.toJson(clean);
            out.println(json);
            out.flush();
            if (clean){
                userManager.getAlliesUserMap().get(allieUserName).setCleanData(false);
            }
        }
    }
}
