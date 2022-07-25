package me.bemayor.components.portabletool.items;

import me.bemayor.api.common.LoreBuilder;
import me.bemayor.api.customitem.CustomItemStack;

import me.bemayor.components.common.HeadTexture;
import me.bemayor.components.portabletool.PortableTool;
import me.bemayor.components.transition.Transition;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ShapedRecipe;

public class PortableCrafter extends CustomItemStack {
    public PortableCrafter() {
        super("PORTABLE_CRAFTER",
                HeadTexture.PORTABLE_CRAFTER.getTexture(),
                "&6便携工作台",
                "&a&o一个便于使用的工作台", "", LoreBuilder.RIGHT_CLICK_TO_OPEN);
        this.setPlaceable(false);

        this.setUseAction(event -> {
            Player p = event.getPlayer();
            p.openWorkbench(p.getLocation(), true);
            p.getWorld().playSound(p.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1, 1);
        });

        this.setRecipe(new ShapedRecipe(PortableTool.namespacedKey,this.getCloneItemStack())
                .shape("xyz", "yay", "zyx")
                .setIngredient('x', Material.DIAMOND)
                .setIngredient('y', Material.LEATHER)
                .setIngredient('z', Material.END_CRYSTAL)
                .setIngredient('a', Material.CRAFTING_TABLE)
        );

    }

}