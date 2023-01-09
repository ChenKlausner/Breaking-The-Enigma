package users;

import dto.AllieResponse;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Allie {
    private String userName;
    private Set<String> agentMembers;
    private Integer taskSize;
    private boolean isReady;
    private List<AllieResponse> allieResponseList;
    private boolean done;
    private boolean cleanData;

    public Allie(String userName) {
        this.userName = userName;
        this.agentMembers = new HashSet<>();
        this.taskSize = 0;
        this.isReady = false;
        this.allieResponseList = new ArrayList<>();
        this.done = false;
        this.cleanData = false;
    }

    public void addAgentMember(String agentUserName) {
        agentMembers.add(agentUserName);
    }

    public void setTaskSize(Integer taskSize) {
        this.taskSize = taskSize;
    }

    public String getUserName() {
        return userName;
    }

    public Set<String> getAgentMembers() {
        return agentMembers;
    }

    public Integer getTaskSize() {
        return taskSize;
    }

    public boolean isReady() {
        return isReady;
    }

    public void setReady(boolean ready) {
        isReady = ready;
    }

    synchronized public void addAllieResponse(AllieResponse allieResponse) {
        allieResponseList.add(allieResponse);
    }

    synchronized public AllieResponse takeAllieResponse() {
        return allieResponseList.remove(0);
    }

    synchronized public boolean isEmptyAllieResponseList() {
        return allieResponseList.isEmpty();
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public boolean isCleanData() {
        return cleanData;
    }

    public void setCleanData(boolean cleanData) {
        this.cleanData = cleanData;
    }

    public void cleanAllieResponseList(){
        allieResponseList.clear();
    }
}
