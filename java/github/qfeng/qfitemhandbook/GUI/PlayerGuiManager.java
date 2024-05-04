package github.qfeng.qfitemhandbook.GUI;

import github.qfeng.qfitemhandbook.QFItemHandBook;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class PlayerGuiManager {
    private static HashMap<String, PlayerGui> map = new HashMap();

    public PlayerGuiManager() {
    }

    public static void loadPlayerGui() {
        map.clear();
    }

    public static PlayerGui getGuiItem(String player) {
        if (map.containsKey(player)) {
            return map.get(player);
        } else {
            PlayerGui playerGui = new PlayerGui(player);
            map.put(player, playerGui);
            return playerGui;
        }
    }



    public static HashMap<String, PlayerGui> getMap() {
        return map;
    }
}
