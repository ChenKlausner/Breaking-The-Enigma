package subComponent.bruteForceTab;

import com.google.gson.reflect.TypeToken;
import dto.AllieDataDto;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
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

    public AlliesTeamDataRefresher(Consumer<List<AllieDataDto>> allieDataDtoListConsumer) {
        this.allieDataDtoListConsumer = allieDataDtoListConsumer;
    }

    @Override
    public void run() {
        HttpClientUtil.runAsync(Constants.ALLIES_TEAMS, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("Team allie Refresher went wrong");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String jsonOfAliieDataDto = response.body().string();
                List<AllieDataDto> allieList = GSON_INSTANCE.fromJson(jsonOfAliieDataDto, new TypeToken<List<AllieDataDto>>() {}.getType());
                allieDataDtoListConsumer.accept(allieList);
            }
        });
    }
}
