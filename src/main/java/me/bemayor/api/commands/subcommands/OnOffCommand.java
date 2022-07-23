package me.bemayor.api.commands.subcommands;

import io.github.bakedlibs.dough.config.Config;
import me.bemayor.api.commands.CommandInputEvent;
import me.bemayor.api.commands.SubCommand;
import me.bemayor.api.common.ChatUtils;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.function.Consumer;

public class OnOffCommand extends SubCommand {
    public OnOffCommand(String description, Config config,String path, Consumer<CommandInputEvent> trueAction, Consumer<CommandInputEvent> falseAction){
        super("onOff",description+"的开关,格式为：{true/false}",false);

        this.setAction(e->{
            List<String> texts=e.getTexts();
            if(texts!=null && texts.size()>0) {
                String s=texts.get(0);
                try{
                    if(s.equalsIgnoreCase("true")){
                        if(trueAction!=null){ trueAction.accept(e); }
                        if(config!=null){ config.setValue(path,true); }
                    }else if(s.equalsIgnoreCase("false")){
                        if(falseAction!=null){ falseAction.accept(e); }
                        if(config!=null){ config.setValue(path,false); }
                    }
                } catch (Exception x) {
                    String errorText=description+"开关设置指令的动作或者"+config.getFile().getName()+"中的"+path+"有错误！具体信息："+x;
                    if(e.getCommandSender() instanceof Player)((Player)e.getCommandSender()).sendMessage(errorText);
                    System.out.println(errorText);
                }
            }
        });

        this.setTabFind(e->{
            List<String> texts=e.getTexts();
            if(texts!=null && texts.size()==1) {
                return ChatUtils.onOffListForTab;
            }
            return ChatUtils.nullListForTab;
        });
    }
    public OnOffCommand(String description, Consumer<CommandInputEvent> trueAction, Consumer<CommandInputEvent> falseAction){
        this(description,null,"",trueAction,falseAction);
    }
}
