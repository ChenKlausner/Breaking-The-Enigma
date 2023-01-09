package subComponent.contest;

import com.google.gson.reflect.TypeToken;
import dto.AgentProgressDto;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.util.List;
import java.util.TimerTask;
import java.util.function.Consumer;

import static util.Constants.GSON_INSTANCE;

public class AgentProgressRefresher extends TimerTask {
    private Consumer<List<AgentProgressDto>> agentProgressDtoListConsumer;

    public AgentProgressRefresher(Consumer<List<AgentProgressDto>> agentProgressDtoListConsumer) {
        this.agentProgressDtoListConsumer = agentProgressDtoListConsumer;
    }

    @Override
    public void run() {
        Callback callback = new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("ALLIE - agent progress went wrong");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String agentProgressDtoListJson = response.body().string();
                List<AgentProgressDto> agentProgressDtoList = GSON_INSTANCE.fromJson(agentProgressDtoListJson, new TypeToken<List<AgentProgressDto>>() {
                }.getType());
                agentProgressDtoListConsumer.accept(agentProgressDtoList);
            }
        };
        String finalUrl = HttpUrl
                .parse(Constants.AGENTS_TEAMS_PROGRESS)
                .newBuilder()
                .build()
                .toString();

        Request request = new Request.Builder()
                .url(finalUrl)
                .build();

        Call call = HttpClientUtil.HTTP_CLIENT.newCall(request);
        call.enqueue(callback);
    }
}
