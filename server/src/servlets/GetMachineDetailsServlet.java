package servlets;

import com.google.gson.Gson;
import constants.Constants;
import dto.MachineDetailsDto;
import engine.Engine;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;
import java.io.IOException;
import java.io.PrintWriter;


@WebServlet(name = "MachineDetailsServlet", urlPatterns = "/machine-details")
public class GetMachineDetailsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String usernameFromParameter = (String)request.getSession().getAttribute(Constants.USERNAME);
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            Engine engine = ServletUtils.getEngine(getServletContext(),usernameFromParameter);
            MachineDetailsDto machineDetailsDto = engine.getMachineDetails();
            String json = gson.toJson(machineDetailsDto);
            out.println(json);
            out.flush();
        }
    }
}
