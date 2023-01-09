package subComponent.bruteForceTab;

import dto.AllieDataDto;
import dto.UBoatResponse;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.util.List;
import java.util.TimerTask;
import java.util.function.Consumer;

import static util.Constants.GSON_INSTANCE;

public class TakingResponseRefresher extends TimerTask {

    private Consumer<UBoatResponse> uBoatResponseConsumer;

    public TakingResponseRefresher(Consumer<UBoatResponse> uBoatResponseConsumer) {
        this.uBoatResponseConsumer = uBoatResponseConsumer;
    }

    @Override
    public void run() {
        Callback callback = new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("uboat- taking candidates went wrong");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String jsonUBoatResponse = response.body().string();
                UBoatResponse uBoatResponse = GSON_INSTANCE.fromJson(jsonUBoatResponse, UBoatResponse.class);
                uBoatResponseConsumer.accept(uBoatResponse);
            }
        };

        String finalUrl = HttpUrl
                .parse(Constants.GET_UBOAT_CANDIDATES)
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
