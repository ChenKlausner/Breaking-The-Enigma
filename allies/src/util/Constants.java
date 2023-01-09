package util;

import com.google.gson.Gson;

public class Constants {

    // global constants
    public final static String LINE_SEPARATOR = System.getProperty("line.separator");
    public final static String JHON_DOE = "<Anonymous>";
    public final static int REFRESH_RATE = 2000;
    public final static String CHAT_LINE_FORMATTING = "%tH:%tM:%tS | %.10s: %s%n";

    // fxml locations
    public final static String MAIN_PAGE_FXML_RESOURCE_LOCATION = "/chat/client/component/main/chat-app-main.fxml";
    public final static String LOGIN_PAGE_FXML_RESOURCE_LOCATION = "/subComponent/login/login.fxml";
    public final static String BODY_FXML_RESOURCE_LOCATION = "/subComponent/body/body.fxml";

    // Server resources locations
    public final static String BASE_DOMAIN = "localhost";
    private final static String BASE_URL = "http://" + BASE_DOMAIN + ":8080";
    //private final static String CONTEXT_PATH = "/server_Web_exploded";
    private final static String CONTEXT_PATH = "/server_Web";
    private final static String FULL_SERVER_PATH = BASE_URL + CONTEXT_PATH;
    public final static String USER_LOGIN = FULL_SERVER_PATH + "/user-login";
    public final static String CONTEST_DATA = FULL_SERVER_PATH + "/contestDataRefresher";
    public final static String AGENT_TEAM = FULL_SERVER_PATH + "/agentTeamRefresher";
    public final static String SIGN_TO_CONTEST = FULL_SERVER_PATH + "/sign-to-contest";
    public final static String ALLIES_TEAMS = FULL_SERVER_PATH + "/allies-teams";
    public final static String ALLIE_READY = FULL_SERVER_PATH + "/allie-ready";
    public final static String CONTEST_READY = FULL_SERVER_PATH + "/contest-ready";
    public final static String START_DM = FULL_SERVER_PATH + "/start-dm";
    public final static String AGENTS_TEAMS_PROGRESS = FULL_SERVER_PATH + "/alliApp-agent-team-progress";
    public final static String GET_CANDIDATES = FULL_SERVER_PATH + "/alliApp-get-candidates";
    public final static String AGENT_CONTEST_DATA = FULL_SERVER_PATH + "/agent-contest-data";
    public final static String SECRET_MSG = FULL_SERVER_PATH + "/getSecretMsg";
    public final static String WINNER_CHECK = FULL_SERVER_PATH + "/allie-check-winner";

    public final static String OK_CLEAN_PRESS = FULL_SERVER_PATH + "/ok-clean-press";

    public final static String CHECK_LOGOUT = FULL_SERVER_PATH + "/allie-logout";

    public final static String LOGIN_PAGE = FULL_SERVER_PATH + "/loginShortResponse";
    public final static String USERS_LIST = FULL_SERVER_PATH + "/userslist";
    public final static String LOGOUT = FULL_SERVER_PATH + "/chat/logout";
    public final static String SEND_CHAT_LINE = FULL_SERVER_PATH + "/pages/chatroom/sendChat";
    public final static String CHAT_LINES_LIST = FULL_SERVER_PATH + "/chat";

    // GSON instance
    public final static Gson GSON_INSTANCE = new Gson();
}
