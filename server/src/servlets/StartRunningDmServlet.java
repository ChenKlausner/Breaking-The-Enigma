package servlets;

import battlefield.BattlefieldManager;
import constants.Constants;
import decryptionManager.DecryptionManager;
import decryptionManager.DifficultyLevel;
import engine.Engine;
import jakarta.servlet.ServletContext;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import users.UserManager;
import utils.ServletUtils;

import java.io.IOException;


@WebServlet(name = "StartRunningDmServlet", urlPatterns = "/start-dm")
public class StartRunningDmServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        synchronized (this){
            UserManager userManager = ServletUtils.getUserManager(getServletContext());
            BattlefieldManager battlefieldManager = ServletUtils.getBattlefieldManager(getServletContext());
            String allieUserName = (String) request.getSession().getAttribute(Constants.USERNAME);
            String uBoatUserName = request.getParameter("uboatName");
            String secretMsg = battlefieldManager.getBattlefieldMap().get(uBoatUserName).getSecretMsg();
            Integer taskSize = userManager.getAlliesUserMap().get(allieUserName).getTaskSize();
            DifficultyLevel difficultyLevel = DifficultyLevel.valueOf(battlefieldManager.getBattlefieldMap().get(uBoatUserName).getDifficultyLevel().toUpperCase());
            Engine engine = ServletUtils.getEngine(getServletContext(), uBoatUserName);
            DecryptionManager decryptionManager = ServletUtils.getDM(getServletContext(),allieUserName,engine.getDeepCopyOfEngine(),secretMsg,taskSize,difficultyLevel);
            Runnable generateAgentTasks = () -> {
                decryptionManager.start();
            };
            new Thread(generateAgentTasks, "pushAgentTasksThread").start();
        }
    }
}
