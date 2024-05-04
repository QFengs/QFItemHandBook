package github.qfeng.qfitemhandbook.GUI;

import github.qfeng.qfitemhandbook.Message;
import github.qfeng.qfitemhandbook.QFItemHandBook;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class Page extends Inv implements Listener {
    @EventHandler
    public void playerClickEvent(InventoryClickEvent e){
        if(e.getInventory().getHolder() instanceof Inv) {
            e.setCancelled(true);
            String itemPackName = ((Inv) e.getInventory().getHolder()).getItemPackName();
            PlayerGui playerGui = PlayerGuiManager.getGuiItem(e.getWhoClicked().getName());
            GuiItem guiItem = GuiItemManager.getGuiItem(itemPackName);
            // 翻页
            if (e.getRawSlot() == 53) {
                ItemStack item = e.getCurrentItem();
                if (item.getItemMeta().getDisplayName().equals("§a§l下一页"))
                {
                    nextGui(e,guiItem,playerGui);
                }
            }
            if (e.getRawSlot() == 45) {
                ItemStack item = e.getCurrentItem();
                if (item.getItemMeta().getDisplayName().equals("§a§l上一页"))
                {
                    preGui(e,guiItem,playerGui);
                }
            }
            //返回目录
            if (e.getRawSlot()==0){
                ItemStack itemStack = e.getCurrentItem();
                if (itemStack.getItemMeta().getDisplayName().equals("§d§l目录")){
                    e.getWhoClicked().closeInventory();
                    e.getWhoClicked().openInventory(CatalogManager.getCatalog().loadItem(new CatalogGui().getCatalogGui(),1));
                }
            }
            //检测指令
            if (e.getRawSlot()>=9&&e.getRawSlot()<=52&&e.getCurrentItem().getItemMeta() != null){
                String itemName =e.getCurrentItem().getItemMeta().getDisplayName();
                Player player = Bukkit.getPlayer(e.getWhoClicked().getName());
                if (!(player.hasPermission(QFItemHandBook.getPlugin().getName()+".ClickCommand")||player.hasPermission("qfihb.ClickCommand"))){
                    player.sendMessage(Message.getMsg("Plugin.Name")+Message.getMsg("Permission.NoClickCommand"));
                    return;
                }
                List<String> commands = guiItem.getCommand(itemName);
                for (String s : commands) {
                    String command = s.replace("%p%", e.getWhoClicked().getName());
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
                }
            }
        }
    }
    private void nextGui(InventoryClickEvent e,GuiItem guiItem,PlayerGui playerGui) {
        playerGui.nextPage();
        e.getInventory().clear();
        guiItem.loadItem(e.getInventory(),playerGui.getpage());
    }
    private void preGui(InventoryClickEvent e,GuiItem guiItem,PlayerGui playerGui) {
        playerGui.prePage();
        e.getInventory().clear();
        guiItem.loadItem(e.getInventory(),playerGui.getpage());
    }
    @EventHandler
    public void playerCloseEvent(InventoryCloseEvent e){
        if (e.getInventory().getHolder() instanceof Inv){
            PlayerGui playerGui = PlayerGuiManager.getGuiItem(e.getPlayer().getName());
            playerGui.setPage(1);
        }
    }
}
