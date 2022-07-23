package me.bemayor.components.portabletool.commands;

import me.bemayor.api.commands.SubCommand;
import me.bemayor.api.commands.subcommands.GiveCommand;
import me.bemayor.components.ComponentManagement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PortableToolCommands {
    private SubCommand command;

    public PortableToolCommands(ComponentManagement manager, List<String> itemIdListForTab) {

        command = new SubCommand("portableTool", "便携工具组件的指令集", false,
                null,
                new ArrayList<>(Arrays.asList(
                        new GiveCommand("给与玩家便携工具组件的物品", manager, itemIdListForTab))
                )
        );
    }

    public SubCommand getCommand() {
        return command;
    }
}
