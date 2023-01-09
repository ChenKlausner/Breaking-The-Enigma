package utils;

//import engine.chat.ChatManager;

import agent.AgentManager;
import battlefield.BattlefieldManager;
import decryptionManager.DecryptionManager;
import decryptionManager.DifficultyLevel;
import engine.Engine;
import users.UserManager;
import jakarta.servlet.ServletContext;

//import static chat.constants.Constants.INT_PARAMETER_ERROR;

public class ServletUtils {

    private static final String USER_MANAGER_ATTRIBUTE_NAME = "userManager";
    private static final String BATTLEFIELD_ATTRIBUTE_NAME = "battlefieldManager";
    private static final String CHAT_MANAGER_ATTRIBUTE_NAME = "chatManager";

    /*
    Note how the synchronization is done only on the question and\or creation of the relevant managers and once they exists -
    the actual fetch of them is remained un-synchronized for performance POV
     */
    private static final Object userManagerLock = new Object();
    private static final Object engineLock = new Object();
    private static final Object DmLock = new Object();
    private static final Object AgentLock = new Object();
    private static final Object battleFieldLock = new Object();
    private static final Object chatManagerLock = new Object();

    public static UserManager getUserManager(ServletContext servletContext) {
        synchronized (userManagerLock) {
            if (servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(USER_MANAGER_ATTRIBUTE_NAME, new UserManager());
            }
        }
        return (UserManager) servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME);
    }

    public static Engine getEngine(ServletContext servletContext, String userName) {
        synchronized (engineLock) {
            if (servletContext.getAttribute(userName) == null) {
                servletContext.setAttribute(userName, new Engine());
            }
        }
        return (Engine) servletContext.getAttribute(userName);
    }

    public static BattlefieldManager getBattlefieldManager(ServletContext servletContext) {
        synchronized (battleFieldLock) {
            if (servletContext.getAttribute(BATTLEFIELD_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(BATTLEFIELD_ATTRIBUTE_NAME, new BattlefieldManager());
            }
        }
        return (BattlefieldManager) servletContext.getAttribute(BATTLEFIELD_ATTRIBUTE_NAME);
    }

    public static DecryptionManager getDM(ServletContext servletContext, String allieUserName, Engine deepCopyOfEngine, String secretMsg, Integer taskSize, DifficultyLevel difficultyLevel) {
        synchronized (DmLock) {
            if (servletContext.getAttribute(allieUserName) == null) {
                servletContext.setAttribute(allieUserName, new DecryptionManager(deepCopyOfEngine, secretMsg, taskSize, difficultyLevel));
            }
        }
        return (DecryptionManager) servletContext.getAttribute(allieUserName);
    }

    public static DecryptionManager getDM(ServletContext servletContext, String allieUserName) {
        return (DecryptionManager) servletContext.getAttribute(allieUserName);
    }

    /*public static AgentManager getAgent(ServletContext servletContext, Integer numOfThreads, String agentName) {
        synchronized (AgentLock) {
            if (servletContext.getAttribute(agentName) == null) {
                servletContext.setAttribute(agentName, new AgentManager(numOfThreads, agentName));
            }
        }
        return (AgentManager) servletContext.getAttribute(agentName);
    }*/

	/*public static ChatManager getChatManager(ServletContext servletContext) {
		synchronized (chatManagerLock) {
			if (servletContext.getAttribute(CHAT_MANAGER_ATTRIBUTE_NAME) == null) {
				servletContext.setAttribute(CHAT_MANAGER_ATTRIBUTE_NAME, new ChatManager());
			}
		}
		return (ChatManager) servletContext.getAttribute(CHAT_MANAGER_ATTRIBUTE_NAME);
	}*/

	/*public static int getIntParameter(HttpServletRequest request, String name) {
		String value = request.getParameter(name);
		if (value != null) {
			try {
				return Integer.parseInt(value);
			} catch (NumberFormatException numberFormatException) {
			}
		}
		return INT_PARAMETER_ERROR;
	}*/
}
