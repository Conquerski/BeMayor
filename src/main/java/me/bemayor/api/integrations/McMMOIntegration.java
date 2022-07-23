package me.bemayor.api.integrations;

import com.gmail.nossr50.events.skills.salvage.McMMOPlayerSalvageCheckEvent;
import com.gmail.nossr50.mcMMO;
import me.bemayor.api.ApiManagement;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;


/**
 * This handles all integrations with {@link mcMMO}.
 * 
 * @author TheBusyBiscuit
 *
 */
class McMMOIntegration implements Listener {

    private final JavaPlugin plugin;
    private final ApiManagement apiManager;
    McMMOIntegration(JavaPlugin plugin,ApiManagement apiManagement) {
        this.plugin = plugin;
        apiManager=apiManagement;
    }

    public void register() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }


    @EventHandler(ignoreCancelled = true)
    public void onItemSalvage(McMMOPlayerSalvageCheckEvent e) {
        // Prevent Slimefun items from being salvaged
        if (!isSalvageable(e.getSalvageItem())) {
            e.setCancelled(true);
            apiManager.getChatMessages().sendMessage(e.getPlayer(), "anvil.mcmmo-salvaging");
        }
    }

    private boolean isSalvageable(ItemStack item) {
        String id = apiManager.getCustomItemManager().getIdByItem(item);
        return id == null ;
    }

}
