package me.bemayor.components;

import me.bemayor.BeMayor;
import me.bemayor.api.commands.PlayerCommand;
import me.bemayor.api.commands.SubCommand;
import me.bemayor.api.customitem.CustomItemStack;
import me.bemayor.components.common.CoreProtectUtils;
import me.bemayor.components.common.EconomyUtils;
import me.bemayor.components.paytorepair.PayToRepair;
import me.bemayor.components.portabletool.PortableTool;
import me.bemayor.components.theoriginalmodify.TheOriginalModify;
import me.bemayor.components.transition.Transition;
import org.bukkit.inventory.ShapedRecipe;

public class ComponentManagement {

    private final BeMayor plugin;


    /**
     * ==========================================================================================
     * 在此处添加或删除各个组件的定义
     */

    private final TheOriginalModify theOriginalModify = new TheOriginalModify(this);
    private final PortableTool portableTool = new PortableTool(this);
    private final PayToRepair payToRepair = new PayToRepair(this);
    private final Transition transition = new Transition(this);
    /**
     * ==========================================================================================
     */


    private static boolean state_isSetup = false;

    public ComponentManagement(BeMayor plugin) {
        this.plugin = plugin;
    }

    public BeMayor getPlugin() {
        return plugin;
    }

    public void setup() {
        if (state_isSetup) {
            System.out.println("组件管理器已经启动了！请检查存在的重复启动问题。");
            return;
        } else {
            state_isSetup = true;
        }


        //对公共组件进行启动
        CoreProtectUtils.setupCoreProtect();
        EconomyUtils.setupEcon();


        //对常规组件进行注册
        registryAll();

    }

    private void registryAll() {

        /**
         * ==========================================================================================
         * 在此处添加或删除各个组件的注册
         */
        theOriginalModify.registry();
        portableTool.registry();
        payToRepair.registry();
        transition.registry();

        /**
         * ==========================================================================================
         */

    }

    public static void registryItem(CustomItemStack customItem) {
        BeMayor.getApiManager().getCustomItemManager().add(customItem);
    }

    public static void registrySubCommand(SubCommand command) {
        BeMayor.getApiManager().getMainCommand().addSubCommand(command);
    }

    public static void registryPlayerCommand(PlayerCommand command) {
        BeMayor.getApiManager().getMainCommand().addPlayerCommand(command);
    }

    public void registryRecipe(ShapedRecipe recipe) {
        BeMayor.getMyServer().addRecipe(recipe);
    }
}
