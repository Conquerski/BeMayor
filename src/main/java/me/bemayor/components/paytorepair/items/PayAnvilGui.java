package me.bemayor.components.paytorepair.items;

import java.util.ArrayList;
import java.util.Arrays;

import io.github.bakedlibs.dough.items.ItemUtils;
import me.bemayor.api.common.ItemMetaBuilder;
import me.bemayor.components.paytorepair.CalCostsResult;
import me.bemayor.components.paytorepair.PayToRepairUsage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import me.bemayor.api.customgui.GuiButton;
import me.bemayor.api.customgui.WorkbenchGui;

public class PayAnvilGui {
    private double costSum;
    private int repairNum;
    private final PayToRepairUsage usage;
    private final WorkbenchGui myGui;

    /*
    WorkbenchGui是可以任意定义输入格数、按钮数量、输出格数的工作台界面。
    该界面在打开时会显示预制的使用说明，界面在关闭时会自动将界面里的物品放入玩家背包，若玩家背包空间不够则放在地上。
    在工作台产生出新物品时，若输出栏没有空间则自动将输出栏的物品放入玩家背包。
    使用时你需要定义：
        -界面标题
        -界面打开时显示的使用说明
        -输入格数（最多6个，竖直排列，默认1个）
        -输出格数（最多6个，竖直排列，默认1个）
        -按钮列表（最多6个，竖直排列，缺省时默认有1个按钮）
        -每个按钮的显示名称和标签、外观、触发动作
        -输入栏发生改变时触发的动作
        -输出栏有物品被拿走时触发的动作
        -界面关闭时显示的文本
     */
    public PayAnvilGui(PayToRepairUsage newUsage) {
        this.usage = newUsage;

        costSum = 0;
        repairNum = 0;
        myGui = new WorkbenchGui(ChatColor.DARK_RED + "修理台", 1, 1, 2);
        myGui.setButtons(new ArrayList<GuiButton>(Arrays.asList(
                new GuiButton("§6✦ 计算费用 ✦", Material.WRITABLE_BOOK, event -> {
                    clickButtons(event, false);
                }),
                new GuiButton("§6✦ 确认修理 ✦", Material.ANVIL, event -> {
                    clickButtons(event, true);
                })
        )));
        myGui.setDescription(
                "§6[修理台] 花费金币来修理工具、武器、装备§d\n" +
                        "     使用方法：\n" +
                        "        1.将要修理的物品放在左侧的§b红色栏位§d；\n" +
                        "        2.单击§b书笔按钮§d计算费用；\n" +
                        "        3.也可以直接单击§b铁毡按钮§d进行修理；\n" +
                        "        4.关闭界面修好的物品会§b自动进入背包§d。"
        );
        myGui.setCloseText("");
        myGui.setGuiCloseAction(event -> {
            event.getPlayer().sendMessage("§6[修理台] §d本次修理共计花费金币§b" + costSum + "§d元，维修§c" + repairNum + "§d件。");
        });
    }

    public void open(Player player) {
        myGui.open(player);
    }

    private void clickButtons(InventoryClickEvent event, boolean isExecute) {
        Player player = (Player) event.getWhoClicked();
        ArrayList<String> ConfirmLore;
        if (myGui.hasInputItem()) {
            ItemStack myTool = myGui.getInputItem();
            String myToolName = ItemUtils.getItemName(myTool);
            ItemMetaBuilder meta = PayToRepairUsage.checkTool(myTool);
            if (meta != null) {
                if (meta.getDamage() != 0) {
                    int currentDamage = meta.getDamage();
                    CalCostsResult result = usage.calCosts(meta, myTool.getType().toString(), currentDamage, player, true);
                    double currentCost = result.costs;
                    int currentRepairDamage = result.repairDamage;
                    if (currentCost > 0.01) {
                        if (isExecute) {
                            player.playSound(player.getEyeLocation(), Sound.BLOCK_SMITHING_TABLE_USE, 1F, 1F);
                            PayToRepairUsage.repairTool(myTool, currentRepairDamage);
                            myGui.setInputToOutput(myTool, player);
                            PayToRepairUsage.castAccounts(player, currentCost);
                            ConfirmLore = new ArrayList<String>(Arrays.asList(
                                    "=========",
                                    "§f已修理完成"
                            ));
                            player.sendMessage("§6[修理台] §d您的" + myToolName + "§d修复至§b" + currentRepairDamage + "§d损伤，花费§c" + currentCost + "§d元，您的账户余额还剩§b" + PayToRepairUsage.getBalance(player) + "§d元。");
                            calSum(currentCost);
                        } else {
                            player.playSound(player.getEyeLocation(), Sound.ITEM_BOOK_PAGE_TURN, 1F, 1F);
                            ConfirmLore = new ArrayList<String>(Arrays.asList(
                                    "=========",
                                    "§f当前损耗耐久：§c" + currentDamage,
                                    "§f修复所需花费：§b" + currentCost
                            ));
                            player.sendMessage("§6[修理台] §d您的" + myToolName + "§d要修复至§b" + currentRepairDamage + "§d损伤，预计花费§c" + currentCost + "§d元。");
                        }
                    } else {
                        ConfirmLore = new ArrayList<String>(Arrays.asList(
                                "=========",
                                "§f您的账户余额不足"
                        ));
                        player.sendMessage("§6[修理台] §d您的账户余额不足，账户余额§b" + PayToRepairUsage.getBalance(player) + "§d元。");
                    }
                } else {
                    ConfirmLore = new ArrayList<String>(Arrays.asList(
                            "=========",
                            "§f此物品不需要修理"
                    ));
                    player.sendMessage("§6[修理台] §c此物品看上去不需要修理！");
                }
            } else {
                ConfirmLore = new ArrayList<String>(Arrays.asList(
                        "=========",
                        "§f此物品不能修理"
                ));
                player.sendMessage("§6[修理台] §c此物品不能修理！");
            }
        } else {
            ConfirmLore = new ArrayList<String>(Arrays.asList(
                    "=========",
                    "§f请将要修理的工具放入左侧"
            ));
            player.sendMessage("§6[修理台] §d请将要修理的工具放入左侧的红色栏位。");
        }
        myGui.getButtons().get(0).setLore(ConfirmLore);
        myGui.refreshButton(0);
        event.setCancelled(true);
    }

    private void calSum(double newCosts) {
        costSum += newCosts;
        repairNum++;
    }

}
