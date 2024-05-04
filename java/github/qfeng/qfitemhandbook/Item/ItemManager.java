package github.qfeng.qfitemhandbook.Item;


import github.qfeng.qfitemhandbook.util.Config;
import github.saukiya.sxattribute.SXAttribute;
import io.lumine.xikage.mythicmobs.MythicMobs;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemManager {
    public static ItemStack getItem (String key){
        List<String> var1 = Config.getI();
        if (key==null){
            ItemStack netherStar = new ItemStack(Material.NETHER_STAR);
            ItemMeta m = netherStar.getItemMeta();
            m.setDisplayName("§r§l代码错误");
            List<String> lore = new ArrayList<>();
            lore.add("§7§l当你看到该物品，请联系青枫进行修复");
            m.setLore(lore);
            netherStar.setItemMeta(m);
            return netherStar;
        }
        ItemStack stack = null;
        if (var1.contains("NI")) {
            stack = pers.neige.neigeitems.manager.ItemManager.INSTANCE.getItemStack(key);
        }
        if (stack==null && var1.contains("MM")){
           stack = MythicMobs.inst().getItemManager().getItemStack(key);
        }
        if (stack==null && var1.contains("SX")){
            stack = SXAttribute.getApi().getItem(key,null);
        }
        if (stack==null){
            ItemStack netherStar = new ItemStack(Material.NETHER_STAR);
            ItemMeta m = netherStar.getItemMeta();
            m.setDisplayName("§r§l物品库不存在该物品");
            List<String> lore = new ArrayList<>();
            lore.add("§7§l当你看到该物品，请联系管理员进行修改配置");
            m.setLore(lore);
            netherStar.setItemMeta(m);
            return netherStar;
        }
        return stack;
    }
    public static ItemStack getNextItem (){
        ItemStack netherStar = new ItemStack(Material.ARROW);
        ItemMeta m = netherStar.getItemMeta();
        m.setDisplayName("§a§l下一页");
        netherStar.setItemMeta(m);
        return netherStar;
    }
    public static ItemStack getCatalogItem (){
        ItemStack book = new ItemStack(Material.BOOK);
        ItemMeta m = book.getItemMeta();
        m.setDisplayName("§d§l目录");
        book.setItemMeta(m);
        return book;
    }

    public static ItemStack getPreItem (){
        ItemStack netherStar = new ItemStack(Material.ARROW);
        ItemMeta m = netherStar.getItemMeta();
        m.setDisplayName("§a§l上一页");
        netherStar.setItemMeta(m);
        return netherStar;
    }
    public static ItemStack getCatalog(String title,Material material){
        ItemStack book = new ItemStack(material);
        ItemMeta m = book.getItemMeta();
        title = title.replace("&","§");
        m.setDisplayName(title);
        book.setItemMeta(m);
        return book;
    }
}
