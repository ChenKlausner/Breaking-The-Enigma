package servlets;

import com.google.gson.Gson;
import constants.Constants;
import dto.CodeConfiguration;
import dto.MachineDetailsDto;
import engine.Engine;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "GetOriginalCodeConfigurationServlet", urlPatterns = "/original-code-configuration")
public class GetOriginalCodeConfigurationServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String usernameFromParameter = (String)request.getSession().getAttribute(Constants.USERNAME);
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            Engine engine = ServletUtils.getEngine(getServletContext(),usernameFromParameter);
            CodeConfiguration codeConfiguration = engine.getOriginalCodeConfig();
            String json = gson.toJson(codeConfiguration);
            out.println(json);
            out.flush();
        }
    }
}
