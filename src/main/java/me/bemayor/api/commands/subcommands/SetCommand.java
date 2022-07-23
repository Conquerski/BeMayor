package me.bemayor.api.commands.subcommands;

import io.github.bakedlibs.dough.config.Config;
import me.bemayor.api.commands.CommandInputEvent;
import me.bemayor.api.commands.SubCommand;
import me.bemayor.api.common.ChatUtils;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class SetCommand extends SubCommand {
    public SetCommand(String vaultName, String description, Config config, String path, Supplier<List<String>> listForTab, Function<String,Boolean> newMatches, Consumer<CommandInputEvent> newAction, Consumer<ConfigInputEvent> newSetConfig){
        super(vaultName,"设置"+description+"的值,格式为：{vault}",false);

        this.setAction(e->{
            List<String> texts=e.getTexts();
            if(texts!=null && texts.size()>0) {
                String s=texts.get(0);
                try {
                    if(newMatches==null || !newMatches.apply(s)) {if(e.getCommandSender() instanceof Player){((Player)e.getCommandSender()).sendMessage("§c输入的值格式不对！");}return;}
                    if (newAction != null) { newAction.accept(e); }
                    if (config != null) { newSetConfig.accept(new ConfigInputEvent(config, path, s)); config.save();}
                } catch (Exception x) {
                    String errorText=description + "的值设置指令的动作或者" + config.getFile().getName() + "中的" + path + "有错误！具体信息：" + x;
                    if(e.getCommandSender() instanceof Player)((Player)e.getCommandSender()).sendMessage(errorText);
                    System.out.println(errorText);
                }
            }
        });

        this.setTabFind(e->{
            List<String> texts=e.getTexts();
            if(texts!=null && texts.size()==1) {
                return listForTab.get();
            }
            return ChatUtils.nullListForTab;
        });
    }
    public SetCommand(String vaultName, String description, Supplier<List<String>> listForTab, Function<String,Boolean> newMatches, Consumer<CommandInputEvent> newAction){
        this(vaultName,description,null,"",listForTab,newMatches,newAction,null);
    }
    public SetCommand(String vaultName, String description, Supplier<List<String>> listForTab, Consumer<CommandInputEvent> newAction){
        this(vaultName,description,null,"",listForTab,null,newAction,null);
    }
}
