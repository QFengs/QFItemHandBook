//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package github.qfeng.qfitemhandbook;

import github.qfeng.qfitemhandbook.QFItemHandBook;

import java.io.File;
import java.io.IOException;
import java.util.List;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class Message {
    static final File messageFile;
    private static YamlConfiguration messages;

    static {
        messageFile = new File("plugins" + File.separator + QFItemHandBook.getPlugin().getName() + File.separator + "Message.yml");
    }

    public Message() {
    }

    public static void createMessage() {
        Bukkit.getConsoleSender().sendMessage("[" + QFItemHandBook.getPlugin().getName() + "] §cCreate Message.yml");
        messages = new YamlConfiguration();
        messages.set("Plugin.Name", "&8「&a无限图鉴&8」");
        messages.set("Command.open", "&8打开某个图鉴");
        messages.set("Command.reload", "&8重载插件");
        messages.set("Admin.PluginReload", "&8插件已重载");
        messages.set("Admin.NoCommand", "&c无对应指令");
        messages.set("Admin.NoOnline", "&c玩家不在线/ihb open");
        messages.set("Admin.NoFoundGui", "&c找不到对应图鉴");
        messages.set("Admin.NoLevel", "&c您的等级不足以打开这个图鉴");
        messages.set("Admin.NoPermission", "&c您没有权限查看这个图鉴");
        messages.set("Admin.NoPermissionCommand", "&c您没有对应的权限");
        messages.set("Title.Catalog", "&e无限图鉴");
        messages.set("Permission.NoClickCommand", "&c抱歉，您没有图鉴的点击指令权限");
        try {
            messages.save(messageFile);
        } catch (IOException var1) {
            var1.printStackTrace();
        }

    }

    public static void loadMessage() {
        if (!messageFile.exists()) {
            createMessage();
        } else {
            Bukkit.getConsoleSender().sendMessage("[" + QFItemHandBook.getPlugin().getName() + "] §aFind Message.yml");
        }

        messages = new YamlConfiguration();

        try {
            messages.load(messageFile);
        } catch (InvalidConfigurationException | IOException var1) {
            var1.printStackTrace();
            Bukkit.getConsoleSender().sendMessage(Message.getMsg("Plugin.Name")+" §a读取message时发生错误");
        }

    }

    public static String getMsg(String loc, Object... args) {
        String raw = messages.getString(loc);
        if (raw != null && !raw.isEmpty()) {
            if (args == null) {
                return raw.replace("&", "§");
            } else {
                for (int i = 0; i < args.length; ++i) {
                    raw = raw.replace("{" + i + "}", args[i] == null ? "null" : args[i].toString());
                }

                return raw.replace("&", "§");
            }
        } else {
            return "Null Message: " + loc;
        }
    }

    public static List<String> getList(String loc, String... args) {
        List<String> list = messages.getStringList(loc);
        if (list != null && !list.isEmpty()) {
            for (int e = 0; e < list.size(); ++e) {
                String lore = ((String) list.get(e)).replace("&", "§");

                for (int i = 0; i < args.length; ++i) {
                    lore = lore.replace("{" + i + "}", args[i] == null ? "null" : args[i]);
                }

                list.set(e, lore);
            }

            return list;
        } else {
            list.add("Null Message: " + loc);
            return list;
        }
    }

    public static void send(LivingEntity entity, String loc, Object... args) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            String message = getMsg(loc, args);
            if (message.contains("[ACTIONBAR]")) {
                message = message.replace("[ACTIONBAR]", "");
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
            } else if (message.contains("[TITLE]")) {
                message = message.replace("[TITLE]", "");
                if (message.contains(":")) {
                    String title = message.split("\n")[0];
                    String subTitle = message.split("\n")[1];
                    player.sendTitle(title, subTitle, 5, 20, 5);
                } else {
                    player.sendTitle(message, (String) null, 5, 20, 5);
                }
            } else {
                player.sendMessage(message);
            }

        }
    }

    public static YamlConfiguration getMessages() {
        return messages;
    }
}
