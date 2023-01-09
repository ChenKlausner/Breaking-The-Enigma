package util;

import com.google.gson.Gson;
import org.omg.CORBA.PUBLIC_MEMBER;

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
    public final static String LOAD_XML = FULL_SERVER_PATH + "/load-XML";
    public final static String USER_LOGIN = FULL_SERVER_PATH + "/user-login";
    public final static String SPECIFICATION = FULL_SERVER_PATH + "/machine-specification";
    public final static String DICTIONARY = FULL_SERVER_PATH + "/machine-dictionary";
    public final static String MACHINE_DETAILS = FULL_SERVER_PATH + "/machine-details";
    public final static String REFLECTOR_ID = FULL_SERVER_PATH + "/reflector-id";
    public final static String MACHINE_MANUAL_CONFIGURATION = FULL_SERVER_PATH + "/machine-manual-configuration";
    public final static String MACHINE_AUTO_CONFIGURATION = FULL_SERVER_PATH + "/machine-auto-configuration";
    public final static String ORIGINAL_CODE_CONFIG = FULL_SERVER_PATH + "/original-code-configuration";
    public final static String CURRENT_CODE_CONFIG = FULL_SERVER_PATH + "/current-code-configuration";
    public final static String WORD_DICTIONARY_CHECK = FULL_SERVER_PATH + "/dictionaryContainWordsFromInput";
    public final static String REMOVE_EXCLUDED_CHARACTERS = FULL_SERVER_PATH + "/removeExcludedChars";
    public final static String VALID_INPUT = FULL_SERVER_PATH + "/valid-input";
    public final static String DECRYPT_INPUT = FULL_SERVER_PATH + "/decrypt-input";
    public final static String STARTUP_SETTINGS = FULL_SERVER_PATH + "/startup-settings";
    public final static String ALLIES_TEAMS = FULL_SERVER_PATH + "/allies-teams";
    public final static String UBOAT_READY = FULL_SERVER_PATH + "/uboat_ready";
    public final static String GET_UBOAT_CANDIDATES = FULL_SERVER_PATH + "/get-uBoat-candidates";
    public final static String OK_UBOAT_PRESS = FULL_SERVER_PATH + "/ok-uboat-press";

    public final static String LOGIN_PAGE = FULL_SERVER_PATH + "/loginShortResponse";
    public final static String USERS_LIST = FULL_SERVER_PATH + "/userslist";
    public final static String LOGOUT = FULL_SERVER_PATH + "/logout";
    public final static String SEND_CHAT_LINE = FULL_SERVER_PATH + "/pages/chatroom/sendChat";
    public final static String CHAT_LINES_LIST = FULL_SERVER_PATH + "/chat";

    // GSON instance
    public final static Gson GSON_INSTANCE = new Gson();
}
