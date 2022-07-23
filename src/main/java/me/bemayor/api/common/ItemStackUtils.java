package me.bemayor.api.common;

import io.github.bakedlibs.dough.common.CommonPatterns;
import io.github.bakedlibs.dough.items.ItemUtils;
import io.github.bakedlibs.dough.skins.PlayerHead;
import io.github.bakedlibs.dough.skins.PlayerSkin;
import me.bemayor.api.customitem.CustomItemStack;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.nio.charset.StandardCharsets;
import java.util.*;

public class ItemStackUtils {

    public static boolean isExistence(ItemStack itemStack){
        if (itemStack== null || itemStack.getType() == Material.AIR || itemStack.getAmount() == 0) {
            return false;
        }
        return true;
    }
    public static boolean isExistence(Block block){
        if (block==null || block.isEmpty() || block.getType()==Material.AIR) {
            return false;
        }
        return true;
    }

    public static String getItemName(ItemStack itemStack) {
        if (itemStack instanceof CustomItemStack) {
            Optional<String> name = ((CustomItemStack) itemStack).getItemMetaSnapshot().getDisplayName();
            if (name.isPresent()) {
                return name.get();
            }
        }
        return ItemUtils.getItemName(itemStack);
    }

    public static void putItemToPlayer(Player player, ItemStack itemStack){
        if(itemStack!=null && player!=null){
            Map<Integer, ItemStack> excess = player.getInventory().addItem(itemStack);
            if (!excess.isEmpty()) {
                for (ItemStack is : excess.values()) {
                    player.getWorld().dropItem(player.getLocation(), is);
                    player.sendMessage("§6背包太满了，你的"+ ItemUtils.getItemName(is)+"掉地上了！");
                }
            }
        }
    }

    public static  ItemStack getSkull(String id,  String texture) {
        PlayerSkin skin = PlayerSkin.fromBase64(getTexture(id, texture));
        return PlayerHead.getItemStack(skin);
    }
    public static String getTexture(String id, String texture) {
        Validate.notNull(id, "The id cannot be null");
        Validate.notNull(texture, "The texture cannot be null");

        if (texture.startsWith("ey")) {
            return texture;
        } else if (CommonPatterns.HEXADECIMAL.matcher(texture).matches()) {
            String value = "{\"textures\":{\"SKIN\":{\"url\":\"http://textures.minecraft.net/texture/" + texture + "\"}}}";
            return Base64.getEncoder().encodeToString(value.getBytes(StandardCharsets.UTF_8));
        } else {
            throw new IllegalArgumentException("The provided texture for Item \"" + id + "\" does not seem to be a valid texture String!");
        }
    }

}
