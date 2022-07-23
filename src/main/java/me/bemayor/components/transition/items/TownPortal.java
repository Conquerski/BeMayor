package me.bemayor.components.transition.items;

import me.bemayor.api.common.LoreBuilder;
import me.bemayor.api.customitem.CustomItemStack;
import me.bemayor.components.transition.TransitionUsage;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class TownPortal extends CustomItemStack {
    public TownPortal(TransitionUsage usage) {
        super("TOWN_PORTAL",
                Material.GLOBE_BANNER_PATTERN,
                "&6跃迁卷轴",
                "&a&o朝对应的核心跃迁", "", "", LoreBuilder.RIGHT_CLICK_TO_OPEN);
        this.setPlaceable(false);
        this.setConsumable(true);

        this.setUseAction(event->{
            Player p = event.getPlayer();
            p.playSound(p.getEyeLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1F, 1F);

            Location l=TransitionUsage.getLocationFromString(event.getItem().getItemMeta().getLore().get(1));
            Location destination=TransitionUsage.findDestination(l);
            if(destination!=null){
                if(TransitionUsage.beaconCheck(p,destination)){
                    TransitionUsage.transitionDestination(p,destination);
                }else{
                    p.sendMessage("§c跃迁目的地的传送阵被破坏！");
                    usage.removeDestination(l);
                }
            }else{
                p.sendMessage("§c跃迁目的地的传送阵已被拆除，该卷轴失效！");
            }
        });
    }



}
