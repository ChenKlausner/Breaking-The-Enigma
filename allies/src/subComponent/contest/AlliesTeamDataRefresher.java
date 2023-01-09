package subComponent.contest;

import com.google.gson.reflect.TypeToken;
import dto.AllieDataDto;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.util.List;
import java.util.TimerTask;
import java.util.function.Consumer;

import static util.Constants.GSON_INSTANCE;

public class AlliesTeamDataRefresher extends TimerTask {
    private Consumer<List<AllieDataDto>> allieDataDtoListConsumer;

    private String uBoatName;

    public AlliesTeamDataRefresher(Consumer<List<AllieDataDto>> allieDataDtoListConsumer,String uBoatName) {
        this.allieDataDtoListConsumer = allieDataDtoListConsumer;
        this.uBoatName = uBoatName;
    }

    @Override
    public void run() {
        Callback callback = new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("Team allie Refresher went wrong");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String jsonOfAliieDataDto = response.body().string();
                List<AllieDataDto> allieList = GSON_INSTANCE.fromJson(jsonOfAliieDataDto, new TypeToken<List<AllieDataDto>>() {
                }.getType());
                allieDataDtoListConsumer.accept(allieList);
            }
        };
        String finalUrl = HttpUrl
                .parse(Constants.ALLIES_TEAMS)
                .newBuilder()
                .addQueryParameter("uboatName",uBoatName)
                .build()
                .toString();

        Request request = new Request.Builder()
                .url(finalUrl)
                .build();

        Call call = HttpClientUtil.HTTP_CLIENT.newCall(request);
        call.enqueue(callback);
    }
}
