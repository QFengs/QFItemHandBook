package github.qfeng.qfitemhandbook.util;


import github.qfeng.qfitemhandbook.Message;
import github.qfeng.qfitemhandbook.QFItemHandBook;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class Config {
    public static final String DATA_FILE = "DataFile";
    static final File configFile;
    private static YamlConfiguration config;
    private static List<String> i = new ArrayList<>();
    private static File dataFile;
    private static final JavaPlugin plugin = QFItemHandBook.getPlugin();

    public Config() {

    }

    public static void createConfig() {
        Bukkit.getConsoleSender().sendMessage("[" + QFItemHandBook.getPlugin().getName() + "] §cCreate Config.yml");
        try {
            config.save(configFile);
        } catch (IOException var1) {
            var1.printStackTrace();
        }
        i = config.getStringList("ItemPacks");
    }

    public static void loadConfig() {
        if (!configFile.exists()) {
            createConfig();
        }else {
            Bukkit.getConsoleSender().sendMessage(Message.getMsg("Plugin.Name") + " §aFind config.yml");
            config = new YamlConfiguration();
            try {
                config.load(configFile);
            } catch (InvalidConfigurationException | IOException var1) {
                var1.printStackTrace();
                Bukkit.getConsoleSender().sendMessage("[" + QFItemHandBook.getPlugin().getName() + "] §c读取config时发生错误");
            }
            i = config.getStringList("ItemPacks");
        }
    }
    public static List<String> getI(){
        return i;
    }
    public static YamlConfiguration getConfig() {
        return config;
    }
    public static void registerConfig(){
        plugin.saveDefaultConfig();
    }
    public static File getDataFile() {
        return dataFile;
    }

    static {
        configFile = new File("plugins" + File.separator + QFItemHandBook.getPlugin().getName() + File.separator + "config.yml");
        dataFile = QFItemHandBook.getPlugin().getDataFolder();
    }
}
