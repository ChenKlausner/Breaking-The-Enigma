package subComponent.dashboard;

import agent.AgentTask;
import dto.AgentResponse;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.BlockingDeque;
import java.util.function.Consumer;

import static util.Constants.GSON_INSTANCE;

public class AgentResponseRefresher extends TimerTask {

    private Consumer<AgentResponse> agentResponseConsumer;
    private BlockingDeque<AgentResponse> agentResponseBlockingDeque;

    public AgentResponseRefresher(Consumer<AgentResponse> agentResponseConsumer, BlockingDeque<AgentResponse> agentResponseBlockingDeque) {
        this.agentResponseConsumer = agentResponseConsumer;
        this.agentResponseBlockingDeque = agentResponseBlockingDeque;
    }

    @Override
    public void run() {
        if (!agentResponseBlockingDeque.isEmpty()){
            try {
                AgentResponse agentResponse = agentResponseBlockingDeque.take();
                agentResponseConsumer.accept(agentResponse);
                Callback callback = new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        System.out.println("agent update candidates went wrong");
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                    }
                };

                String agentResponseJson = GSON_INSTANCE.toJson(agentResponse);

                String finalUrl = HttpUrl
                        .parse(Constants.AGENT_RESPONSE)
                        .newBuilder()
                        .addQueryParameter("agentResponse", agentResponseJson)
                        .build()
                        .toString();

                Request request = new Request.Builder()
                        .url(finalUrl)
                        .build();

                Call call = HttpClientUtil.HTTP_CLIENT.newCall(request);
                call.enqueue(callback);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
