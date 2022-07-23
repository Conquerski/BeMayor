package me.bemayor.components.transition.items;

import me.bemayor.api.customitem.CustomItemStack;
import me.bemayor.components.transition.TransitionUsage;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ShapedRecipe;

public class TransportCore extends CustomItemStack {
    public TransportCore(TransitionUsage usage) {
        super("TRANSPORT_CORE",
                HeadTexture.FANCY_CUBE.getTexture(),
                "&6跃迁核心",
                "&a&o标记所在信标位置为所属卷轴跃迁目的地", "&f用来合成跃迁卷轴", "", "&e放置在激活信标的光柱内&7进行使用");

        this.setUseAction(event -> {
            Player p = event.getPlayer();
            Location l = event.getClickedBlock().getLocation();
            Location destination = TransitionUsage.getDestination(p, l);
            if (destination != null) {
                usage.addDestination(destination);
                openTransportCore(p, destination);
                p.playSound(p.getEyeLocation(), Sound.BLOCK_ENDER_CHEST_OPEN, 1F, 0.6F);
            }
        });

        this.setRecipe(new ShapedRecipe(this.getCloneItemStack())
                .shape("axa", "byb", "zcz")
                .setIngredient('x', Material.BEACON)
                .setIngredient('y', Material.CONDUIT)
                .setIngredient('z', Material.END_CRYSTAL)
                .setIngredient('a', Material.DISPENSER)
                .setIngredient('b', Material.NETHERITE_INGOT)
                .setIngredient('c', Material.END_CRYSTAL)
        );

    }

    public void openTransportCore(Player player, Location destination) {
        TransportCoreGui tcGui = new TransportCoreGui();
        tcGui.open(player, destination);
    }
}
