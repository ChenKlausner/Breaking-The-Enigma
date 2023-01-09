package subComponent.dashboard;

import dto.BattlefieldDto;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.util.TimerTask;
import java.util.function.Consumer;

import static util.Constants.GSON_INSTANCE;

public class ContestDataRefresher extends TimerTask {
    private Consumer<BattlefieldDto> battlefieldDtoConsumer;

    public ContestDataRefresher(Consumer<BattlefieldDto> battlefieldDtoConsumer) {
        this.battlefieldDtoConsumer = battlefieldDtoConsumer;
    }

    @Override
    public void run() {
        HttpClientUtil.runAsync(Constants.AGENT_CONTEST_DATA, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("agent contest data refresher went wrong");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String jsonOfBattlefieldDto = response.body().string();
                BattlefieldDto battlefieldDto = GSON_INSTANCE.fromJson(jsonOfBattlefieldDto, BattlefieldDto.class);
                battlefieldDtoConsumer.accept(battlefieldDto);
            }
        });
    }
}
