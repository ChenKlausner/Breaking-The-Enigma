package servlets;

import battlefield.Battlefield;
import battlefield.BattlefieldManager;
import com.google.gson.Gson;
import constants.Constants;
import dto.AllieDataDto;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import users.Allie;
import users.UserManager;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@WebServlet(name = "GetActiveAlliesTeamsServlet", urlPatterns = "/allies-teams")
public class GetActiveAlliesTeamsServlet extends HttpServlet {

    @Override
    synchronized protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            UserManager userManager = ServletUtils.getUserManager(getServletContext());
            String usernameFromParameter = (String) request.getSession().getAttribute(Constants.USERNAME);
            if (userManager.getUsersMap().get(usernameFromParameter).equals("Allies")) {
                usernameFromParameter = request.getParameter("uboatName");
            }
            BattlefieldManager battlefieldManager = ServletUtils.getBattlefieldManager(getServletContext());
            List<AllieDataDto> allieDataDtoList = new ArrayList<>();
            Battlefield battlefield = battlefieldManager.getBattlefieldMap().get(usernameFromParameter);
            if (battlefield != null) {
                Set<String> signAllies = battlefield.getSignedAllies();
                for (String allie : signAllies) {
                    Allie allie1 = userManager.getAlliesUserMap().get(allie);
                    allieDataDtoList.add(new AllieDataDto(allie, allie1.getAgentMembers().size(), allie1.getTaskSize()));
                }
            }
            String json = gson.toJson(allieDataDtoList);
            out.println(json);
            out.flush();
        }
    }
}
