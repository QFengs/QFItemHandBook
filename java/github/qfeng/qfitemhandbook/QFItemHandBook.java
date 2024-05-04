package github.qfeng.qfitemhandbook;

import github.qfeng.qfitemhandbook.Command.ItemHandBookCommand;
import github.qfeng.qfitemhandbook.GUI.CatalogPage;
import github.qfeng.qfitemhandbook.GUI.Page;
import github.qfeng.qfitemhandbook.util.Config;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class QFItemHandBook extends JavaPlugin {

    private static JavaPlugin plugin = null;

    public static QFItemHandBook getInstance() {
        return instance;
    }

    private static QFItemHandBook instance;
    private Logger logger;

    public static JavaPlugin getPlugin() {
        return  plugin;
    }

    @Override
    public void onEnable() {
        Long oldTimes = System.currentTimeMillis();
        logger = getLogger();
        plugin = this;
        instance = this;
        Message.loadMessage();
        Config.registerConfig();
        // 使用Logger记录日志
        logger.info("QFItemHandBook 插件已启用");
        // 注册命令
        getCommand("itemhanbook").setExecutor(new ItemHandBookCommand());
        //注册监听器
        getServer().getPluginManager().registerEvents(new Page(),this);
        getServer().getPluginManager().registerEvents(new CatalogPage(),this);
        if (Bukkit.getPluginManager().isPluginEnabled("NeigeItems")) {
            Bukkit.getConsoleSender().sendMessage("[" + this.getName() + "]Find NeigeItems");
        } else if (Bukkit.getPluginManager().isPluginEnabled("MythicMobs")) {
            Bukkit.getConsoleSender().sendMessage("[" + this.getName() + "]Find MythicMobs!");
        } else {
            Bukkit.getConsoleSender().sendMessage("[" + this.getName() + "]Please load NeigeItems or MythicMobs!");
            this.setEnabled(false);
        }
        Bukkit.getConsoleSender().sendMessage("[" + this.getName() + "] §a加载用时:" + (System.currentTimeMillis() - oldTimes) + "毫秒");
        Bukkit.getConsoleSender().sendMessage("[" + this.getName() + "] §a加载成功! ");
        Config.loadConfig();


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        logger.info("QFItemHandBook 插件卸载");
        logger = null;
    }
}
