package me.bemayor.components.transition;

import me.bemayor.api.customitem.CustomItemStack;
import me.bemayor.components.ComponentManagement;
import me.bemayor.components.ComponentMember;
import me.bemayor.components.transition.commands.TransitionCommands;
import me.bemayor.components.transition.items.TownPortal;
import me.bemayor.components.transition.items.TransportCore;

import java.util.ArrayList;
import java.util.List;

public class Transition  extends ComponentMember {
    public static final List<String> itemIdList=new ArrayList<>();//本组件的自定义物品ID列表

    //跃迁算法定义
    public TransitionUsage usage=new TransitionUsage();


    //物品定义：回城卷轴
    public final CustomItemStack TOWN_PORTAL = new TownPortal(usage);
    public final CustomItemStack TRANSPORT_CORE = new TransportCore(usage);


    //指令定义
    public final TransitionCommands commands;
    public Transition(ComponentManagement componentManagement) {
        super(componentManagement);
        commands = new TransitionCommands(manager,itemIdList);
    }


    //组件注册
    public void registry(){
        //物品注册
        itemIdList.add(TOWN_PORTAL.getId());
        ComponentManagement.registryItem(TOWN_PORTAL);

        itemIdList.add(TRANSPORT_CORE.getId());
        ComponentManagement.registryItem(TRANSPORT_CORE);



        //指令注册
        ComponentManagement.registrySubCommand(commands.getCommand());
    }

}
