package me.bemayor.components.paytorepair.commands;

import io.github.bakedlibs.dough.items.ItemUtils;
import me.bemayor.api.commands.PlayerCommand;
import me.bemayor.api.commands.SubCommand;
import me.bemayor.api.commands.subcommands.GiveCommand;
import me.bemayor.api.commands.subcommands.setcommand.SetDoubleCommand;
import me.bemayor.api.common.ItemMetaBuilder;
import me.bemayor.components.ComponentManagement;
import me.bemayor.components.paytorepair.CalCostsResult;
import me.bemayor.components.paytorepair.PayToRepair;
import me.bemayor.components.paytorepair.PayToRepairUsage;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class PayToRepairCommands {

    private PlayerCommand playerCommand;
    private SubCommand subCommand;

    public PayToRepairCommands(PayToRepair upper, ComponentManagement manager, List<String> itemIdListForTab) {
        subCommand = new SubCommand("payToRepair", "付费修理组件的指令集，或者打开修复台界面", false,
                event -> {
                    upper.openPayAnvil((Player) event.getCommandSender());
                },
                new ArrayList<>(Arrays.asList(
                        new GiveCommand("给与玩家付费修理组件的物品", manager, itemIdListForTab),
                        new SubCommand("set", "设置付费修理算法的参数", false, null, new ArrayList<>(Arrays.asList(
                                new SubCommand("typeCoe", "设置材质权重", false, null, new ArrayList<>(Arrays.asList(
                                        new SetDoubleCommand("outfit", "装备的权重", upper.config, "typeCoe.outfit",
                                                () -> {
                                                    return new ArrayList<>(Arrays.asList("0.0", String.valueOf(upper.usage.typeCoeOutfit), "10.0"));
                                                },
                                                i -> {
                                                    upper.usage.typeCoeOutfit = i;
                                                }),
                                        new SetDoubleCommand("diamond", "钻石质的权重", upper.config, "typeCoe.diamond",
                                                () -> {
                                                    return new ArrayList<>(Arrays.asList("0.0", String.valueOf(upper.usage.typeCoeDiamond), "10.0"));
                                                },
                                                i -> {
                                                    upper.usage.typeCoeDiamond = i;
                                                }),
                                        new SetDoubleCommand("netherite", "下界合金质的权重", upper.config, "typeCoe.netherite",
                                                () -> {
                                                    return new ArrayList<>(Arrays.asList("0.0", String.valueOf(upper.usage.typeCoeNetherite), "10.0"));
                                                },
                                                i -> {
                                                    upper.usage.typeCoeNetherite = i;
                                                }),
                                        new SetDoubleCommand("other", "其他材质的权重", upper.config, "typeCoe.other",
                                                () -> {
                                                    return new ArrayList<>(Arrays.asList("0.0", String.valueOf(upper.usage.typeCoeOther), "10.0"));
                                                },
                                                i -> {
                                                    upper.usage.typeCoeOther = i;
                                                })
                                ))),
                                new SubCommand("perCosts", "设置每耐久价格", false, null, new ArrayList<>(Arrays.asList(
                                        new SetDoubleCommand("quickFix", "快速维修指令的每耐久价格", upper.config, "perCosts.quickFix",
                                                () -> {
                                                    return new ArrayList<>(Arrays.asList("0.0", String.valueOf(upper.usage.perCostsQuickFix), "10.0"));
                                                },
                                                i -> {
                                                    upper.usage.perCostsQuickFix = i;
                                                }),
                                        new SetDoubleCommand("payAnvil", "修理台的每耐久价格", upper.config, "perCosts.payAnvil",
                                                () -> {
                                                    return new ArrayList<>(Arrays.asList("0.0", String.valueOf(upper.usage.perCostsPayAnvil), "10.0"));
                                                },
                                                i -> {
                                                    upper.usage.perCostsPayAnvil = i;
                                                })
                                )))
                        )))
                ))
        );

        playerCommand = new PlayerCommand("xl", sender -> {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                ItemStack item = player.getInventory().getItemInMainHand();
                return repairIt(item, player, upper.usage);
            } else {
                return false;
            }
        });
    }

    public static boolean repairIt(ItemStack myTool, Player player, PayToRepairUsage usage) {
        if (myTool == null) {
            player.sendMessage("§6[快速修复服务]：§c您需要把要修复的物品拿在手上！");
            return false;
        }

        String myToolName = ItemUtils.getItemName(myTool);
        ItemMetaBuilder meta = PayToRepairUsage.checkTool(myTool);
        if (meta != null) {
            if (meta.getDamage() != 0) {
                int currentDamage = meta.getDamage();
                CalCostsResult result = usage.calCosts(meta, myTool.getType().toString(), currentDamage, player, false);
                double currentCost = result.costs;
                int currentRepairDamage = result.repairDamage;
                if (currentCost > 0.02) {
                    PayToRepairUsage.repairTool(myTool, currentRepairDamage);
                    PayToRepairUsage.castAccounts(player, currentCost);
                    player.sendMessage("§6[快速修复服务] §d您的" + myToolName + "§d修复至§b" + currentRepairDamage + "§d损伤，花费§c" + currentCost + "§d元，您的账户余额还剩§b" + PayToRepairUsage.getBalance(player) + "§d元。\n" +
                            "§6感谢您对本服务的使用，欢迎下次光临。");
                } else {
                    player.sendMessage("§6[快速修复服务] §d您的账户余额不足，账户余额§b" + PayToRepairUsage.getBalance(player) + "§d元。");
                }
            } else {
                player.sendMessage("§6[快速修复服务] §c您手里的物品看上去不需要修理！");
            }
        } else {
            player.sendMessage("§6[快速修复服务] §c您手里的物品不能修理！");
        }
        return true;
    }

    public SubCommand getSubCommand() {
        return subCommand;
    }

    public PlayerCommand getPlayerCommand() {
        return playerCommand;
    }
}
