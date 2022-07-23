package me.bemayor.components.paytorepair.items;

import io.github.bakedlibs.dough.config.Config;
import me.bemayor.api.common.ChatUtils;
import me.bemayor.api.common.ConfigUtils;
import me.bemayor.api.common.ItemMetaBuilder;
import me.bemayor.components.common.EconomyUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import static org.bukkit.enchantments.Enchantment.DURABILITY;
import static org.bukkit.enchantments.Enchantment.MENDING;

public class PayAnvilUasge {
    private static final String CONFIG_FILE_NAME = "payAnvilUsage.yml";//注意需要在项目资源中添加这个文件
    private final Config config;

    public PayAnvilUasge() {
        ConfigUtils.setFile(CONFIG_FILE_NAME, "付费修理台的算法");
        config = new Config(JavaPlugin.getProvidingPlugin(PayAnvilUasge.class), CONFIG_FILE_NAME);
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

    public CalCostsResult calCosts(ItemMetaBuilder metaSnapshot, String type, int currentDamage, Player player) {
        ItemMeta meta = metaSnapshot.getMeta();

        double typeCoe = 1.0;
        try {
            if (type.contains("HELMET") || type.contains("CHESTPLATE") || type.contains("LEGGINGS") || type.contains("BOOTS")) {
                typeCoe = config.getDouble("typeCoe.outfit");
            } else if (type.contains("DIAMOND")) {
                typeCoe = config.getDouble("typeCoe.diamond");
                ;
            } else if (type.contains("NETHERITE")) {
                typeCoe = config.getDouble("typeCoe.nephrite");
                ;
            } else {
                typeCoe = config.getDouble("typeCoe.other");
                ;
            }
        } catch (Exception x) {
            System.out.println("付费修理台的算法的配置文件" + CONFIG_FILE_NAME + "中有错误，请删除该文件！具体信息：" + x);
        }

        int quantity = meta.getEnchants().size();
        if (meta.hasEnchant(MENDING)) {
            quantity--;
        }

        int durabilityCoe = meta.getEnchantLevel(DURABILITY);

        if (durabilityCoe != 0) {
            quantity = quantity - 1;
        }

        double totalCoe = (quantity + 1) * (durabilityCoe + 1) * typeCoe * config.getDouble("typeCoe.perCosts");
        ;

        double budget = ChatUtils.monetization(totalCoe * currentDamage);
        double balance = getBalance(player);
        CalCostsResult result = new CalCostsResult();
        if (balance > budget) {
            result.costs = budget;
            result.repairDamage = 0;
        } else if (balance > 1) {
            result.costs = balance - 1;
            result.repairDamage = currentDamage - (int) (balance / totalCoe) - 1;
        }
        result.costs = 0;
        result.repairDamage = currentDamage;
        return result;
    }

    public static double getBalance(Player player) {
        return EconomyUtils.econ.getBalance(player);
    }

    public static void castAccounts(Player player, double cost) {
        player.performCommand("pay tax " + cost);
    }
}
