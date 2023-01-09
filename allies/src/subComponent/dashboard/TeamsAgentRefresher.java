package subComponent.dashboard;

import com.google.gson.reflect.TypeToken;
import dto.AgentDataDto;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.util.List;
import java.util.TimerTask;
import java.util.function.Consumer;

import static util.Constants.GSON_INSTANCE;

public class TeamsAgentRefresher extends TimerTask {

    private Consumer<List<AgentDataDto>> agentDataDtoListConsumer;

    public TeamsAgentRefresher(Consumer<List<AgentDataDto>> agentDataDtoListConsumer) {
        this.agentDataDtoListConsumer = agentDataDtoListConsumer;
    }

    @Override
    public void run() {
        HttpClientUtil.runAsync(Constants.AGENT_TEAM, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("Team agent Refresher went wrong");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String jsonOfAgentDataDto = response.body().string();
                List<AgentDataDto> battlefieldDtoList = GSON_INSTANCE.fromJson(jsonOfAgentDataDto, new TypeToken<List<AgentDataDto>>() {}.getType());
                agentDataDtoListConsumer.accept(battlefieldDtoList);
            }
        });
    }
}
