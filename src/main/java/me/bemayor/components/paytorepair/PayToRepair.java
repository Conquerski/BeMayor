package me.bemayor.components.paytorepair;

import io.github.bakedlibs.dough.config.Config;
import me.bemayor.api.common.ConfigUtils;
import me.bemayor.api.customitem.CustomItemStack;
import me.bemayor.components.ComponentManagement;
import me.bemayor.components.ComponentMember;
import me.bemayor.components.paytorepair.commands.PayToRepairCommands;
import me.bemayor.components.paytorepair.items.PayAnvil;
import me.bemayor.components.paytorepair.items.PayAnvilGui;
import me.bemayor.components.paytorepair.items.PortablePayAnvil;
import me.bemayor.components.theoriginalmodify.commands.TheOriginalModifyCommands;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class PayToRepair extends ComponentMember {

    public static final List<String> itemIdList = new ArrayList<>();//本组件的自定义物品ID列表

    //修理算法定义
    public PayToRepairUsage usage;

    //物品创建:修复台、便携修复台
    public final CustomItemStack PAY_ANVIL = new PayAnvil(this);
    ;
    public final CustomItemStack PORTABLE_PAY_ANVIL = new PortablePayAnvil(this);
    ;


    //指令定义
    public final PayToRepairCommands commands;
    //配置文件定义
    public static final String CONFIG_FILE_NAME = "PayToRepair.yml";//注意需要在项目资源中添加这个文件
    public final Config config;

    public PayToRepair(ComponentManagement componentManagement) {
        super(componentManagement);

        //配置文件创建
        ConfigUtils.setFile(CONFIG_FILE_NAME, "付费修理组件");
        config = new Config(JavaPlugin.getProvidingPlugin(TheOriginalModifyCommands.class), CONFIG_FILE_NAME);

        //修理算法创建
        usage = new PayToRepairUsage(config);

        //指令创建
        commands = new PayToRepairCommands(this, manager, itemIdList);
    }

    //组件注册
    public void registry() {

        //物品注册
        itemIdList.add(PAY_ANVIL.getId());
        ComponentManagement.registryItem(PAY_ANVIL);

        itemIdList.add(PORTABLE_PAY_ANVIL.getId());
        ComponentManagement.registryItem(PORTABLE_PAY_ANVIL);


        //指令注册
        ComponentManagement.registrySubCommand(commands.getSubCommand());
        ComponentManagement.registryPlayerCommand(commands.getPlayerCommand());
    }

    public void openPayAnvil(Player player) {
        player.playSound(player.getEyeLocation(), Sound.BLOCK_ANVIL_PLACE, 1F, 1F);
        PayAnvilGui paGui = new PayAnvilGui(usage);
        paGui.open(player);
    }

}
