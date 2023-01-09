package subComponent.dashboard;

import agent.AgentManager;
import dto.AgentProgressDto;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.util.TimerTask;
import java.util.function.Consumer;

import static util.Constants.GSON_INSTANCE;

public class AgentProgressRefresher extends TimerTask {
    private Consumer<AgentProgressDto> agentProgressDtoConsumer;
    private AgentManager agentManager;
    private String userName;

    public AgentProgressRefresher(Consumer<AgentProgressDto> agentProgressDtoConsumer, AgentManager agentManager,String userName) {
        this.agentProgressDtoConsumer = agentProgressDtoConsumer;
        this.agentManager = agentManager;
        this.userName = userName;
    }

    @Override
    public void run() {
        AgentProgressDto agentProgressDto = new AgentProgressDto(userName, agentManager.getTotalMissions(), agentManager.getTotalCompletedMissionCount(), agentManager.getCurrentAmountInQueue(), agentManager.getTotalCandidatesCount());
        agentProgressDtoConsumer.accept(agentProgressDto);

        Callback callback = new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("Agent progress refresher went wrong");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

            }
        };

        String agentProgressDtoJson = GSON_INSTANCE.toJson(agentProgressDto);

        String finalUrl = HttpUrl
                .parse(Constants.AGENT_PROGRESS)
                .newBuilder()
                .addQueryParameter("agentProgressDto", agentProgressDtoJson)
                .build()
                .toString();

        Request request = new Request.Builder()
                .url(finalUrl)
                .build();

        Call call = HttpClientUtil.HTTP_CLIENT.newCall(request);
        call.enqueue(callback);

    }
}
