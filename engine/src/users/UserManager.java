package users;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
Adding and retrieving users is synchronized and in that manner - these actions are thread safe
Note that asking if a user exists (isUserExists) does not participate in the synchronization and it is the responsibility
of the user of this class to handle the synchronization of isUserExists with other methods here on it's own
 */
public class UserManager {
    private final HashMap<String, String> usersMap;
    private final HashMap<String, UBoat> uBoatUserMap;
    private final HashMap<String, Allie> alliesUserMap;
    private final HashMap<String, Agent> agentUserMap;

    public HashMap<String, UBoat> getuBoatUserMap() {
        return uBoatUserMap;
    }

    public HashMap<String, Allie> getAlliesUserMap() {
        return alliesUserMap;
    }

    public HashMap<String, Agent> getAgentUserMap() {
        return agentUserMap;
    }

    public UserManager() {
        usersMap = new HashMap<>();
        uBoatUserMap = new HashMap<>();
        alliesUserMap = new HashMap<>();
        agentUserMap = new HashMap<>();
    }

    public synchronized void addUBoatUser(String username, String userType) {
        usersMap.put(username, userType);
        uBoatUserMap.put(username, new UBoat(username));
    }

    public synchronized void addAllieUser(String username, String userType) {
        usersMap.put(username, userType);
        alliesUserMap.put(username,new Allie(username));
    }

    public synchronized void addUser(String username, String alliesTeamName, Integer numOfThreads, Integer numOfTasks) {
        agentUserMap.put(username, new Agent(username, alliesTeamName, numOfThreads, numOfTasks));
        alliesUserMap.get(alliesTeamName).addAgentMember(username);
        usersMap.put(username, "Agent");
    }

    public synchronized void removeUser(String username) {
        usersMap.remove(username);
        uBoatUserMap.remove(username);
    }

    public synchronized Set<String> getUsers() {
        Set<String> usersWithType = new HashSet<String>();
        for (String name : usersMap.keySet()) {
            String newUsersWithType = name + " " + usersMap.get(name);
            usersWithType.add(newUsersWithType);
        }
        return usersWithType;
    }

    public boolean isUserExists(String username) {
        return usersMap.containsKey(username);
    }

    public HashMap<String, String> getUsersMap() {
        return usersMap;
    }

    public Set<String> getAlliesUserNamesSet(){
        return alliesUserMap.keySet();
    }
}
