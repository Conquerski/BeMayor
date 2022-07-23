package me.bemayor.api.integrations;

import me.minebuilders.clearlag.Clearlag;
import me.minebuilders.clearlag.events.EntityRemoveEvent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Iterator;

/**
 * This handles all integrations with {@link Clearlag}.
 * We don't want it to clear our altar items.
 * 
 * @author TheBusyBiscuit
 *
 */
class ClearLagIntegration implements Listener {

    private static final String NO_PICKUP_METADATA = "no_pickup";

    private final JavaPlugin plugin;

    ClearLagIntegration(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void register() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onEntityRemove(EntityRemoveEvent e) {
        Iterator<Entity> iterator = e.getEntityList().iterator();

        while (iterator.hasNext()) {
            Entity n = iterator.next();

            if (n instanceof Item && ((Item) n).hasMetadata(NO_PICKUP_METADATA)) {
                iterator.remove();
            }
        }
    }
}
