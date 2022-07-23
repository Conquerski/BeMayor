package me.bemayor.components.theoriginalmodify.listener;

import io.github.bakedlibs.dough.config.Config;
import me.bemayor.components.theoriginalmodify.random.DurabilityRandom;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;

import static me.bemayor.components.theoriginalmodify.random.RandomDr.dr;

public class MultipleDurabilityListener implements Listener {

    public int outfitRate = 2;
    public int netheriteRate = 6;
    public int diamondRate = 5;
    public int ironRate = 3;
    public int goldenRate = 1;
    public int stoneRate = 2;
    public int woodenRate = 1;
    public int elytraRate = 1;
    public int otherRate = 1;


    public int dr1Probability = 300, dr1Amount = 30;
    public int dr2Probability = 300, dr2Amount = -30;
    public int dr3Probability = 150, dr3Amount = 10;
    public int dr4Probability = 150, dr4Amount = -10;
    public int dr5Probability = 200, dr5Amount = 20;
    public int dr6Probability = 200, dr6Amount = -20;


    public boolean on_off = true;
    DurabilityRandom dRandom = new DurabilityRandom();

    @EventHandler
    public void calDurability(PlayerItemDamageEvent event) {
        if (on_off) {
            int damage = event.getDamage();//damage 本次事件即将增加的消耗耐久
            int newDamage = 0;
            String name = event.getItem().getType().toString();//物品名称
            //根据品阶制定消耗倍率
            if (name.contains("HELMET")||name.contains("CHESTPLATE")||name.contains("LEGGINGS")||name.contains("BOOTS")){
                newDamage = outfitRate * damage;
            } else if (name.contains("NETHERITE")) {
                newDamage = netheriteRate * damage;
            } else if (name.contains("DIAMOND")) {
                newDamage = diamondRate * damage;
            } else if (name.contains("IRON")) {
                newDamage = ironRate * damage;
            } else if (name.contains("GOLDEN")) {
                newDamage = goldenRate * damage;
            } else if (name.contains("STONE")) {
                newDamage = stoneRate * damage;
            } else if (name.contains("WOODEN")) {
                newDamage = woodenRate * damage;
            } else if (name.contains("ELYTRA")) {
                newDamage = elytraRate * damage;
            } else {
                newDamage = otherRate * damage;
            }
            event.setDamage(newDamage);

            //随机事件
            int r = dr.nextInt(6);
            switch (r) {
                case 1:
                    dRandom.durabilityRandom(dr1Probability, dr1Amount, event);
                    break;
                case 2:
                    dRandom.durabilityRandom(dr2Probability, dr2Amount, event);
                    break;
                case 3:
                    dRandom.durabilityRandom(dr3Probability, dr3Amount, event);
                    break;
                case 4:
                    dRandom.durabilityRandom(dr4Probability, dr4Amount, event);
                    break;
                case 5:
                    dRandom.durabilityRandom(dr5Probability, dr5Amount, event);
                    break;
                case 6:
                    dRandom.durabilityRandom(dr6Probability, dr6Amount, event);
                    break;

            }
        }
    }


    public void setupConfigData(Config config) {
        try {
            on_off = config.getBoolean("multipleDurability.onOff");

            outfitRate = config.getInt("multipleDurability.rate.outfit");
            netheriteRate = config.getInt("multipleDurability.rate.netherite");
            diamondRate = config.getInt("multipleDurability.rate.diamond");
            ironRate = config.getInt("multipleDurability.rate.iron");
            goldenRate = config.getInt("multipleDurability.rate.golden");
            stoneRate = config.getInt("multipleDurability.rate.stone");
            woodenRate = config.getInt("multipleDurability.rate.wooden");
            elytraRate = config.getInt("multipleDurability.rate.elytra");
            otherRate = config.getInt("multipleDurability.rate.other");


            dr1Probability = config.getInt("multipleDurability.dr1.probability");
            dr1Amount = config.getInt("multipleDurability.dr1.amount");

            dr2Probability = config.getInt("multipleDurability.dr2.probability");
            dr2Amount = config.getInt("multipleDurability.dr2.amount");

            dr3Probability = config.getInt("multipleDurability.dr3.probability");
            dr3Amount = config.getInt("multipleDurability.dr3.amount");

            dr4Probability = config.getInt("multipleDurability.dr4.probability");
            dr4Amount = config.getInt("multipleDurability.dr4.amount");

            dr5Probability = config.getInt("multipleDurability.dr5.probability");
            dr5Amount = config.getInt("multipleDurability.dr5.amount");

            dr6Probability = config.getInt("multipleDurability.dr6.probability");
            dr6Amount = config.getInt("multipleDurability.dr6.amount");
        } catch (Exception x) {
            System.out.println("配置文件" + config.getFile().getName() + "中的MultipleDurability有错误！具体信息：" + x);
        }
    }


}
