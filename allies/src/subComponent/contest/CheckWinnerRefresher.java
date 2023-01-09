package subComponent.contest;

import dto.AllieResponse;
import dto.WinnerResponse;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.util.TimerTask;
import java.util.function.Consumer;

import static util.Constants.GSON_INSTANCE;

public class CheckWinnerRefresher extends TimerTask {
    private Consumer<WinnerResponse> winnerResponseConsumer;

    private String uBoatName;

    public CheckWinnerRefresher(Consumer<WinnerResponse> winnerResponseConsumer, String uBoatName) {
        this.winnerResponseConsumer = winnerResponseConsumer;
        this.uBoatName = uBoatName;
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
                String jsonWinnerResponse = response.body().string();
                WinnerResponse winnerResponse = GSON_INSTANCE.fromJson(jsonWinnerResponse, WinnerResponse.class);
                winnerResponseConsumer.accept(winnerResponse);
            }
        };

        String finalUrl = HttpUrl
                .parse(Constants.WINNER_CHECK)
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
