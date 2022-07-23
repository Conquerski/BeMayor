package me.bemayor.components.theoriginalmodify.random;

import me.bemayor.components.common.CoreProtectUtils;
import org.bukkit.block.Block;

import static me.bemayor.components.theoriginalmodify.random.RandomDr.dr;


public class MiningRandom {

    public Boolean checkNaturalized(Block block) {
        return (CoreProtectUtils.api.blockLookup(block, (int) (System.currentTimeMillis() / 1000L)).isEmpty());
    }

    public Boolean miningRandom(Integer probability, String blockBreakName, Block block) {
        String blockName = block.getType().toString().toLowerCase();

        if (blockName.contains(blockBreakName)) {
            if (dr.nextInt(probability) == 0) {
                return checkNaturalized(block);
            }
        }
        return false;
    }
}
