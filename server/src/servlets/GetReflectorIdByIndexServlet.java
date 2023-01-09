package servlets;

import constants.Constants;
import engine.Engine;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "ReflectorIdByIndexServlet", urlPatterns = "/reflector-id")
public class GetReflectorIdByIndexServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String usernameFromParameter = (String)request.getSession().getAttribute(Constants.USERNAME);
        PrintWriter out = response.getWriter();
        Engine engine = ServletUtils.getEngine(getServletContext(),usernameFromParameter);
        String reflectorId = request.getParameter("index");
        out.print(engine.getReflectorId(Integer.parseInt(reflectorId)));
        out.flush();
    }
}
