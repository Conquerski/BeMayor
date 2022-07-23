package me.bemayor.components.theoriginalmodify.random;

import me.bemayor.components.theoriginalmodify.listener.PlayerSendMsg;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import static me.bemayor.components.theoriginalmodify.random.RandomDr.dr;

public class DurabilityRandom {
    /**
     * @param probability      "设置概率，此参数传递的是分母，概率为1/分母"
     * @param durabilityAmount "设置事件中影响的耐久度数值，正为加，负为减"
     * @param event            "传递触发事件"
     *
     * @author ConquerSki
     */
    public void durabilityRandom(Integer probability, Integer durabilityAmount, PlayerItemDamageEvent event) {
        if ((probability == null || durabilityAmount == null || event == null)) return;

        Player player = event.getPlayer();//获取玩家信息
        ItemStack item = event.getItem();//item 受影响的物品ItemStack类型
        ItemMeta itemMeta = item.getItemMeta();//itemMeta 物品Meta
        Damageable damageableMeta = (Damageable) itemMeta;//damageableMeta
        int existDamage = damageableMeta.getDamage();//existDamage 物品已经存在的消耗耐久
        int maxDurability = item.getType().getMaxDurability();//maxDurability 物品最大耐久度
        String name = item.getType().toString();//物品名称

        if (dr.nextInt(probability) == 0) {
            if (durabilityAmount < 0) {//消耗更多耐久
                int absAmount = Math.abs(durabilityAmount);
                if (existDamage + absAmount < maxDurability) {
                    event.setDamage(absAmount);
                } else {
                    damageableMeta.setDamage(maxDurability - 1);//剩1点耐久防止爆装备
                    item.setItemMeta((ItemMeta) damageableMeta);
                    event.setCancelled(true);
                }
            } else if (durabilityAmount > 0) {//获得奖励耐久
                if (existDamage > durabilityAmount) {
                    damageableMeta.setDamage(existDamage - durabilityAmount);
                } else {
                    damageableMeta.setDamage(0);//修复装备，应为满耐久
                }
                item.setItemMeta((ItemMeta) damageableMeta);
                event.setCancelled(true);
            }
            PlayerSendMsg.SendMsg(player, durabilityAmount, name);
            return;

        }

    }
}
