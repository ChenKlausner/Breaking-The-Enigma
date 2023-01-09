package battlefield;

import java.util.HashMap;
import java.util.Map;

public class BattlefieldManager {
    private Map<String, Battlefield> battlefieldMap;

    public BattlefieldManager() {
        this.battlefieldMap = new HashMap<>();
    }

    public void addBattlefield(String userName , Battlefield battlefield){
        battlefieldMap.put(userName,battlefield);
    }

    public Map<String, Battlefield> getBattlefieldMap() {
        return battlefieldMap;
    }

    public void removeBattlefield(String userName){
        battlefieldMap.remove(userName);
    }
}
