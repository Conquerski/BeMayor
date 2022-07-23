package me.bemayor.api;

import io.github.bakedlibs.dough.config.Config;

import me.bemayor.api.common.ChatUtils;
import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.function.UnaryOperator;

public class ChatMessages extends Config {

    public ChatMessages(JavaPlugin plugin) {
        super(plugin, ApiManagement.MESSAGES_YML);
    }

    public String getChatPrefix() {
        return getMessage("chat.prefix");
    }

    public String getMessage(String key) {
        return getString(key);
    }

    public String getMessage(Player p, String key) {
        Validate.notNull(p, "Player must not be null!");
        Validate.notNull(key, "Message key must not be null!");
        String str = "回复" + p.getPlayer().getDisplayName() + ":&b" + getMessage(key);
        return str;
    }

    /**
     * Returns the Strings referring to the specified Key
     *
     * @param key The Key of those Messages
     * @return The List this key is referring to
     */

    public List<String> getMessages(String key) {
        Validate.notNull(key, "Message key cannot be null.");
        return getStringList(key);
    }

    public List<String> getMessages(String key, UnaryOperator<String> function) {
        Validate.notNull(key, "Message key cannot be null.");
        Validate.notNull(function, "Function cannot be null.");

        List<String> messages = getMessages(key);
        messages.replaceAll(function);

        return messages;
    }

    public void sendMessage(CommandSender recipient, String key, boolean addPrefix) {
        Validate.notNull(recipient, "Recipient cannot be null!");
        Validate.notNull(key, "Message key cannot be null!");

        String prefix = addPrefix ? getChatPrefix() : "";

        if (recipient instanceof Player) {
            recipient.sendMessage(ChatUtils.colorize(prefix + getMessage((Player) recipient, key)));
        } else {
            recipient.sendMessage(ChatColor.stripColor(ChatUtils.colorize(prefix + getMessage(key))));
        }
    }

    public void sendMessage(CommandSender recipient, String key) {
        sendMessage(recipient, key, true);
    }

    public void sendMessage(CommandSender recipient, String key, UnaryOperator<String> function) {
        sendMessage(recipient, key, true, function);
    }

    public void sendMessage(CommandSender recipient, String key, boolean addPrefix, UnaryOperator<String> function) {

        String prefix = addPrefix ? getChatPrefix() : "";

        if (recipient instanceof Player) {
            recipient.sendMessage(ChatUtils.colorize(prefix + function.apply(getMessage((Player) recipient, key))));
        } else {
            recipient.sendMessage(ChatColor.stripColor(ChatUtils.colorize(prefix + function.apply(getMessage(key)))));
        }
    }

    public void sendMessages(CommandSender recipient, String key) {
        String prefix = getChatPrefix();
        for (String translation : getMessages(key)) {
            String message = ChatUtils.colorize(prefix + translation);
            recipient.sendMessage(message);
        }
    }

    public void sendMessages(CommandSender recipient, String key, boolean addPrefix, UnaryOperator<String> function) {
        String prefix = addPrefix ? getChatPrefix() : "";
        for (String translation : getMessages(key)) {
            String message = ChatUtils.colorize(prefix + function.apply(translation));
            recipient.sendMessage(message);
        }
    }

    public void sendMessages(CommandSender recipient, String key, UnaryOperator<String> function) {
        sendMessages(recipient, key, true, function);
    }
}
