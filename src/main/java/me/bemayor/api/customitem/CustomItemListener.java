package me.bemayor.api.customitem;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import io.github.bakedlibs.dough.protection.Interaction;
import me.bemayor.api.integrations.IntegrationsManager;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import me.bemayor.api.common.ItemStackUtils;
import me.bemayor.api.common.PlayerUtils;

public class CustomItemListener implements Listener {

    private CustomItemManagement manager;
    public CustomItemListener(CustomItemManagement management) {
        manager=management;
        JavaPlugin j= manager.getApiManager().getPlugin();
        j.getServer().getPluginManager().registerEvents(this, j);
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if(!rightClickItem(e)){
                rightClickBlock(e);
            }
        }
    }

    private boolean rightClickItem(PlayerInteractEvent event) {
        ItemStack is=event.getItem();
        if(ItemStackUtils.isExistence(is)){
            String id=CustomItemManagement.getIdByItem(is);
            if (id!=null && !id.isEmpty()) {
                CustomItemStack cItem=manager.getTemplateById(id);
                if(cItem!=null && !cItem.isUseActionOfBlock()){
                    event.setCancelled(true);//因为该物品是不可放置的，所以取消放置动作
                    cItem.callUseAction(event);
                    if(cItem.isConsumable()){
                        int n=event.getItem().getAmount();
                        event.getItem().setAmount(n-1);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    private void rightClickBlock(PlayerInteractEvent event) {
        if(PlayerUtils.isInteractBlock(event)){
            Block b = event.getClickedBlock();
            if(ItemStackUtils.isExistence(b)){
                String id=CustomItemManagement.getIdByBlock(b);
                if (id!=null && !id.isEmpty()) {
                    CustomItemStack cItem = manager.getTemplateById(id);
                    if (cItem!=null && cItem.hasUseAction()) {
                        event.setCancelled(true);//触发该方块的使用动作，所以取消其他可能的放置动作
                        cItem.callUseAction(event);
                        if(cItem.isConsumable()){
                            event.getClickedBlock().setType(Material.AIR);
                        }
                    }
                }
            }
        }
    }


    /**
     * 以上为自定义物质的使用监听
     * ======================================================================================================
     * ======================================================================================================
     * 以下为自定义方块的放置和破坏监听
     */

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onBlockPlaceExisting(BlockPlaceEvent e) {
        Block block = e.getBlock();

        // Fixes #2636 - This will solve the "ghost blocks" issue
        if (e.getBlockReplacedState().getType().isAir()) {
            CustomItemStack cItem = manager.getCloneByBlock(block);
            if (cItem != null) {
                block.getWorld().dropItemNaturally(block.getLocation(), cItem);
            }
        } else{
            String blockId=CustomItemManagement.getIdByBlock(block);
            if (blockId!=null && !blockId.isEmpty()) {
                // If there is no air (e.g. grass) then don't let the block be placed
                e.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent e) {
        CustomItemStack cItem = manager.getTemplateByItem(e.getItemInHand());
        if (cItem != null && cItem.isPlaceable()) {
            if (CustomItemManagement.isTileEntity(e.getBlock().getType())) {
                CustomItemManagement.setBlockData(e.getBlock(), cItem.getId());
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent e) {
        IntegrationsManager integrationsManager=manager.getApiManager().getIntegrationsManager();

        // Simply ignore any events that were faked by other plugins
        if (integrationsManager.isEventFaked(e)) {
            return;
        }

        // Also ignore custom blocks which were placed by other plugins
        if (integrationsManager.isCustomBlock(e.getBlock())) {
            return;
        }

        if (!e.isCancelled()) {
            List<ItemStack> drops = new ArrayList<>();
            setBlockToDrops(e, drops);
            dropItems(e, drops,integrationsManager);
        }
    }
    private void setBlockToDrops(BlockBreakEvent e, List<ItemStack> drops) {
        Block block = e.getBlock();
        if (CustomItemManagement.isTileEntity(block.getType())) {
            CustomItemStack cItem = manager.getCloneByBlock(block);
            if (cItem!= null) {
                if (e.isCancelled()) {
                    return ;
                }
                drops.add(cItem);
            }
        }
    }
    private void dropItems(BlockBreakEvent e, List<ItemStack> drops,IntegrationsManager integrationsManager) {
        if (!drops.isEmpty() && !e.isCancelled()) {
            // Notify plugins like CoreProtect
            integrationsManager.getProtectionManager().logAction(e.getPlayer(), e.getBlock(), Interaction.BREAK_BLOCK);

            // Fixes #2560
            if (e.isDropItems()) {
                // Disable normal block drops
                e.setDropItems(false);

                for (ItemStack drop : drops) {
                    // Prevent null or air from being dropped
                    if (drop != null && drop.getType() != Material.AIR) {
                        e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), drop);
                    }
                }
            }
        }
    }
}
