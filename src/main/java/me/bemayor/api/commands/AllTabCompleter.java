package me.bemayor.api.commands;

import me.bemayor.api.common.ChatUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.*;

class AllTabCompleter implements TabCompleter {

    private final MainCommand mainCmd;

    public AllTabCompleter(MainCommand mainCommand) {
        this.mainCmd = mainCommand;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length > 0) {
            List<SubCommand> commands = mainCmd.getSubCommands();
            if (!commands.isEmpty()) {
                if (args.length > 1) {
                    SubCommand command = null;
                    for (SubCommand scmd : commands) {
                        if (args[0].equalsIgnoreCase(scmd.getName())) {
                            command = scmd;
                            break;
                        }
                    }
                    if (command == null) {
                        return ChatUtils.nullListForTab;
                    } else {
                        return command.onTabFind(sender, args, 0);
                    }
                } else {
                    List l = new ArrayList();
                    l.add("help");
                    for (SubCommand command : commands) {
                        l.add(command.getName());
                    }
                    return l;
                }
            } else {
                return ChatUtils.helpListForTab;
            }
        }
        return null;
    }
}
