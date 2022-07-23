package me.bemayor.api.common;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PlayerUtils {
    public static boolean isInteractBlock(PlayerInteractEvent event) {
        if (!event.getPlayer().isSneaking() || event.getItem() == null || event.getItem().getType() == Material.AIR) {
            return true;
        }
        return false;
    }

    public static Optional<Player> getPlayerFromOnlineList(String playerName) {
        Optional<Player> p = Optional.empty();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getDisplayName().equals(playerName)) {
                p = Optional.of(player);
                break;
            }
        }
        return p;
    }

    public static List<String> getPlayerNameListOnline() {
        List<String> l = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            l.add(player.getDisplayName());
        }
        return l;
    }
}
