package servlets;

import battlefield.Battlefield;
import battlefield.BattlefieldManager;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "GetSecretMsgServlet", urlPatterns = "/getSecretMsg")
public class GetSecretMsgServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        String uboatName = request.getParameter("uboatName");
        BattlefieldManager battlefieldManager = ServletUtils.getBattlefieldManager(getServletContext());
        Battlefield battlefield = battlefieldManager.getBattlefieldMap().get(uboatName);
        out.print(battlefield.getSecretMsg());
        out.flush();
    }
}
