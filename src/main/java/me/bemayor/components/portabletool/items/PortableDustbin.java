package me.bemayor.components.portabletool.items;

import me.bemayor.api.common.LoreBuilder;
import me.bemayor.api.customitem.CustomItemStack;
import me.bemayor.components.common.HeadTexture;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ShapedRecipe;

public class PortableDustbin extends CustomItemStack {
    public PortableDustbin() {
        super("PORTABLE_DUSTBIN",
                HeadTexture.TRASH_CAN.getTexture(),
                "&6便携垃圾桶",
                "&f放入物品后关闭界面，物品就会消失", "", LoreBuilder.RIGHT_CLICK_TO_OPEN);
        this.setPlaceable(false);

        this.setUseAction(event -> {
            Player p = event.getPlayer();
            p.openInventory(Bukkit.createInventory(null, 9 * 3, ChatColor.DARK_RED + "便携垃圾桶"));
            p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
        });

        this.setRecipe(new ShapedRecipe(this.getCloneItemStack())
                .shape("xxx", "y y", "yyy")
                .setIngredient('x', Material.IRON_INGOT)
                .setIngredient('y', Material.COPPER_INGOT)
        );
    }

}
