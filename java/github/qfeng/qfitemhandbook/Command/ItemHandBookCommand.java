package github.qfeng.qfitemhandbook.Command;


import github.qfeng.qfitemhandbook.GUI.*;
import github.qfeng.qfitemhandbook.Message;
import github.qfeng.qfitemhandbook.QFItemHandBook;
import github.qfeng.qfitemhandbook.util.Config;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.IntStream;

public class ItemHandBookCommand implements CommandExecutor, TabExecutor {

    public JavaPlugin plugin = QFItemHandBook.getPlugin();

    public boolean onCommand(CommandSender sender, org.bukkit.command.Command arg1, String label, String[] args) {
        CommandType type = CommandType.CONSOLE;
        if (sender instanceof Player) {
            if (!(sender.hasPermission(plugin.getName() + ".use")||sender.hasPermission("qfihb.use"))) {
                sender.sendMessage(Message.getMsg("Admin.NoPermissionCommand", new Object[0]));
                return true;
            }

            type = CommandType.PLAYER;
        }

        int var8;
        if (args.length == 0) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&l----------"+Message.getMsg("Plugin.Name")+" &e&l指令详细"+"&7&l----------") );
            String color = "&7";
            Method[] var14 = this.getClass().getDeclaredMethods();
            var8 = var14.length;

            for (int var15 = 0; var15 < var8; ++var15) {
                Method method = var14[var15];
                if (method.isAnnotationPresent(PlayerCommand.class)) {
                    PlayerCommand sub = (PlayerCommand) method.getAnnotation(PlayerCommand.class);
                    if (this.contains(sub.type(), type) && (sender.hasPermission(plugin.getName() + "." + sub.cmd())||sender.hasPermission( "qfihb." + sub.cmd()))) {
                        color = color.equals("&7") ? "" : "&7";
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', MessageFormat.format(color + "/{0} {1}{2}&7 -&c {3}", label, sub.cmd(), sub.arg(), Message.getMsg("Command." + sub.cmd(), new Object[0]))));
                    }
                }
            }

            return true;
        } else {
            Method[] var6 = this.getClass().getDeclaredMethods();
            int var7 = var6.length;

            for (var8 = 0; var8 < var7; ++var8) {
                Method method = var6[var8];
                if (method.isAnnotationPresent(PlayerCommand.class)) {
                    PlayerCommand sub = (PlayerCommand) method.getAnnotation(PlayerCommand.class);
                    if (sub.cmd().equalsIgnoreCase(args[0])) {
                        if (this.contains(sub.type(), type) && (sender.hasPermission(plugin.getName() + "." + args[0])||sender.hasPermission( "qfihb." + args[0]))) {
                            try {
                                method.invoke(this, sender, args);
                            } catch (InvocationTargetException | IllegalAccessException var12) {
                                var12.printStackTrace();
                            }

                            return true;
                        }

                        sender.sendMessage(Message.getMsg("Admin.NoPermissionCommand", new Object[0]));
                        return true;
                    }
                }
            }

            sender.sendMessage(Message.getMsg("Admin.NoCommand", new Object[]{args[0]}));
            return true;
        }
    }

    private boolean contains(CommandType[] type1, CommandType type2) {
        return IntStream.range(0, type1.length).anyMatch((i) -> {
            return type1[i].equals(CommandType.ALL) || type1[i].equals(type2);
        });
    }
    private void showHelp(CommandSender sender) {
        // 显示帮助信息
        sender.sendMessage(Message.getMsg("Plugin.Name")+ChatColor.GREEN + "输入'/ihb'查看详细指令");

    }

    @PlayerCommand(
            cmd = "open",
            arg = " (<图鉴名> <player>)"
    )
    public void onOpenCommand(CommandSender sender, String[] args) {
        if (args.length == 1){
            Player player = Bukkit.getPlayer(sender.getName());
            if (player== null){
                sender.sendMessage("命令台不能使用该指令");
                return;
            }
            Catalog catalog = CatalogManager.getCatalog();
            player.openInventory(catalog.loadItem(new CatalogGui().getCatalogGui(),1));
            return;
        }
        if (args.length == 2) {
            Player player = Bukkit.getPlayerExact(sender.getName());
            if (player == null) {
                sender.sendMessage(Message.getMsg("Admin.NoOnline", new Object[0]));
                return;
            }
            String itemPacks = args[1];
            if(!CatalogManager.getCatalog().getIdList().contains(itemPacks)){
                sender.sendMessage(Message.getMsg("Plugin.Name")+Message.getMsg("Admin.NoFoundGui"));
                return;
            }
            PlayerGui playerGui = new PlayerGui(sender.getName());
            player.openInventory(playerGui.getGui(itemPacks));
        }
        else if (args.length == 3){
            if (sender.hasPermission(QFItemHandBook.getPlugin().getName()+".openothers")||sender.hasPermission("qfihb.openothers")){
                Player player = Bukkit.getPlayer(args[2]);
                if (player == null) {
                    sender.sendMessage(Message.getMsg("Admin.NoOnline", new Object[0]));
                    return;
                }
                String itemPacks = args[1];
                PlayerGui playerGui = new PlayerGui(player.getName());
                player.openInventory(playerGui.getGui(itemPacks));
            }  else {
                sender.sendMessage(Message.getMsg("Admin.NoPermissionCommand"));
            }
        }
        else {
            showHelp(sender);
        }
    }

    @PlayerCommand(
            cmd = "reload"
    )
    public void onReloadCommand(CommandSender sender, String[] args) {

        Message.loadMessage();
        Config.loadConfig();
        CatalogManager.load();
        (new Thread(() -> {
            GuiItemManager.getMap().values().forEach(GuiItem::load);
            GuiItemManager.loadGuiItem();
            PlayerGuiManager.loadPlayerGui();
        })).start();
        sender.sendMessage(Message.getMsg("Plugin.Name")+Message.getMsg("Admin.PluginReload", new Object[0]));
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args) {
        if(sender.hasPermission("qfihb.reload") &&args.length == 1){
            List<String> list = new ArrayList<>();
            list.add("open");
            list.add("reload");
            return list;
        }
        if(sender.hasPermission("qfihb.reload") &&Objects.equals(args[0], "open") && args.length == 2){
            Catalog catalog = CatalogManager.getCatalog();
            return catalog.getIdList();
        }
        return null;
    }
}
