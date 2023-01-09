package servlets;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import users.UserManager;
import utils.ServletUtils;


import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "GetAlliesUserNamesServlet", urlPatterns = "/allies-users")
public class GetAlliesUserNamesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            UserManager userManager = ServletUtils.getUserManager(getServletContext());
            String json = gson.toJson(userManager.getAlliesUserNamesSet());
            out.println(json);
            out.flush();
        }
    }
}
