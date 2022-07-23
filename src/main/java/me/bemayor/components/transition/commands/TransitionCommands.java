package me.bemayor.components.transition.commands;

import me.bemayor.api.commands.SubCommand;
import me.bemayor.api.commands.subcommands.GiveCommand;
import me.bemayor.components.ComponentManagement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TransitionCommands {
    private SubCommand command;

    public TransitionCommands(ComponentManagement manager, List<String> itemIdListForTab){

        command=new SubCommand("transition","跃迁组件的指令集",false,
                null,
                new ArrayList<>(Arrays.asList(
                        new GiveCommand("给与玩家跃迁组件的物品",manager,itemIdListForTab))
                )
        );
    }

    public SubCommand getCommand(){return command;}
}
