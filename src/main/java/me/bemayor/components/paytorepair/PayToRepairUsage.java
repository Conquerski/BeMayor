package me.bemayor.components.paytorepair;

import io.github.bakedlibs.dough.config.Config;
import me.bemayor.api.common.ChatUtils;
import me.bemayor.api.common.ItemMetaBuilder;
import me.bemayor.components.common.EconomyUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import static org.bukkit.enchantments.Enchantment.DURABILITY;
import static org.bukkit.enchantments.Enchantment.MENDING;

public class PayToRepairUsage {


    public double typeCoeOutfit = 0.8;
    public double typeCoeDiamond = 1.15;
    public double typeCoeNephrite = 1.4;
    public double typeCoeOther = 1.0;

    public double perCostsQuickFix = 0.03;
    public double perCostsPayAnvil = 0.01;

    private final Config config;

    public PayToRepairUsage(Config config) {
        this.config = config;
        setupConfigData(config);
    }

    public void setupConfigData(Config config) {
        try {
            typeCoeOutfit = config.getDouble("typeCoe.outfit");
            typeCoeDiamond = config.getDouble("typeCoe.diamond");
            typeCoeNephrite = config.getDouble("typeCoe.nephrite");
            typeCoeOther = config.getDouble("typeCoe.other");

            perCostsQuickFix = config.getDouble("perCosts.quickFix");
            perCostsPayAnvil = config.getDouble("perCosts.payAnvil");
        } catch (Exception x) {
            System.out.println("配置文件" + config.getFile().getName() + "中的NoMending有错误！具体信息：" + x);
        }
    }

    public static void repairTool(ItemStack isTool, int repairDamage) {
        ItemMeta meta = isTool.getItemMeta();
        ((Damageable) meta).setDamage(repairDamage);
        isTool.setItemMeta(meta);
    }

    public static ItemMetaBuilder checkTool(ItemStack isTool) {
        //存在检查、类型检查
        if (isTool == null) {
            return null;
        }
        ItemMeta im = isTool.getItemMeta();
        if (im instanceof Damageable) {
            ItemMetaBuilder meta = new ItemMetaBuilder(im);
            return meta;
        }
        return null;
    }

    public CalCostsResult calCosts(ItemMetaBuilder metaSnapshot, String type, int currentDamage, Player player, boolean isPayAnvil) {
        ItemMeta meta = metaSnapshot.getMeta();

        int quantity = meta.getEnchants().size();
        if (meta.hasEnchant(MENDING)) {
            quantity--;
        }

        int durabilityCoe = meta.getEnchantLevel(DURABILITY);

        if (durabilityCoe != 0) {
            quantity = quantity - 1;
        }

        double perCosts = 0.03;
        if (isPayAnvil) {
            perCosts = perCostsPayAnvil;
        } else {
            perCosts = perCostsQuickFix;
        }

        double typeCoe = 1.0, totalCoe = 1.0;

        if (type.contains("HELMET") || type.contains("CHESTPLATE") || type.contains("LEGGINGS") || type.contains("BOOTS")) {
            typeCoe = typeCoeOutfit;
        } else if (type.contains("DIAMOND")) {
            typeCoe = typeCoeDiamond;
        } else if (type.contains("NETHERITE")) {
            typeCoe = typeCoeNephrite;
        } else {
            typeCoe = typeCoeOther;
        }

        totalCoe = (quantity + 1) * (durabilityCoe + 1) * typeCoe * perCosts;

        double budget = ChatUtils.monetization(totalCoe * currentDamage);
        double balance = getBalance(player);
        CalCostsResult result = new CalCostsResult();
        if (balance > budget) {
            result.costs = budget;
            result.repairDamage = 0;
        } else if (balance > 1) {
            result.costs = balance - 1;
            result.repairDamage = currentDamage - (int)(balance / totalCoe) - 1;
        } else {
            result.costs = 0;
            result.repairDamage = currentDamage;
        }
        return result;
    }

    public static double getBalance(Player player) {
        return EconomyUtils.econ.getBalance(player);
    }

    public static void castAccounts(Player player, double cost) {
        player.performCommand("pay tax " + cost);
    }
}
