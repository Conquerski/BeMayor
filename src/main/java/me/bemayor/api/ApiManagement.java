package me.bemayor.api;

import me.bemayor.api.commands.MainCommand;
import me.bemayor.api.common.ConfigUtils;
import me.bemayor.api.customitem.CustomItemManagement;

import me.bemayor.api.integrations.IntegrationsManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;

public class ApiManagement {

    //此处设置自定义物质的NBT标签和外观文件
    public static String ITEM_KEY = "beMayor_item";
    public static String BLOCK_KEY = "beMayor_block";
    public static String MESSAGES_YML = "messages.yml";//注意需要在项目资源中添加这个文件
    public static String APICONFIG_YML = "apiconfig.yml";//注意需要在项目资源中添加这个文件
    public static String ITEM_MODELS_YML = "item-models.yml";//注意需要在项目资源中添加这个文件
    public static String COMMAND_NAME = "bemayor";


    private static final JavaPlugin plugin = JavaPlugin.getProvidingPlugin(ApiManagement.class);
    private final ApiConfig config = new ApiConfig(plugin);
    private final ChatMessages messages = new ChatMessages(plugin);
    private final CustomItemManagement customItemManager;
    private final MainCommand allCommand;
    private final IntegrationsManager integrationsManager;

    private static boolean state_isSetup = false;

    public ApiManagement() {
        ConfigUtils.setFiles(new ArrayList<>(Arrays.asList(
                APICONFIG_YML,
                MESSAGES_YML,
                ITEM_MODELS_YML
        )), "框架");//确保配置文件存在

        System.out.println("框架正在创建...");
        customItemManager = new CustomItemManagement(this);
        allCommand = new MainCommand(this, COMMAND_NAME);
        integrationsManager = new IntegrationsManager(this);
        System.out.println("框架已创建");
        setup();
    }

    public void setup() {
        if (state_isSetup) {
            System.out.println("API管理器已经启动了！请检查存在的重复启动问题。");
            return;
        } else {
            state_isSetup = true;
        }

        System.out.println("框架正在启动...");


        integrationsManager.start();


        System.out.println("框架已启动");
    }

    public static JavaPlugin getPlugin() {
        return plugin;
    }

    public ApiConfig getApiConfig() {
        return config;
    }

    public ChatMessages getChatMessages() {
        return messages;
    }

    public CustomItemManagement getCustomItemManager() {
        return customItemManager;
    }

    public MainCommand getMainCommand() {
        return allCommand;
    }

    public IntegrationsManager getIntegrationsManager() {
        return integrationsManager;
    }

}
