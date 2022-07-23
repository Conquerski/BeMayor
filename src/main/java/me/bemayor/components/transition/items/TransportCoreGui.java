package me.bemayor.components.transition.items;

import me.bemayor.BeMayor;
import me.bemayor.api.customgui.GuiButton;
import me.bemayor.api.customgui.WorkbenchGui;
import me.bemayor.components.transition.TransitionUsage;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;

public class TransportCoreGui {

    private Location destination;
    private final WorkbenchGui myGui;

    public TransportCoreGui() {

        myGui = new WorkbenchGui(ChatColor.DARK_RED + "跃迁核心", 2, 1, 1);

        myGui.setButton(new GuiButton("§6✦ 卷轴合成 ✦", HeadTexture.EXPERIENCE_CUBE.getTexture(), this::clickButtons));

        myGui.setDescription(
                "§6[跃迁核心] 将末影珍珠、纸张、经验合成卷轴§d\n" +
                        "     使用方法：\n" +
                        "        1.将§b末影珍珠、纸张§d放在左侧的红色栏位；\n" +
                        "        2.单击合成按钮§b消耗玩家1级§d进行合成；\n" +
                        "        3.关闭界面合成的卷轴会自动进入背包。"
        );

        myGui.setCloseText("");
        myGui.setGuiCloseAction(event -> {
            Player p = (Player) event.getPlayer();
            p.playSound(p.getEyeLocation(), Sound.BLOCK_SHULKER_BOX_CLOSE, 1F, 1F);
        });
    }

    public void open(Player player, Location destination) {
        if (destination != null) {
            this.destination = destination;
            myGui.open(player);
        } else {
            player.sendMessage("§6[跃迁核心] §c跃迁核心没有放置在激活信标的光柱内！");
        }
    }

    private void clickButtons(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ArrayList<String> ConfirmLore;
        int playerLevel = player.getLevel();
        if (player.getGameMode() == GameMode.CREATIVE) {
            playerLevel = playerLevel + 1;
        }
        if (playerLevel > 0) {
            if (inputItemCheck()) {
                player.setLevel(playerLevel - 1);
                ItemStack outcome = BeMayor.getApiManager().getCustomItemManager().getTemplateById("TOWN_PORTAL").getCloneItemStack();
                TransitionUsage.setLocationToItem(outcome, TransitionUsage.getStringFromLocation(destination));
                myGui.setInputToOutput(outcome, player);
                ConfirmLore = new ArrayList<String>(Arrays.asList(
                        "=========",
                        "§f卷轴已合成完成"
                ));
                player.playSound(player.getEyeLocation(), Sound.ITEM_BOOK_PUT, 1F, 1F);
            } else {
                ConfirmLore = new ArrayList<String>(Arrays.asList(
                        "=========",
                        "§f请将末影珍珠和纸张放入左侧"
                ));
                player.sendMessage("§6[跃迁核心] §d请将末影珍珠和纸张放入左侧的红色栏位。");
            }
        } else {
            ConfirmLore = new ArrayList<String>(Arrays.asList(
                    "=========",
                    "§f您的等级不足"
            ));
            player.sendMessage("§6[跃迁核心] §c您的等级不足！");
        }
        myGui.getButtons().get(0).setLore(ConfirmLore);
        myGui.refreshButton(0);
        event.setCancelled(true);
    }

    private boolean inputItemCheck() {
        if (myGui.hasInputItem()) {
            boolean b1 = false;
            boolean b2 = false;
            for (ItemStack is : myGui.getInputItems()) {
                if (is.getType() == Material.ENDER_PEARL) {
                    b1 = true;
                    continue;
                }
                if (is.getType() == Material.PAPER) {
                    b2 = true;
                }
            }
            if (b1 && b2) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
