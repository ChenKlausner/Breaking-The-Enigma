package subComponent.contest;

import dto.AllieDataDto;
import dto.AllieResponse;
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
    private Consumer<AllieResponse> allieResponseConsumer;

    private String uBoatName;

    public TakingResponseRefresher(Consumer<AllieResponse> allieResponseConsumer,String uBoatName) {
        this.allieResponseConsumer = allieResponseConsumer;
        this.uBoatName = uBoatName;
    }

    @Override
    public void run() {
        Callback callback = new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("allie- taking candidates went wrong");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String jsonAllieResponse = response.body().string();
                AllieResponse allieResponse = GSON_INSTANCE.fromJson(jsonAllieResponse, AllieResponse.class);
                allieResponseConsumer.accept(allieResponse);
            }
        };

        String finalUrl = HttpUrl
                .parse(Constants.GET_CANDIDATES)
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
