package servlets;

import battlefield.Battlefield;
import battlefield.BattlefieldManager;
import constants.Constants;
import engine.Engine;
import enums.ContestStatus;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import users.UserManager;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;


@WebServlet(name = "LogoutServlet", urlPatterns = "/logout")
public class LogoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String usernameFromSession = SessionUtils.getUsername(request);
        UserManager userManager = ServletUtils.getUserManager(getServletContext());

        Engine engine = ServletUtils.getEngine(getServletContext(), usernameFromSession);
        String battlefieldName = engine.getBattlefieldName();

        if (getServletContext().getAttribute(battlefieldName) != null) {
            getServletContext().removeAttribute(battlefieldName);
        }

        if (getServletContext().getAttribute(usernameFromSession) != null) {
            getServletContext().removeAttribute(usernameFromSession);
        }

        BattlefieldManager battlefieldManager = ServletUtils.getBattlefieldManager(getServletContext());
        battlefieldManager.removeBattlefield(usernameFromSession);

        if (usernameFromSession != null) {
            userManager.removeUser(usernameFromSession);
            request.getSession(false).setAttribute(Constants.USERNAME,null);
        }
    }
}
