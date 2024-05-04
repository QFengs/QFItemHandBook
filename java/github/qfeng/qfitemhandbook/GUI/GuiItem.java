package github.qfeng.qfitemhandbook.GUI;

import github.qfeng.qfitemhandbook.Item.ItemManager;
import github.qfeng.qfitemhandbook.util.Config;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.*;
public class GuiItem {
    public String getItemPackName() {
        return itemPackName;
    }
    public Material getMaterial(){
        return material;
    }
    public List<String> getPermission(){
        return permission;
    }
    public int getLevel(){
        return level;
    }

    private String itemPackName;    //物品包名

    public String getTitle() {
        return title;
    }

    private String title;           //GUI标题名
    private Material material;           //目录中的材质
    private List<String> itemName = new ArrayList<>();
    private List<String> permission = new ArrayList<>();    //权限限制
    private int level = 0;          //等级限制
    private List<String> thisItem = new ArrayList<>();
    private Map<String,List<String>> lore = new HashMap<>();
    private Map<String,String> displayName = new HashMap<>();
    //这俩直接用物品库ID去存↑ ↓
    private Map<String,List<String>> command = new HashMap<>();
    public List<String> getCommand(String item) {
        if (displayName.containsKey(item)) {
            return command.get(displayName.get(item));
        } else  return new ArrayList<>();
    }


    private YamlConfiguration yaml =new YamlConfiguration();


    public GuiItem(String itemPackName) {
        this.itemPackName = itemPackName;
        load();
    }

    private void create() {
        File file = new File(Config.getDataFile(), "ItemPacks" + File.separator + this.itemPackName + ".yml");
        yaml.set(itemPackName+".title","标题");
        yaml.set(itemPackName+".material","BOOK");
        yaml.set(itemPackName+".level","0");
        yaml.set(itemPackName+".permission",new String[]{"ihb.tj1"});
        yaml.set(itemPackName+".Items.bianhao.lore", new String[]{"lore1","lore2"});
        yaml.set(itemPackName+".Items.bianhao.command", new String[]{"money give %p% 1"});
        try {
            yaml.save(file);
        } catch (IOException var4) {
            var4.printStackTrace();
        }
    }

    public void load() {
        File file = new File(Config.getDataFile(), "ItemPacks" + File.separator + this.itemPackName + ".yml");
        if (!file.exists() || file.isDirectory()) {
            this.create();
        }
        try {
            yaml.load(file);
        } catch (InvalidConfigurationException | IOException var7) {
            var7.printStackTrace();
        }
        ConfigurationSection itemPack = yaml.getConfigurationSection(itemPackName);
        this.title = itemPack.getString("title").replace("&","§");
        if (itemPack.getString("material")==null){
            material = Material.BOOK;
        } else {
            this.material = Material.getMaterial(itemPack.getString("material"));
        }
        if (material == null){
            material = Material.BOOK;
        }
        this.level = Math.max(itemPack.getInt("level"), 0);
        this.permission = itemPack.getStringList("permission");
        ConfigurationSection itemfind = itemPack.getConfigurationSection("Items");
        Set<String> items = itemfind.getKeys(false);
        for (String itemName : items) {
            ConfigurationSection itemsection = itemfind.getConfigurationSection(itemName);
            this.itemName.add(itemName);
            this.lore.put(itemName,itemsection.getStringList("lore"));
            this.displayName.put(ItemManager.getItem(itemName).getItemMeta().getDisplayName(),itemName);
            if(itemsection.contains("command")){
                this.command.put(itemName, itemsection.getStringList("command"));
            }
        }
    }
    public ItemStack getGuiItem(String item){
        ItemStack itemStack = ItemManager.getItem(item);
        ItemMeta meta = itemStack.getItemMeta();
        List<String> lore = meta.getLore();
        if (lore==null){
            lore = new ArrayList<>();
        }
        if (this.lore.containsKey(item)) {
            lore.addAll(this.lore.get(item));
        }
        meta.setLore(lore);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public Inventory loadItem(Inventory gui,int page) {
        new Inv().loadGuiItem(gui);
        int maxpage = 1 + itemName.size() / 37;
        if (itemName.size() > 36 && page < maxpage) {
            gui.setItem(53, ItemManager.getNextItem());  //下一页
        }
        if (page != 1) {
            gui.setItem(45, ItemManager.getPreItem());   //上一页
        }
        this.thisItem = itemName.subList((page - 1) * 36, Math.min(this.itemName.size(), page * 36));
        int index = 9;
        for (String item : this.thisItem) {
            if (gui.getItem(index) == null) {
                gui.setItem(index, getGuiItem(item));
                index++;
            } else {
                break;
            }
        }
        return gui;
    }

}
