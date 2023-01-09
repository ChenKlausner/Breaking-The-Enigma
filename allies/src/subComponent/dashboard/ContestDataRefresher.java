package subComponent.dashboard;

import com.google.gson.reflect.TypeToken;
import dto.BattlefieldDto;
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

public class ContestDataRefresher extends TimerTask {
    private Consumer<List<BattlefieldDto>> battlefieldDtoConsumer;

    public ContestDataRefresher(Consumer<List<BattlefieldDto>> battlefieldDtoConsumer) {
        this.battlefieldDtoConsumer = battlefieldDtoConsumer;
    }

    @Override
    public void run() {
        HttpClientUtil.runAsync(Constants.CONTEST_DATA, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("Refresher went wrong");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String jsonOfBattlefieldDto = response.body().string();
                List<BattlefieldDto> battlefieldDtoList = GSON_INSTANCE.fromJson(jsonOfBattlefieldDto, new TypeToken<List<BattlefieldDto>>() {}.getType());
                battlefieldDtoConsumer.accept(battlefieldDtoList);
            }
        });
    }
}
