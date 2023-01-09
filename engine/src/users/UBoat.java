package users;

import java.util.HashSet;
import java.util.Set;

public class UBoat {
    private String userName;
    private Set<String> alliesMembers;

    public UBoat(String userName) {
        this.userName = userName;
        alliesMembers = new HashSet<>();
    }

    public void addAllieMember(String allieUserName){
        alliesMembers.add(allieUserName);
    }
}
