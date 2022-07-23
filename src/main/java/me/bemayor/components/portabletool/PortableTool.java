package me.bemayor.components.portabletool;

import me.bemayor.api.customitem.CustomItemStack;
import me.bemayor.components.ComponentManagement;
import me.bemayor.components.ComponentMember;
import me.bemayor.components.portabletool.commands.PortableToolCommands;
import me.bemayor.components.portabletool.items.PortableCrafter;
import me.bemayor.components.portabletool.items.PortableDustbin;
import me.bemayor.components.portabletool.items.PortableEnderChest;

import java.util.ArrayList;
import java.util.List;

public class PortableTool extends ComponentMember {

    public static final List<String> itemIdList=new ArrayList<>();//本组件的自定义物品ID列表


    //物品定义：便携工作台、便携垃圾桶
    public static final CustomItemStack PORTABLE_CRAFTER = new PortableCrafter();
    public static final CustomItemStack PORTABLE_DUSTBIN = new PortableDustbin();
    public static final CustomItemStack ENDER_BACKPACK = new PortableEnderChest();

    //指令定义
    public final PortableToolCommands commands;
    public PortableTool(ComponentManagement componentManagement) {
        super(componentManagement);
        commands = new PortableToolCommands(manager,itemIdList);
    }


    //组件注册
    public void registry(){
        //物品注册
        itemIdList.add(PORTABLE_CRAFTER.getId());
        ComponentManagement.registryItem(PORTABLE_CRAFTER);
        manager.registryRecipe(PORTABLE_CRAFTER.getRecipe());

        itemIdList.add(PORTABLE_DUSTBIN.getId());
        ComponentManagement.registryItem(PORTABLE_DUSTBIN);
        manager.registryRecipe(PORTABLE_DUSTBIN.getRecipe());

        itemIdList.add(ENDER_BACKPACK.getId());
        ComponentManagement.registryItem(ENDER_BACKPACK);
        manager.registryRecipe(ENDER_BACKPACK.getRecipe());


        //指令注册
        ComponentManagement.registrySubCommand(commands.getCommand());

    }

}
