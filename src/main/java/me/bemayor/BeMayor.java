package me.bemayor;

import me.bemayor.api.ApiManagement;
import me.bemayor.components.ComponentManagement;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

/*
主旨：该插件旨在多人游戏中增强原版的多元游戏性。
组件：原版修改、付费修理、便携工具、钻石银行
基础：指令、自定义物品、异常信息、配置文件
依赖：
 */
public final class BeMayor extends JavaPlugin {

    private static BeMayor instance;//本插件主类的单例化（作用是保证主类的静态方法的稳定）

    /**
     * 目前框架包含4样东西：
     * -自定义物质管理器CustomItemManager
     * -基础界面CustomGui、WorkbenchGui（专用于工作台类界面）
     * -自定义指令PlayerCommand、SubCommand、GiveCommand\OnOffCommand\SetIntCommand\SetDoubleCommand（专用于给与玩家自定义物品\设置组件的功能开关或值）
     * -调用也可以发送预设在配置文件messages.yml中的文本ChatMessages
     * 以上皆通过框架管理器apiManager调用
     * <p>
     * 目前框架提供的工具有：
     * -ChatUtils
     * -CommonPatterns
     * -ItemMetaBuilder
     * -ItemStackUtils
     * -LoreBuilder
     * -PlayerUtils
     * -ConfigUtils（用于保证配置文件的存在）
     * 以上在api.common目录中
     */
    private ApiManagement apiManager;//框架及其管理
    private ComponentManagement componentManager;//组件及其管理

    private static void setInstance(BeMayor pluginInstance) {
        instance = pluginInstance;
    }

    private static void validateInstance() {
        if (instance == null) {
            throw new IllegalStateException("Cannot invoke static method, BeMayor instance is null.");
        }
    }

    public static ApiManagement getApiManager() {
        validateInstance();
        return instance.apiManager;
    }

    public static ComponentManagement getComponentManager() {
        validateInstance();
        return instance.componentManager;
    }

    public static Server getMyServer() {
        validateInstance();
        return instance.getServer();
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().log(Level.INFO, "BeMayor插件正在加载...");

        setInstance(this);//本插件主类的单例化
        apiManager = new ApiManagement();//创建框架
        componentManager = new ComponentManagement(instance);//创建各个组件
        getLogger().log(Level.INFO, "所有组件已创建");

        componentManager.setup();//启动各个组件
        getLogger().log(Level.INFO, "所有组件已启动");
        apiManager.getMainCommand().register();
        getLogger().log(Level.INFO, "指令集已注册");

        System.out.println("BeMayor插件已加载。");
        getLogger().log(Level.INFO, "BeMayor插件已加载。");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().log(Level.INFO, "BeMayor插件正在关闭...");

        getLogger().log(Level.INFO, "BeMayor插件已关闭。");
    }


}
