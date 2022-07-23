package me.bemayor.components.theoriginalmodify.listener;

import org.bukkit.entity.Player;

public class PlayerSendMsg {
    /**
     * @param player "接收信息的玩家参数"
     * @param amount “随机事件带来的数值”
     * @param name   “影响物品的名称，耐久度事件填参数“
     * @author ConquerSki
     */
    public static void SendMsg(Player player, Integer amount, String name) {
        if (amount > 0) {
            player.sendMessage("§2[隐藏事件]§6你真是太幸运了，你的 §b" + name + "§2 增加了§b " + amount + " §6耐久！");
        } else if (amount < 0) {
            player.sendMessage("§2[隐藏事件]§6你运气太差了了，你的 §b" + name + "§2 减少了§b " + Math.abs(amount) + " §6耐久！");
        }
    }

    /**
     * @param player "接收信息的玩家参数"
     * @param amount “随机事件带来的数值”
     * @param name   “影响物品的名称，掉落物事件填想展现的名字，经验事件填Exp“
     * @author ConquerSki
     */
    public static void SendMsgMining(Player player, Integer amount, String name) {
        if (name.equals("Exp")) {
            player.sendMessage("§2[隐藏事件]§6你真是太幸运了，挖掘方块爆出了§b " + amount + " §6点§b经验值");
        } else {
            player.sendMessage("§2[隐藏事件]§6你真是太幸运了，掉落了 §b" + amount + " §6个 §b" + name);
        }
    }
}
