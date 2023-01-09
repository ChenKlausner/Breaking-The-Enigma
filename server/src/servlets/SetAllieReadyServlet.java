package servlets;

import constants.Constants;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import users.UserManager;
import utils.ServletUtils;

import java.io.IOException;


@WebServlet(name = "SetAllieReadyServlet", urlPatterns = "/allie-ready")
public class SetAllieReadyServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        synchronized (this) {
            UserManager userManager = ServletUtils.getUserManager(getServletContext());
            String usernameFromParameter = (String) request.getSession().getAttribute(Constants.USERNAME);
            Integer taskSize = Integer.valueOf(request.getParameter("taskSize"));
            userManager.getAlliesUserMap().get(usernameFromParameter).setReady(true);
            if (userManager.getAlliesUserMap().get(usernameFromParameter).getAgentMembers().size() < 1) {
                userManager.getAlliesUserMap().get(usernameFromParameter).setReady(false);
            }
            userManager.getAlliesUserMap().get(usernameFromParameter).setTaskSize(taskSize);
            response.setStatus(userManager.getAlliesUserMap().get(usernameFromParameter).isReady() ? HttpServletResponse.SC_OK : HttpServletResponse.SC_NOT_IMPLEMENTED);
        }
    }
}
