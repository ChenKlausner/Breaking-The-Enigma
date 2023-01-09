package subComponent.dashboard;

import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.util.TimerTask;
import java.util.function.Consumer;

import static util.Constants.GSON_INSTANCE;

public class CheckCleanDataRefresher extends TimerTask {

    private Consumer<Boolean> isNeedToCleanConsumer;

    public CheckCleanDataRefresher(Consumer<Boolean> isNeedToCleanConsumer) {
        this.isNeedToCleanConsumer = isNeedToCleanConsumer;
    }

    @Override
    public void run() {
        Callback callback = new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("AGENT- CLEAN DATA WENT WRONG");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String cleanDataGson = response.body().string();
                boolean cleanData = GSON_INSTANCE.fromJson(cleanDataGson, Boolean.class);
                isNeedToCleanConsumer.accept(cleanData);
            }
        };

        String finalUrl = HttpUrl
                .parse(Constants.CLEAN_DATA)
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
