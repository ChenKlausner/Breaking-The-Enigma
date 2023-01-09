package servlets;

import com.google.gson.Gson;
import constants.Constants;
import dto.CodeConfiguration;
import engine.Engine;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;
import java.io.IOException;


@WebServlet(name = "setMachineSettingsManuallyServlet", urlPatterns = "/machine-manual-configuration")
public class SetMachineSettingsManuallyServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String usernameFromParameter = (String)request.getSession().getAttribute(Constants.USERNAME);
        Gson gson = new Gson();
        Engine engine = ServletUtils.getEngine(getServletContext(),usernameFromParameter);
        String jsonCodeConfiguration = request.getParameter("codeConfiguration");
        CodeConfiguration codeConfiguration = gson.fromJson(jsonCodeConfiguration,CodeConfiguration.class);
        engine.setMachineSettingsManually(codeConfiguration);
    }
}
