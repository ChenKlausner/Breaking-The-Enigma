package subComponent.dashboard;

import agent.AgentTask;
import com.google.gson.reflect.TypeToken;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.util.List;
import java.util.TimerTask;
import java.util.function.Consumer;

import static util.Constants.GSON_INSTANCE;

public class AgentTaskListRefresher extends TimerTask {
    private Consumer<List<AgentTask>> agentTaskListConsumer;

    private boolean isEmptyBlockingQueue;

    public AgentTaskListRefresher(Consumer<List<AgentTask>> agentTaskListConsumer, boolean isEmptyBlockingQueue) {
        this.agentTaskListConsumer = agentTaskListConsumer;
        this.isEmptyBlockingQueue = isEmptyBlockingQueue;
    }

    @Override
    public void run() {
        if (isEmptyBlockingQueue){
            String finalUrl = HttpUrl
                    .parse(Constants.GET_AGENT_TASK)
                    .newBuilder()
                    .build()
                    .toString();

            HttpClientUtil.runAsync(finalUrl, new Callback() {

                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    System.out.println("get agent task went wrong!");
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String agentTaskListGson = response.body().string();
                    List<AgentTask> agentTaskList = GSON_INSTANCE.fromJson(agentTaskListGson, new TypeToken<List<AgentTask>>() {}.getType());
                    agentTaskListConsumer.accept(agentTaskList);
                }
            });
        }
    }
}
