package servlets;

import battlefield.Battlefield;
import battlefield.BattlefieldManager;
import com.google.gson.Gson;
import dto.BattlefieldDto;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet(name = "GetContestsDataServlet", urlPatterns = "/contestDataRefresher")
public class GetContestsDataServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            BattlefieldManager battlefieldManager = ServletUtils.getBattlefieldManager(getServletContext());
            List<BattlefieldDto> battlefieldDtoList = new ArrayList<>();
            for (Map.Entry<String, Battlefield> entry : battlefieldManager.getBattlefieldMap().entrySet()) {
                Battlefield battlefield = entry.getValue();
                battlefieldDtoList.add(new BattlefieldDto(battlefield.getBattleName(),battlefield.getuBoatUserName(),battlefield.getContestStatus(),battlefield.getDifficultyLevel(),battlefield.getListedTeamsVsNeededTeams()));
            }
            String json = gson.toJson(battlefieldDtoList);
            out.println(json);
            out.flush();
        }
    }
}
