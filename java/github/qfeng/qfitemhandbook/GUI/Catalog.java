package github.qfeng.qfitemhandbook.GUI;

import github.qfeng.qfitemhandbook.Item.ItemManager;
import github.qfeng.qfitemhandbook.util.Config;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Catalog {
    public List<String> getTitleList() {
        return catalogtitle;
    }

    private List<String> catalogtitle = new ArrayList<>();
    private Map<String,String> titleToId = new HashMap<>();
    private File filen = new File(Config.getDataFile(), "ItemPacks");
    private File[] files = filen.listFiles();
    private List<String> thisItem;

    public void load(){

        for (File file : files) {
            if (file.toString().contains(".yml")) {
                String s = file.toString().replace("plugins\\QFItemHandBook\\ItemPacks\\","").replace(".yml","");
                catalogtitle.add(new GuiItem(s).getTitle());
                titleToId.put(new GuiItem(s).getTitle(),s);
            }
        }
    }
    public String getItemPackName(String title){
        return titleToId.get(title);
    }
    public Inventory loadItem(Inventory gui,int page) {
        new CatalogGui().loadGuiItem(gui);
        int maxpage = 1 + catalogtitle.size() / 37;
        if (catalogtitle.size() > 36 && page < maxpage) {
            gui.setItem(53, ItemManager.getNextItem());  //下一页
        }
        if (page != 1) {
            gui.setItem(45, ItemManager.getPreItem());   //上一页
        }
        this.thisItem = catalogtitle.subList((page - 1) * 36, Math.min(catalogtitle.size(), page * 36));
        int index = 9;
        for (String item : this.thisItem) {
            if (gui.getItem(index) == null) {
                gui.setItem(index, ItemManager.getCatalog(item,GuiItemManager.getGuiItem(titleToId.get(item)).getMaterial()));
                index++;
            } else {
                break;
            }
        }
        return gui;
    }
    public  List<String> getIdList(){
        List<String> id = new ArrayList<>();
        for (String s : catalogtitle) {
            id.add(titleToId.get(s));
        }
        return id;
    }
}
