package me.bemayor.api.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.function.Function;

public class PlayerCommand implements CommandExecutor {

    private final String name;
    private final Function<CommandSender, Boolean> cmdAction;
    public PlayerCommand(String commandName, Function<CommandSender, Boolean> newAction){
        this.name=commandName;
        this.cmdAction=newAction;
    }
    public String getName(){return name;}

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(cmdAction!=null) {
            return cmdAction.apply(sender);
        }else
           return false;
    }
}
