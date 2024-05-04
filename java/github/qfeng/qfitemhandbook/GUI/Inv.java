package github.qfeng.qfitemhandbook.GUI;

import github.qfeng.qfitemhandbook.Item.ItemManager;
import github.qfeng.qfitemhandbook.Message;
import github.qfeng.qfitemhandbook.util.Config;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Inv implements InventoryHolder {
    YamlConfiguration config = new YamlConfiguration();
    Inventory gui;

    private ItemStack item;
    private Player player;

    public String getItemPackName() {
        return itemPackName;
    }

    private String itemPackName;

    public Inv() {
        File configfile = new File(Config.getDataFile(), "config.yml");
        try {
            config.load(configfile);
        } catch (IOException | InvalidConfigurationException var4) {
            var4.printStackTrace();
        }
        ConfigurationSection itemsfirst = config.getConfigurationSection("Items");
        ConfigurationSection items = itemsfirst.getConfigurationSection("DangBan");
        Material itemmaterial = Material.NETHER_STAR;
        ItemStack item = new ItemStack(Material.STAINED_GLASS);
        ItemMeta meta = item.getItemMeta();
        meta.setLore(Collections.singletonList("§7配置错误了"));
        meta.setDisplayName("§d§l图鉴");
        if(items != null){
            itemmaterial = Material.getMaterial(items.getString("material")) ;
            meta.setLore(items.getStringList("lore"));
            meta.setDisplayName(items.getString("name"));
        }
        if(itemmaterial==null){
            itemmaterial = Material.STAINED_GLASS_PANE;
        }
        item = new ItemStack(itemmaterial);
        item.setItemMeta(meta);
        this.item = item;
    }
    public void load(){

    }
    public Inventory getGui(String itemPackName) {
        this.itemPackName = itemPackName;
        String title = GuiItemManager.getGuiItem(itemPackName).getTitle();
        gui = Bukkit.createInventory(this,6*9,title);
        //接下来上下两圈挡板
        for (int i = 0; i < 9; i++) {
            gui.setItem(i,item);
        }
        for (int i = 45; i < 54; i++) {
            gui.setItem(i,item);
        }
        return gui;
    }
    public Inventory getCatalogGui() {
        gui = Bukkit.createInventory(this,6*9, Message.getMsg("Title.Catalog"));
        //接下来上下两圈挡板
        for (int i = 0; i < 9; i++) {
            gui.setItem(i,item);
        }
        for (int i = 45; i < 54; i++) {
            gui.setItem(i,item);
        }
        return gui;
    }
    public void loadGuiItem(Inventory gui){
        for (int i = 0; i < 9; i++) {
            gui.setItem(i,item);
        }
        for (int i = 45; i < 54; i++) {
            gui.setItem(i,item);
        }
        gui.setItem(0,ItemManager.getCatalogItem());
    }
    @Override
    public Inventory getInventory() {
        if (gui == null ){
            return Bukkit.createInventory(this,9,"配置错误");
        }
        return gui;
    }


}
