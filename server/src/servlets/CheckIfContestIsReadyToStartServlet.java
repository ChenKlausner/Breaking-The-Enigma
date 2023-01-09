package servlets;

import battlefield.Battlefield;
import battlefield.BattlefieldManager;
import com.google.gson.Gson;
import enums.ContestStatus;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import users.Allie;
import users.UserManager;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

@WebServlet(name = "CheckIfContestIsReadyToStartServlet", urlPatterns = "/contest-ready")
public class CheckIfContestIsReadyToStartServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            String uBoatUserName = request.getParameter("uboatName");
            BattlefieldManager battlefieldManager = ServletUtils.getBattlefieldManager(getServletContext());
            Battlefield battlefield = battlefieldManager.getBattlefieldMap().get(uBoatUserName);
            UserManager userManager = ServletUtils.getUserManager(getServletContext());

            boolean contestIsReady = battlefield.isUBoatReady();
            Set<String> signAllies = battlefield.getSignedAllies();
            for (String allie : signAllies) {
                Allie allie1 = userManager.getAlliesUserMap().get(allie);
                contestIsReady = contestIsReady && allie1.isReady();
            }
            battlefield.setReadyToStartContest(contestIsReady);

            if (battlefield.getSignedAllies().size() != battlefield.getNumOfAllies()){
                battlefield.setReadyToStartContest(false);
            }

            if (battlefield.isReadyToStartContest()){
                battlefield.setContestStatus(ContestStatus.ACTIVE);
            }

            boolean result = battlefield.isReadyToStartContest();
            String json = gson.toJson(result);
            out.println(json);
            out.flush();
        }
    }
}
