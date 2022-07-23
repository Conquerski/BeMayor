package me.bemayor.api.commands;

import org.bukkit.command.CommandSender;

import java.util.List;

public class CommandInputEvent {

    private final CommandSender sender;
    private final List<String> texts;

    public CommandInputEvent(CommandSender sender,List<String> texts){
        this.sender=sender;
        this.texts=texts;
    }

    public CommandSender getCommandSender(){return sender;}
    public List<String> getTexts(){return texts;}
}
