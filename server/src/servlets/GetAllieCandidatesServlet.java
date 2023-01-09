package servlets;

import battlefield.Battlefield;
import battlefield.BattlefieldManager;
import com.google.gson.Gson;
import constants.Constants;
import dto.AllieResponse;
import dto.UBoatResponse;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import users.Allie;
import users.UserManager;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "GetAllieCandidatesServlet", urlPatterns = "/alliApp-get-candidates")
public class GetAllieCandidatesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            UserManager userManager = ServletUtils.getUserManager(getServletContext());
            String allieUserName = (String) request.getSession().getAttribute(Constants.USERNAME);
            String uBoatName = request.getParameter("UBoatName");
            BattlefieldManager battlefieldManager = ServletUtils.getBattlefieldManager(getServletContext());
            Battlefield battlefield = battlefieldManager.getBattlefieldMap().get(uBoatName);
            Gson gson = new Gson();
            AllieResponse allieResponse = null;
            Allie allie = userManager.getAlliesUserMap().get(allieUserName);
            if (!allie.isEmptyAllieResponseList()){
                allieResponse = allie.takeAllieResponse();
                battlefield.addUBoatResponse(new UBoatResponse(allieResponse.getCandidates(),allieUserName));
            }
            String json = gson.toJson(allieResponse);
            out.println(json);
            out.flush();
        }
    }
}
