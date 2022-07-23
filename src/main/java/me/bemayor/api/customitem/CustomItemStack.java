package me.bemayor.api.customitem;

/*
自定义物品(堆)cItem

-cItem的属性
--(s/g)ID
--(s/g)NBT贴图处理过的原版物品(堆)样板itemStackTemplate，用以代表该自定义的物品
--(s/g)NBT快照，用于快速获取NBT
--(s/g)锁定，不能拿取和使用
--(s/g)放置，可以被放置在地面上
--(s/g)贴图

-cItem的创建

-cItem的获取
--直接返回可定义数量的itemStackTemplate的副本
--ItemStack getItemStack()
--ItemStack getItemStack(int amount)

-cItem的使用

 */

import io.github.bakedlibs.dough.common.CommonPatterns;
import io.github.bakedlibs.dough.items.ItemMetaSnapshot;
import io.github.bakedlibs.dough.items.ItemUtils;
import io.github.bakedlibs.dough.skins.PlayerHead;
import io.github.bakedlibs.dough.skins.PlayerSkin;
import me.bemayor.api.ApiManagement;
import me.bemayor.api.common.ItemStackUtils;
import me.bemayor.api.exceptions.PrematureCodeException;
import me.bemayor.api.exceptions.WrongItemStackException;
import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Consumer;

public class CustomItemStack extends ItemStack {
    private String id;
    /**
     * This is the original {@link ItemStack} that represents this item.
     * It is immutable and should always be cloned, never used directly.
     */
    private ItemMetaSnapshot itemMetaSnapshot;
    private boolean locked = false;
    private boolean placeable = true;
    private boolean consumable = false;
    private String texture = null;
    private Consumer<PlayerInteractEvent> useAction;
    private ShapedRecipe recipe;

    public CustomItemStack(String id, ItemStack item) {
        super(item);

        Validate.notNull(id, "物品ID不能为空!");
        Validate.isTrue(id.equals(id.toUpperCase(Locale.ROOT)), "物品ID必须是大写的! (e.g. 'MY_ITEM_ID')");

        if (ApiManagement.getPlugin() == null) {
            throw new PrematureCodeException("使用自定义物品前先确保插件的加载！");
        }

        this.id = id;

        ItemMeta meta = item.getItemMeta();

        CustomItemManagement.setItemData(meta, id);
        CustomItemManagement.setTexture(meta, id);

        setItemMeta(meta);
    }

    public CustomItemStack(String id, ItemStack item, Consumer<ItemMeta> consumer) {
        this(id, item);

        ItemMeta im = item.getItemMeta();
        consumer.accept(im);
        setItemMeta(im);
    }

    public CustomItemStack(String id, Material type, Consumer<ItemMeta> consumer) {
        this(id, new ItemStack(type), consumer);
    }

    public CustomItemStack(String id, Material type, String name, Consumer<ItemMeta> consumer) {
        this(id, type, meta -> {
            if (name != null) {
                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
            }

            consumer.accept(meta);
        });
    }

    public CustomItemStack(String id, ItemStack item, String name, String... lore) {
        this(id, item, im -> {
            if (name != null) {
                im.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
            }

            if (lore.length > 0) {
                List<String> lines = new ArrayList<>();

                for (String line : lore) {
                    lines.add(ChatColor.translateAlternateColorCodes('&', line));
                }
                im.setLore(lines);
            }
        });
    }

    public CustomItemStack(String id, Material type, String name, String... lore) {
        this(id, new ItemStack(type), name, lore);
    }

    public CustomItemStack(String id, Material type, Color color, String name, String... lore) {
        this(id, type, im -> {
            if (name != null) {
                im.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
            }

            if (lore.length > 0) {
                List<String> lines = new ArrayList<>();

                for (String line : lore) {
                    lines.add(ChatColor.translateAlternateColorCodes('&', line));
                }

                im.setLore(lines);
            }

            if (im instanceof LeatherArmorMeta) {
                ((LeatherArmorMeta) im).setColor(color);
            }

            if (im instanceof PotionMeta) {
                ((PotionMeta) im).setColor(color);
            }
        });
    }

    public CustomItemStack(String id, Color color, PotionEffect effect, String name, String... lore) {
        this(id, Material.POTION, im -> {
            if (name != null) {
                im.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
            }

            if (lore.length > 0) {
                List<String> lines = new ArrayList<>();

                for (String line : lore) {
                    lines.add(ChatColor.translateAlternateColorCodes('&', line));
                }

                im.setLore(lines);
            }

            if (im instanceof PotionMeta) {
                ((PotionMeta) im).setColor(color);
                ((PotionMeta) im).addCustomEffect(effect, true);

                if (effect.getType().equals(PotionEffectType.SATURATION)) {
                    im.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
                }
            }
        });
    }

    public CustomItemStack(CustomItemStack item, int amount) {
        this(item.getId(), item);
        setAmount(amount);
    }

    public CustomItemStack(String id, String texture, String name, String... lore) {
        this(id, ItemStackUtils.getSkull(id, texture), name, lore);
        this.texture = ItemStackUtils.getTexture(id, texture);
    }

    public CustomItemStack(String id, String texture, String name, Consumer<ItemMeta> consumer) {
        this(id, ItemStackUtils.getSkull(id, texture), meta -> {
            if (name != null) {
                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
            }

            consumer.accept(meta);
        });

        this.texture = ItemStackUtils.getTexture(id, texture);
    }

    public CustomItemStack(String id, String texture, Consumer<ItemMeta> consumer) {
        this(id, ItemStackUtils.getSkull(id, texture), consumer);
        this.texture = ItemStackUtils.getTexture(id, texture);
    }


    /**
     * ======================================================================================================
     * ======================================================================================================
     * 以下是CustomItemStack的功能区
     */

    /*
    Item的生命周期分为：创建——使用或食用——被注销
    Item的创建：
            在CustomItem中，由CustomItemStack实例化，并被注册到CustomItemManagement的样本库中。
            在游戏中的创建方式有两种且都是作为物品，一种是被放进某个容器中，另一种是被丢弃到某一坐标。
    Item的使用：作为物品使用（必须不可放置），作为方块使用，使用时可能被消耗
     */

    //跟使用动作中跟消耗有关的部分没写，需要加入
    public Consumer<PlayerInteractEvent> getUseAction() {
        return useAction;
    }

    public void setUseAction(Consumer<PlayerInteractEvent> newUseAction) {
        useAction = newUseAction;
    }

    public boolean hasUseAction() {
        return useAction != null ? true : false;
    }

    public void callUseAction(PlayerInteractEvent e) {
        try {
            useAction.accept(e);
        } catch (Exception | LinkageError x) {
            String errorText = this.id + "的使用动作useAction代码有误！";
            e.getPlayer().sendMessage(errorText);
            System.out.println(errorText);
        }
    }

    public boolean isUseActionOfBlock() {
        return hasUseAction() ? placeable : false;
    }


    public ItemStack getCloneItemStack() {
        return ((ItemStack) this).clone();
    }

    public ItemStack getCloneItemStack(int amount) {
        ItemStack is = getCloneItemStack();
        is.setAmount(amount);
        return is;
    }

    public <T extends ItemStack> T getCloneItemStack(Class<T> type) {
        ItemStack item = getCloneItemStack();
        return type.isInstance(item) ? type.cast(item) : null;
    }

    public <T extends ItemStack> T getCloneItemStack(Class<T> type, int amount) {
        ItemStack item = getCloneItemStack(amount);
        return type.isInstance(item) ? type.cast(item) : null;
    }


    public String getName() {
        String name = getDisplayName();
        if (name != null || !name.isEmpty()) {
            return name;
        }
        return ItemUtils.getItemName(this);
    }

    /**
     * 以上是CustomItemStack的功能区
     * ======================================================================================================
     * ======================================================================================================
     * 以下是CustomItemStack的基础部分
     */

    public final String getId() {
        return id;
    }

    public void setPlaceable(boolean isPlaceable) {
        validate();
        placeable = isPlaceable;
    }

    public boolean isPlaceable() {
        return placeable;
    }

    public void setConsumable(boolean isConsumable) {
        validate();
        consumable = isConsumable;
    }

    public boolean isConsumable() {
        return consumable;
    }

    public ItemMetaSnapshot getItemMetaSnapshot() {
        return itemMetaSnapshot;
    }

    @Override
    public boolean setItemMeta(ItemMeta meta) {
        validate();
        itemMetaSnapshot = new ItemMetaSnapshot(meta);

        return super.setItemMeta(meta);
    }

    @Override
    public void setType(Material type) {
        validate();
        super.setType(type);
    }

    @Override
    public void setAmount(int amount) {
        validate();
        super.setAmount(amount);
    }


    private void validate() {
        if (locked) {
            throw new WrongItemStackException(id + "不可变的。");
        }
    }

    public void lock() {
        locked = true;
    }

    public ShapedRecipe getRecipe() {
        return recipe;
    }

    public void setRecipe(ShapedRecipe newRecipe) {
        recipe = newRecipe;
    }

    public void setSkullTexture(String newSkullTexture) {
        texture = newSkullTexture;
    }

    public Optional<String> getSkullTexture() {
        return Optional.ofNullable(texture);
    }

    public String getDisplayName() {
        if (itemMetaSnapshot == null) {
            // Just to be extra safe
            return null;
        }
        return itemMetaSnapshot.getDisplayName().orElse(null);
    }


    @Override
    public CustomItemStack clone() {
        CustomItemStack newCis = new CustomItemStack(id, this);
        newCis.setPlaceable(placeable);
        newCis.setConsumable(consumable);
        newCis.setSkullTexture(texture);
        newCis.setUseAction(useAction);
        return newCis;
    }

    @Override
    public String toString() {
        return "自定义物品 (" + id + (getAmount() > 1 ? (" x " + getAmount()) : "") + ')';
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean equals(Object obj) {
        // We don't want people to override this, it should use the super method
        return super.equals(obj);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int hashCode() {
        // We don't want people to override this, it should use the super method
        return super.hashCode();
    }

}
