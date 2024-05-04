package github.qfeng.qfitemhandbook.GUI;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class PlayerGui {
    private Inventory gui;

    private String playerName;
    private Player player;
    private int page =1;
    public PlayerGui(String playerName) {
        this.playerName = playerName;
        this.player = Bukkit.getPlayer(playerName);
    }
    public Inventory getGui(String itemPackName){
        gui = GuiItemManager.getGuiItem(itemPackName).loadItem(new Inv().getGui(itemPackName),page);
        return gui;
    }
    public int getpage(){
        return page;
    }
    public int setPage(int n){
        page = n;
        return page;
    }
    public void nextPage(){
        page++;
    }
    public void prePage(){
        page--;
    }
}
