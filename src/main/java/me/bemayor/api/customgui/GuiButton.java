package me.bemayor.api.customgui;

import java.util.List;


import me.bemayor.api.common.ItemMetaBuilder;
import me.bemayor.api.common.ItemStackUtils;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.kyori.adventure.text.Component;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.components.GuiAction;
import dev.triumphteam.gui.guis.GuiItem;

public class GuiButton {
    GuiItem buttonItem;
    ItemMetaBuilder itemMetaSnapshot;

    public GuiButton(String nameText,ItemStack displayItemStack){
        buttonItem = ItemBuilder.from(displayItemStack).name(Component.text(nameText)).asGuiItem();
        itemMetaSnapshot=new ItemMetaBuilder(buttonItem.getItemStack().getItemMeta());
    }
    public GuiButton(String nameText,Material displayMaterial){
        this(nameText,new ItemStack(displayMaterial));
    }
    public GuiButton(String nameText,Material displayMaterial,GuiAction<InventoryClickEvent> clickAction){
        this(nameText,displayMaterial);
        buttonItem.setAction(clickAction);
    }
    public GuiButton(String nameText,String texture){
        this(nameText, ItemStackUtils.getSkull(nameText, texture));
    }
    public GuiButton(String nameText,String texture,GuiAction<InventoryClickEvent> clickAction){
        this(nameText,texture);
        buttonItem.setAction(clickAction);
    }


    public String getName() { return itemMetaSnapshot.getDisplayName(); }
    public boolean setName(String nameText) { return itemMetaSnapshot.setDisplayName(nameText); }
    public List<String> getLore() { return itemMetaSnapshot.getLore(); }
    public boolean setLore(List<String> newLore) { return itemMetaSnapshot.setLore(newLore); }
    public ItemMeta getItemMeta() {
        return itemMetaSnapshot.getMeta();
    }
    public boolean setItemMeta(ItemMeta meta) { return itemMetaSnapshot.setMeta(meta); }

    public ItemStack getItemStack() {
        return buttonItem.getItemStack();
    }
    public void setItemStack(ItemStack itemStack) { buttonItem.setItemStack(itemStack); }

    public void setAction(GuiAction<InventoryClickEvent> action) { buttonItem.setAction(action); }

    public GuiItem getGuiItem() { return buttonItem; }

    public void refreshDisplay(){
        buttonItem.getItemStack().setItemMeta(itemMetaSnapshot.getMeta());
    }
}
