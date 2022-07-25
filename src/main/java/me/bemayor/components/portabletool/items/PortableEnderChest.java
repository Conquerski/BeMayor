package me.bemayor.components.portabletool.items;

import me.bemayor.api.common.LoreBuilder;
import me.bemayor.api.customitem.CustomItemStack;
import me.bemayor.components.common.HeadTexture;

import me.bemayor.components.portabletool.PortableTool;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ShapedRecipe;

public class PortableEnderChest extends CustomItemStack {
    public PortableEnderChest() {
        super("ENDER_BACKPACK",
                HeadTexture.ENDER_BACKPACK.getTexture(),
                "&6便携末影箱",
                "&a&o一个便于使用的末影箱", "", LoreBuilder.RIGHT_CLICK_TO_OPEN);
        this.setPlaceable(false);

        this.setUseAction(event -> {
            Player p = event.getPlayer();
            p.openInventory(p.getEnderChest());
            p.playSound(p.getEyeLocation(), Sound.BLOCK_ENDER_CHEST_OPEN, 1F, 1F);
        });

        this.setRecipe(new ShapedRecipe(PortableTool.namespacedKey,this.getCloneItemStack())
                .shape(" x ", "yay", "zzz")
                .setIngredient('x', Material.ECHO_SHARD)
                .setIngredient('y', Material.DIAMOND)
                .setIngredient('z', Material.SCULK_SHRIEKER)
                .setIngredient('a', Material.ENDER_CHEST)
        );
    }

}
