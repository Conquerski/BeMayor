package me.bemayor.api.common;

import java.util.List;
import java.util.Map;
import java.util.OptionalInt;
import java.util.Set;

import io.github.bakedlibs.dough.items.ItemMetaSnapshot;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemMetaBuilder {
    private ItemMetaSnapshot itemMetaSnapshot;
    private ItemMeta meta;
    private String displayName;
    private boolean isSetDisplayName=false;
    private boolean unbreakable;
    private boolean isSetUnbreakable=false;
    private int damage;
    private boolean isSetDamage=false;
    private List<String> lore;
    private boolean isSetLore=false;

    public ItemMetaBuilder(ItemMeta newMeta){
        itemMetaSnapshot=new ItemMetaSnapshot(newMeta);
        setMeta(newMeta);
    }
    public ItemMeta getMeta(){
        if(isSetDisplayName)
            meta.setDisplayName(displayName);
        if(isSetUnbreakable)
            meta.setUnbreakable(unbreakable);
        if(isSetDamage){
            if(meta instanceof Damageable){
                ((Damageable) meta).setDamage(damage);
            }
        }
        if(isSetLore)
            meta.setLore(lore);
        isSetDisplayName=false;
        isSetUnbreakable=false;
        isSetDamage=false;
        isSetLore=false;
        return meta;
    }
    public boolean setMeta(ItemMeta newMeta){
        if(newMeta==null){return false;}
        meta=newMeta;
        if(meta.hasDisplayName()){displayName=meta.getDisplayName();}
        if(meta.hasLore()){lore=meta.getLore();}
        unbreakable=meta.isUnbreakable();
        if(meta instanceof Damageable){
            damage=((Damageable) meta).getDamage();
        }
        return true;
    }
    public String getDisplayName() { return displayName; }
    public boolean setDisplayName(String newName) {
        if(newName==null){return false;}
        displayName=newName;
        isSetDisplayName=true;
        return true;
    }
    public List<String> getLore() {isSetLore=true; return lore; }
    public boolean setLore(List<String> newLore) {
        if(newLore==null){return false;}
        lore=newLore;
        isSetLore=true;
        return true;
    }
    public boolean getUnbreakable() { return unbreakable; }
    public void setUnbreakable(boolean newUnbreakable) { unbreakable=newUnbreakable;isSetUnbreakable=true; }
    public int getDamage() { return damage; }
    public void setDamage(int newDamage) { damage=newDamage;isSetDamage=true; }

    public Set<ItemFlag> getFlags() { return itemMetaSnapshot.getItemFlags(); }
    public Map<Enchantment,Integer> getEnchants() { return itemMetaSnapshot.getEnchantments(); }
    public OptionalInt getCustomModelData() { return itemMetaSnapshot.getCustomModelData(); }
    public boolean isSimilar(ItemMeta iItemMeta) { return itemMetaSnapshot.isSimilar(iItemMeta); }
    public boolean isSimilar(ItemMetaSnapshot iItemMetaSnapshot) { return itemMetaSnapshot.isSimilar(iItemMetaSnapshot); }
}
