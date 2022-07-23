package me.bemayor.api.commands;

import me.bemayor.api.ApiManagement;
import me.bemayor.api.ApiMember;
import me.bemayor.api.ChatMessages;

import me.bemayor.api.common.ChatUtils;
import org.apache.commons.lang.Validate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.*;

public class MainCommand extends ApiMember implements CommandExecutor, Listener {

    private boolean registered = false;

    private final String name;
    private final List<SubCommand> subCommands = new ArrayList<>();
    private final List<PlayerCommand> playerCommands = new ArrayList<>();

    public MainCommand(ApiManagement apiManagement, String name) {
        super(apiManagement);
        this.name = name;
    }

    public List<SubCommand> getSubCommands() {
        return subCommands;
    }

    public void addSubCommand(SubCommand command) {
        subCommands.add(command);
    }

    public boolean removeSubCommand(int index) {
        if (index >= 0 && subCommands.size() > index) {
            subCommands.remove(index);
            return true;
        } else {
            return false;
        }
    }

    public void clearSubCommands() {
        subCommands.clear();
    }

    public List<PlayerCommand> getPlayerCommands() {
        return playerCommands;
    }

    public void addPlayerCommand(PlayerCommand command) {
        playerCommands.add(command);
    }

    public boolean removePlayerCommand(int index) {
        if (index >= 0 && playerCommands.size() > index) {
            playerCommands.remove(index);
            return true;
        } else {
            return false;
        }
    }

    public void clearPlayerCommands() {
        playerCommands.clear();
    }

    public void register() {
        Validate.isTrue(!registered, "当前插件的指令集已经过注册了，不要重复注册!");
        registered = true;

        apiManager.getPlugin().getServer().getPluginManager().registerEvents(this, apiManager.getPlugin());
        apiManager.getPlugin().getCommand(name).setExecutor(this);
        apiManager.getPlugin().getCommand(name).setTabCompleter(new AllTabCompleter(this));

        if (playerCommands != null && !playerCommands.isEmpty()) {
            for (PlayerCommand pCmd : playerCommands) {
                apiManager.getPlugin().getCommand(pCmd.getName()).setExecutor(pCmd);
            }
        }
    }


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender.hasPermission("bemayor.command.bemayor")) {
            if (!subCommands.isEmpty() && args.length > 0) {
                if (args[0].equalsIgnoreCase("help")) {
                    sendHelp(sender);
                } else {
                    for (SubCommand command : subCommands) {
                        if (args[0].equalsIgnoreCase(command.getName())) {
                            command.onExecute(sender, args, 0);
                            sender.sendMessage(ChatUtils.colorize("&a老村长-BeMayor &2v1.0" + "  &b指令已执行完毕。"));
                            return true;
                        }
                    }
                }
            } else {
                apiManager.getChatMessages().sendMessage(sender, "chat.no-command", true);
            }

            /*
             * We could just return true here, but if there's no subcommands,
             * then something went horribly wrong anyway.
             * This will also stop sonarcloud from nagging about
             * this always returning true...
             */
            return !subCommands.isEmpty();
        } else
            apiManager.getChatMessages().sendMessage(sender, "chat.no-permission", true);
        return false;
    }

    public void sendHelp(CommandSender sender) {
        sender.sendMessage("");
        sender.sendMessage(ChatUtils.colorize("&a老村长-BeMayor &2v1.0"));
        sender.sendMessage("");

        for (SubCommand cmd : subCommands) {
            if (!cmd.isHidden()) {
                sender.sendMessage(ChatUtils.colorize("&3/bm " + cmd.getName() + " &b") + cmd.getDescription());
            }
        }
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        if (e.getMessage().equalsIgnoreCase("/help bemayor")) {
            sendHelp(e.getPlayer());
            e.setCancelled(true);
        }
    }


    public ChatMessages getChatMessages() {
        return apiManager.getChatMessages();
    }
}
