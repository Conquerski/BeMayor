package me.bemayor.components.theoriginalmodify.listener;

import io.github.bakedlibs.dough.config.Config;
import me.bemayor.components.theoriginalmodify.random.MiningRandom;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;


/**
 * 破坏方块事件
 *
 * @author ConquerSki
 * @create 2022/7/8 13:50
 */
public class BlockBreakEventListener implements Listener {

    public int stoneAppleProbability=200;
    public int stoneAppleAmount=1;

    public int stoneExpProbability=200;
    public int stoneExpAmount=50;

    public int deepslateAppleProbability=400;
    public int deepslateAppleAmount=1;


    public boolean on_off=true;
    MiningRandom m = new MiningRandom();
    @EventHandler
    public void miningRandom(BlockBreakEvent event) {
        if(on_off) {
            Player player = event.getPlayer();
            World world = player.getWorld();
            Block block = event.getBlock();
            Location location = block.getLocation();
            if (m.miningRandom(stoneAppleProbability, "stone", block)) {
                world.dropItemNaturally(location, new ItemStack(Material.ENCHANTED_GOLDEN_APPLE,stoneAppleAmount));//奖励附魔金苹果
                PlayerSendMsg.SendMsgMining(player, stoneAppleAmount, "附魔金苹果");
            }
            if (m.miningRandom(deepslateAppleProbability, "deepslate", block)) {
                world.dropItemNaturally(location, new ItemStack(Material.ENCHANTED_GOLDEN_APPLE,deepslateAppleAmount));//奖励附魔金苹果
                PlayerSendMsg.SendMsgMining(player, deepslateAppleAmount, "附魔金苹果");
            }
            if (m.miningRandom(stoneExpProbability, "stone", block)) {
                event.setExpToDrop(stoneExpAmount);
                PlayerSendMsg.SendMsgMining(player, stoneExpAmount, "Exp");
            }
        }
    }
    //检测自然放置

    //载入配置文件数据
    public void setupConfigData(Config config){
        try {
            on_off=config.getBoolean("blockBreakEvent.onOff");

            stoneAppleProbability=config.getInt("blockBreakEvent.stone.enchantedGoldenApple.probability");
            stoneAppleAmount=config.getInt("blockBreakEvent.stone.enchantedGoldenApple.amount");

            stoneExpProbability=config.getInt("blockBreakEvent.stone.exp.probability");
            stoneExpAmount=config.getInt("blockBreakEvent.stone.exp.amount");

            deepslateAppleProbability=config.getInt("blockBreakEvent.deepslate.enchantedGoldenApple.probability");
            deepslateAppleAmount=config.getInt("blockBreakEvent.deepslate.enchantedGoldenApple.amount");
        } catch (Exception x) {
            System.out.println("配置文件" + config.getFile().getName() + "中的BlockBreakEvent有错误！具体信息：" + x);
        }
    }

}
