package dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MachineHistory implements Serializable {
    Map<CodeConfiguration, List<InputProcess>> historyAndStats = new HashMap<>();

    public Map<CodeConfiguration, List<InputProcess>> getHistoryAndStats() {
        return historyAndStats;
    }
}
