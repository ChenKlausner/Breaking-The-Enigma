package subComponent.contest;

import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.util.TimerTask;
import java.util.function.Consumer;

import static util.Constants.GSON_INSTANCE;

public class LogoutRefresher extends TimerTask {
    private Consumer<Boolean> logoutConsumer;

    private String uBoatName;

    public LogoutRefresher(Consumer<Boolean> logoutConsumer, String uBoatName) {
        this.logoutConsumer = logoutConsumer;
        this.uBoatName = uBoatName;
    }

    @Override
    public void run() {
        Callback callback = new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String jsonLogoutResponse = response.body().string();
                Boolean logoutResponse = GSON_INSTANCE.fromJson(jsonLogoutResponse, Boolean.class);
                logoutConsumer.accept(logoutResponse);
            }
        };

        String finalUrl = HttpUrl
                .parse(Constants.CHECK_LOGOUT)
                .newBuilder()
                .addQueryParameter("UBoatName",uBoatName)
                .build()
                .toString();

        Request request = new Request.Builder()
                .url(finalUrl)
                .build();

        Call call = HttpClientUtil.HTTP_CLIENT.newCall(request);
        call.enqueue(callback);

    }
}
