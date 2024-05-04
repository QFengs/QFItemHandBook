package github.qfeng.qfitemhandbook.GUI;

import github.qfeng.qfitemhandbook.QFItemHandBook;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class GuiItemManager {
    private static HashMap<String, GuiItem> map = new HashMap();

    public GuiItemManager() {
    }

    public static void loadGuiItem() {
        map.clear();
    }

    public static GuiItem getGuiItem(String itemPackName) {
        if (map.containsKey(itemPackName)) {
            return (GuiItem) map.get(itemPackName);
        } else {
            GuiItem GuiItem = new GuiItem(itemPackName);
            map.put(itemPackName, GuiItem);
            return GuiItem;
        }
    }


    public static void autoSave() {
        (new BukkitRunnable() {
            public void run() {
                GuiItemManager.map.values().forEach(GuiItem::load);
            }
        }).runTaskTimerAsynchronously(QFItemHandBook.getPlugin(), 2400L, 2400L);
    }

    public static HashMap<String, GuiItem> getMap() {
        return map;
    }
}
