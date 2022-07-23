package me.bemayor.components.theoriginalmodify.listener;

import io.github.bakedlibs.dough.config.Config;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemMendEvent;

/**
 * 取消经验修补事件的监听器
 *
 * @author ConquerSki
 */

public class NoMendingListener implements Listener {

    public boolean on_off=true;

    @EventHandler
    public void noMending(PlayerItemMendEvent e){
        if(on_off) {
            e.setCancelled(true);
            //String a = e.getPlayer().getName();
        }
    }

    public void setupConfigData(Config config){
        try {
            on_off=config.getBoolean("noMending.onOff");
        } catch (Exception x) {
            System.out.println("配置文件" + config.getFile().getName() + "中的NoMending有错误！具体信息：" + x);
        }
    }
}

