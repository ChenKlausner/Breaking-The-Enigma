package fullApp;

import dto.AgentResponse;
import javafx.application.Platform;
import java.util.function.Consumer;

public class UIAdapter {
    private Consumer<AgentResponse> introduceNewCandidates;
    private Consumer<Long> updateTotalMissions;

    public UIAdapter(Consumer<AgentResponse> introduceNewCandidates, Consumer<Long> updateTotalMissions) {
        this.introduceNewCandidates = introduceNewCandidates;
        this.updateTotalMissions = updateTotalMissions;

    }

    public void addNewCandidates(AgentResponse agentResponse) {
        Platform.runLater(
                () -> {
                    introduceNewCandidates.accept(agentResponse);
                }
        );
    }

    public void updateTotalMissions(long totalMissions) {
        Platform.runLater(
                () -> updateTotalMissions.accept(totalMissions)
        );
    }
}
