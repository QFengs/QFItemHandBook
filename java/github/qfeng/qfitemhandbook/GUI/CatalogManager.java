package github.qfeng.qfitemhandbook.GUI;

import github.qfeng.qfitemhandbook.QFItemHandBook;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class CatalogManager {
    private static HashMap<String, Catalog> map = new HashMap();

    public CatalogManager() {
    }

    public static void load() {
        map.clear();
    }

    public static Catalog getCatalog() {
        if (map.containsKey("1")) {
            return (Catalog) map.get("1");
        } else {
            Catalog catalog = new Catalog();
            catalog.load();
            map.put("1", catalog);
            return catalog;
        }
    }


    public static HashMap<String, Catalog> getMap() {
        return map;
    }
}
