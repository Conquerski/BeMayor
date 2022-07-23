package me.bemayor.components.theoriginalmodify;

import io.github.bakedlibs.dough.config.Config;
import me.bemayor.api.common.ConfigUtils;
import me.bemayor.components.ComponentManagement;
import me.bemayor.components.ComponentMember;
import me.bemayor.components.theoriginalmodify.commands.TheOriginalModifyCommands;
import me.bemayor.components.theoriginalmodify.listener.BlockBreakEventListener;
import me.bemayor.components.theoriginalmodify.listener.MultipleDurabilityListener;
import me.bemayor.components.theoriginalmodify.listener.NoMendingListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class TheOriginalModify extends ComponentMember {


    //监听器定义和创建
    public static final NoMendingListener noMendingListener = new NoMendingListener();
    public static final MultipleDurabilityListener multipleDurabilityListener = new MultipleDurabilityListener();
    public static final BlockBreakEventListener blockBreakEventListener = new BlockBreakEventListener();


    //指令定义
    public final TheOriginalModifyCommands commands;

    //配置文件定义
    private static final String CONFIG_FILE_NAME = "TheOriginalModify.yml";//注意需要在项目资源中添加这个文件
    private final Config config;

    private static boolean state_isSetup = false;

    public TheOriginalModify(ComponentManagement componentManagement) {
        super(componentManagement);

        //配置文件创建
        ConfigUtils.setFile(CONFIG_FILE_NAME, "原版修改组件");
        config = new Config(JavaPlugin.getProvidingPlugin(TheOriginalModifyCommands.class), CONFIG_FILE_NAME);

        //指令创建
        commands = new TheOriginalModifyCommands(config, noMendingListener, multipleDurabilityListener, blockBreakEventListener);
    }


    public void registry() {
        if (state_isSetup) {
            System.out.println("原版调整已经启动了！请检查存在的重复启动问题。");
        } else {
            state_isSetup = true;
        }

        //载入配置文件数据
        noMendingListener.setupConfigData(config);
        multipleDurabilityListener.setupConfigData(config);
        blockBreakEventListener.setupConfigData(config);

        //监听器注册
        JavaPlugin plugin = manager.getPlugin();
        Bukkit.getPluginManager().registerEvents(noMendingListener, plugin);
        Bukkit.getPluginManager().registerEvents(multipleDurabilityListener, plugin);
        Bukkit.getPluginManager().registerEvents(blockBreakEventListener, plugin);

        //指令注册
        ComponentManagement.registrySubCommand(commands.getCommand());
    }

}
