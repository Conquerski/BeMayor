package me.bemayor.api.commands;

import me.bemayor.api.common.ChatUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.HelpCommand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * This class represents a {@link SubCommand}, it is a {@link Command} that starts with
 * {@code /sf ...} and is followed by the name of this {@link SubCommand}.
 * 
 * @author TheBusyBiscuit
 * 
 * @see MainCommand
 *
 */
public class SubCommand {

    private final String name;
    private final String description;
    private final boolean hidden;

    private Consumer<CommandInputEvent> cmdAction;
    private Function<CommandInputEvent,List<String>> cmdTab;
    private final List<SubCommand> commands;

    protected SubCommand(String name,String description, boolean hidden, Consumer<CommandInputEvent> newAction,Function<CommandInputEvent,List<String>> newTabFind,List<SubCommand> newCommands) {

        this.name = name;
        this.description=description;
        this.hidden = hidden;

        cmdAction=newAction;
        cmdTab=newTabFind;
        commands=newCommands;
    }
    public SubCommand(String name,String description, boolean hidden, Consumer<CommandInputEvent> newAction,Function<CommandInputEvent,List<String>> newTabFind) {
        this(name,description,hidden,newAction,newTabFind,new ArrayList<>());
    }
    public SubCommand(String name,String description, boolean hidden, Consumer<CommandInputEvent> newAction,List<SubCommand> newCommands) {
        this(name,description,hidden,newAction,null,newCommands);
    }
    public SubCommand(String name,String description, boolean hidden, Consumer<CommandInputEvent> newAction) {
        this(name,description,hidden,newAction,null,new ArrayList<>());
    }
    public SubCommand(String name,String description, boolean hidden) {
        this(name,description,hidden,null,null,new ArrayList<>());
    }
    public SubCommand(String name,String description) {
        this(name,description,false,null,null,new ArrayList<>());
    }

        /**
         * This returns the name of this {@link SubCommand}, the name is equivalent to the
         * first argument given to the actual command.
         *
         * @return The name of this {@link SubCommand}
         */

    public final String getName() {
        return name;
    }

    protected String getDescription() {
        return description;
    }
    /**
     * This method returns whether this {@link SubCommand} is hidden from the {@link HelpCommand}.
     * 
     * @return Whether to hide this {@link SubCommand}
     */
    public final boolean isHidden() {
        return hidden;
    }

    public Consumer<CommandInputEvent> getAction(){return cmdAction;}
    public void setAction(Consumer<CommandInputEvent> newAction){cmdAction=newAction;}
    public boolean hasAction() { return cmdAction!=null?true:false; }

    public Function<CommandInputEvent,List<String>>  getTabFind(){return cmdTab;}
    public void setTabFind(Function<CommandInputEvent,List<String>> newTabFind){cmdTab=newTabFind;}
    public boolean hasTabFind() { return cmdTab!=null?true:false; }

    public List<SubCommand> getCommands(){ return commands; }

    public void addSubCommand(SubCommand command){ commands.add(command); }
    public boolean removeSubCommand(int index){
        if(index>=0 && commands.size()>index){
            commands.remove(index);
            return true;
        }else{
            return false;
        }
    }
    public void clearSubCommands(){ commands.clear(); }


    public void onExecute(CommandSender sender, String[] args,int currentIndex){
        int len=args.length-1;
        if(commands.isEmpty()){
            //没有下级指令了
            if(hasAction()){
                if(len > currentIndex){
                    //存在后续指令
                    if(args[currentIndex+1].equalsIgnoreCase("help")){
                        //显示下级所有指令的描述
                        sender.sendMessage(ChatUtils.colorize("&3" + this.getName() + " &b") + this.getDescription());
                    }else{
                        List<String> l=new ArrayList<>();
                        for(int i=currentIndex+1;i<args.length;i++)
                            l.add(args[i]);
                        cmdAction.accept(new CommandInputEvent(sender,l));
                    }
                }else{
                    //不存在后续指令
                    cmdAction.accept(new CommandInputEvent(sender,null));
                }
            }
        }else{
            //存在下级指令
            if(len>currentIndex){
                //存在后续指令
                currentIndex++;
                if(args[currentIndex].equalsIgnoreCase("help")){
                    //显示下级所有指令的描述
                    sendHelp(sender);
                }else{
                    for (SubCommand command : commands) {
                        if (args[currentIndex].equalsIgnoreCase(command.getName())) {
                            command.onExecute(sender, args,currentIndex);
                            break;
                        }
                    }
                }
            }else{
                //不存在后续指令
                if(hasAction()){
                    cmdAction.accept(new CommandInputEvent(sender,null));
                }
            }
        }
    }

    public List<String> onTabFind(CommandSender sender, String[] args,int currentIndex){
        int len=args.length-1;
        if(len>currentIndex) {
            currentIndex++;
            if(!commands.isEmpty()){
                //存在下级指令且存在后续指令
                SubCommand command=null;
                for (SubCommand cmd : commands) {
                    if (args[currentIndex].equalsIgnoreCase(cmd.getName())) {
                        command=cmd;
                        break;
                    }
                }
                if(command==null){
                    List l=new ArrayList();
                    l.add("help");
                    for (SubCommand cmd : commands) {
                        l.add(cmd.getName());
                    }
                    return l;
                }else{
                    return command.onTabFind(sender, args,currentIndex);
                }
            }else{
                //不存在下级指令且存在后续指令
                if(hasTabFind()){
                    List<String> l=new ArrayList<>();
                    for(int i=currentIndex;i<args.length;i++)
                        l.add(args[i]);
                    return cmdTab.apply(new CommandInputEvent(sender, l));
                }else{
                    if(len==currentIndex)
                        return ChatUtils.helpListForTab;
                    else
                        return ChatUtils.nullListForTab;
                }
            }
        }else{
            return ChatUtils.helpListForTab;
        }
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage("");
        sender.sendMessage(ChatUtils.colorize("&a老村长-BeMayor &2v1.0————指令"+name+"的下级指令如下："));
        sender.sendMessage("");

        for (SubCommand cmd : commands) {
            if (!cmd.isHidden()) {
                sender.sendMessage(ChatUtils.colorize("&3" + cmd.getName() + " &b") + cmd.getDescription());
            }
        }
    }

}
