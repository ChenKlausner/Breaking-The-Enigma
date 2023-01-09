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
import java.io.PrintWriter;

@WebServlet(name = "CheckValidDictionaryWordsServlet", urlPatterns = "/dictionaryContainWordsFromInput")
public class CheckValidDictionaryWordsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        String usernameFromParameter = (String)request.getSession().getAttribute(Constants.USERNAME);
        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            Engine engine = ServletUtils.getEngine(getServletContext(),usernameFromParameter);
            boolean result = engine.isDictionaryContainWordsFromInput(request.getParameter("text"));
            String json = gson.toJson(result);
            out.println(json);
            out.flush();
        }
    }
}
