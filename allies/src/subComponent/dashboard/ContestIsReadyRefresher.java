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

public class ContestIsReadyRefresher extends TimerTask {

    private Consumer<Boolean> isReadyConsumer;
    private String uboatName;

    public ContestIsReadyRefresher(Consumer<Boolean> isReadyConsumer,String uboatName) {
        this.isReadyConsumer = isReadyConsumer;
        this.uboatName = uboatName;
    }

    @Override
    public void run() {
        Callback callback = new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("CONTEST CHECK READY went wrong");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String isReadyJson = response.body().string();
                boolean isReady = GSON_INSTANCE.fromJson(isReadyJson, new TypeToken<Boolean>() {}.getType());
                isReadyConsumer.accept(isReady);
            }
        };

        String finalUrl = HttpUrl
                .parse(Constants.CONTEST_READY)
                .newBuilder()
                .addQueryParameter("uboatName",uboatName)
                .build()
                .toString();

        Request request = new Request.Builder()
                .url(finalUrl)
                .build();

        Call call = HttpClientUtil.HTTP_CLIENT.newCall(request);
        call.enqueue(callback);
    }
}
