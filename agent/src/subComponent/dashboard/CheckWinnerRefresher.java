package subComponent.dashboard;

import agent.AgentTask;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.util.List;
import java.util.TimerTask;
import java.util.function.Consumer;

import static util.Constants.GSON_INSTANCE;

public class CheckWinnerRefresher extends TimerTask {
    private Consumer<Boolean> isDoneConsumer;

    public CheckWinnerRefresher(Consumer<Boolean> isDoneConsumer) {
        this.isDoneConsumer = isDoneConsumer;
    }

    @Override
    public void run() {
        Callback callback = new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("allie- check winner went wrong");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String doneGson = response.body().string();
                boolean done = GSON_INSTANCE.fromJson(doneGson, Boolean.class);
                isDoneConsumer.accept(done);
            }
        };

        String finalUrl = HttpUrl
                .parse(Constants.CHECK_IF_DONE)
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
