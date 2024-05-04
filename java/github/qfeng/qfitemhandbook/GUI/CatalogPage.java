package github.qfeng.qfitemhandbook.GUI;


import github.qfeng.qfitemhandbook.Message;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import github.qfeng.qfitemhandbook.GUI.*;

import java.util.List;


public class CatalogPage extends Inv implements Listener {
    @EventHandler
    public void playerClickEvent(InventoryClickEvent e){
        if(e.getInventory().getHolder() instanceof CatalogGui) {
            e.setCancelled(true);
            PlayerGui playerGui = PlayerGuiManager.getGuiItem(e.getWhoClicked().getName());
            // 翻页
            if (e.getRawSlot() == 53) {
                ItemStack item = e.getCurrentItem();
                if (item.getItemMeta().getDisplayName().equals("§a§l下一页"))
                {
                    nextGui(e,playerGui);
                }
            }
            if (e.getRawSlot() == 45) {
                ItemStack item = e.getCurrentItem();
                if (item.getItemMeta().getDisplayName().equals("§a§l上一页"))
                {
                    preGui(e,playerGui);
                }
            }
            if (e.getRawSlot()>=9&&e.getRawSlot()<=52&&e.getCurrentItem().getItemMeta() != null){
                String title =e.getCurrentItem().getItemMeta().getDisplayName();
                Catalog catalog = CatalogManager.getCatalog();
                String itemPackName = catalog.getItemPackName(title);
                GuiItem guiItem = GuiItemManager.getGuiItem(itemPackName);
                if(!catalog.getIdList().contains(itemPackName)){
                    e.getWhoClicked().sendMessage(Message.getMsg("Plugin.Name")+Message.getMsg("Admin.NoFoundGui"));
                    return;
                }
                if (e.getWhoClicked().getExpToLevel()<guiItem.getLevel()){
                    e.getWhoClicked().sendMessage(Message.getMsg("Plugin.Name")+Message.getMsg("Admin.NoLevel"));
                    return;
                }
                if (guiItem.getPermission()!=null){
                    List<String> ps = guiItem.getPermission();
                    for (String p : ps) {
                        if (!e.getWhoClicked().hasPermission(p)){
                            e.getWhoClicked().sendMessage(Message.getMsg("Plugin.Name")+Message.getMsg("Admin.NoPermission"));
                            return;
                        }
                    }
                }
                e.getWhoClicked().closeInventory();
                e.getWhoClicked().openInventory(playerGui.getGui(itemPackName));
            }
        }
    }
    private void nextGui(InventoryClickEvent e,PlayerGui playerGui) {
        playerGui.nextPage();
        e.getInventory().clear();
        Catalog catalog = CatalogManager.getCatalog();
        catalog.loadItem(e.getInventory(),playerGui.getpage());
    }
    private void preGui(InventoryClickEvent e,PlayerGui playerGui) {
        playerGui.prePage();
        e.getInventory().clear();
        Catalog catalog = CatalogManager.getCatalog();
        catalog.loadItem(e.getInventory(),playerGui.getpage());
    }
    @EventHandler
    public void playerCloseEvent(InventoryCloseEvent e){
        if (e.getInventory().getHolder() instanceof CatalogGui){
            PlayerGui playerGui = PlayerGuiManager.getGuiItem(e.getPlayer().getName());
            playerGui.setPage(1);
        }
    }
}
