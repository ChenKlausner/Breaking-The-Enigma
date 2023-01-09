package subComponent.dashboard;

import com.google.gson.reflect.TypeToken;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.util.TimerTask;
import java.util.function.Consumer;

import static util.Constants.GSON_INSTANCE;

public class CheckIfNeedToWorkRefresher extends TimerTask {

    private Consumer<Boolean> isReadyConsumer;

    public CheckIfNeedToWorkRefresher(Consumer<Boolean> isReadyConsumer) {
        this.isReadyConsumer = isReadyConsumer;
    }

    @Override
    public void run() {
        Callback callback = new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("agent working went wrong");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String isReadyJson = response.body().string();
                boolean isReady = GSON_INSTANCE.fromJson(isReadyJson, new TypeToken<Boolean>() {}.getType());
                isReadyConsumer.accept(isReady);
            }
        };

        String finalUrl = HttpUrl
                .parse(Constants.START_WORKING)
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
