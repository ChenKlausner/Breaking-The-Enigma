package servlets;

import battlefield.Battlefield;
import battlefield.BattlefieldManager;
import constants.Constants;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;
import java.io.IOException;


@WebServlet(name = "SignInContestServlet", urlPatterns = "/sign-to-contest")
public class SignInContestServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        BattlefieldManager battlefieldManager = ServletUtils.getBattlefieldManager(getServletContext());
        String uBoatUserName = request.getParameter("uBoatUserName");
        Battlefield battlefield = battlefieldManager.getBattlefieldMap().get(uBoatUserName);
        boolean succeeded = battlefield.addAllieTeam((String) request.getSession().getAttribute(Constants.USERNAME));
        response.setStatus(succeeded ? HttpServletResponse.SC_OK : HttpServletResponse.SC_NOT_IMPLEMENTED);
    }
}
