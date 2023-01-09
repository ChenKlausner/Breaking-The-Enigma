package servlets;

import constants.Constants;
import engine.Engine;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;
import java.io.IOException;

@WebServlet(name = "setMachineSettingsAutomaticallyServlet", urlPatterns = "/machine-auto-configuration")
public class SetMachineSettingsAutomaticallyServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String usernameFromParameter = (String)request.getSession().getAttribute(Constants.USERNAME);
        Engine engine = ServletUtils.getEngine(getServletContext(),usernameFromParameter);
        engine.setMachineSettingsAutomaticallyEx3();
    }
}
