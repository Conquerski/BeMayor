package me.bemayor.components.transition;

import de.tr7zw.changeme.nbtapi.NBTEntity;
import io.github.bakedlibs.dough.config.Config;
import io.papermc.lib.PaperLib;
import me.bemayor.BeMayor;
import me.bemayor.api.common.ConfigUtils;
import me.bemayor.api.common.ItemStackUtils;
import org.bukkit.*;
import org.bukkit.block.Beacon;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class TransitionUsage {


    public static final List<Location> destinations = new ArrayList<>();


    //配置文件定义
    public static final String CONFIG_FILE_NAME = "Transition.yml";//注意需要在项目资源中添加这个文件
    public static final String DESTINATIONS_KEY = "transportCoreDestination";
    public final Config config;


    public TransitionUsage() {
        //配置文件创建
        ConfigUtils.setFile(CONFIG_FILE_NAME, "跃迁组件");
        config = new Config(JavaPlugin.getProvidingPlugin(BeMayor.class), CONFIG_FILE_NAME);

        loadDestinations();
    }

    public void loadDestinations() {
        List<String> l = getConfigDestinations();
        if (!l.isEmpty()) {
            for (String s : l) {
                destinations.add(getLocationFromString(s));
            }
        }
    }

    public List<String> getConfigDestinations() {
        return config.getStringList(DESTINATIONS_KEY);
    }

    public void setConfigDestinations(List<String> configDestinations) {
        config.setValue(DESTINATIONS_KEY, configDestinations);
        config.save();
    }

    public void addDestination(Location destination) {
        if (findDestination(destination) == null) {
            destinations.add(destination);
            List<String> l = getConfigDestinations();
            l.add(getStringFromLocation(destination));
            setConfigDestinations(l);
        }
    }

    public void removeDestination(Location destination) {
        Location l = findDestination(destination);
        if (l != null) {
            destinations.remove(l);
            List<String> ls = getConfigDestinations();
            ls.remove(getStringFromLocation(l));
            setConfigDestinations(ls);
        }
    }

    public static Location findDestination(Location destination) {
        if (destination == null) {
            return null;
        }
        if (!destinations.isEmpty()) {
            boolean b = false;
            Location find = null;
            for (Location l : destinations) {
                if (l.equals(destination) || equalLocations(l, destination)) {
                    find = l;
                    b = true;
                    break;
                }
            }
            if (b) {
                return find;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public static boolean equalLocations(Location location1, Location location2) {
        if (location1 == null || location2 == null) {
            return false;
        }
        boolean b = false;
        if (location1.getWorld().getName().equalsIgnoreCase(location2.getWorld().getName())) {
            if (location1.getBlockX() == location2.getBlockX()) {
                if (location1.getBlockZ() == location2.getBlockZ()) {
                    b = true;
                }
            }
        }
        return b;
    }


    public static Location getDestination(Player player, Location transportCoreLocation) {
        if (transportCoreLocation != null) {
            World world = player.getWorld();
            int startY = transportCoreLocation.getBlockY() - 1, endY = 0;
            if (world.getName().equalsIgnoreCase("world")) {
                endY = -64;
            }
            if (startY > endY) {
                Location l = new Location(world, transportCoreLocation.getBlockX(), startY, transportCoreLocation.getBlockZ());
                Location destination = null;
                for (int y = startY; y > endY; y--) {
                    l.setY(y);
                    Block b = l.getBlock();
                    if (ItemStackUtils.isExistence(b)) {
                        if (b.getType() == Material.BEACON) {
                            if (((Beacon) b.getState()).getTier() > 0) {
                                destination = l;
                                destination.setY(l.getBlockY() + 2);
                            } else {
                                player.sendMessage("§6[跃迁核心] §c信标没有充能，传送阵构建失败！");
                            }
                        } else if (b.getType().toString().toLowerCase().contains("glass")) {
                            continue;
                        } else {
                            player.sendMessage("§6[跃迁核心] §c下方没有安装信标，传送阵构建失败！");
                        }
                        break;
                    }
                }
                return destination;
            } else {
                player.sendMessage("§6[跃迁核心] §c放置位置高度太低！");
                return null;
            }
        } else {
            player.sendMessage("§6[跃迁核心] §c没有放置&6跃迁核心§c！");
            return null;
        }
    }

    public static boolean beaconCheck(Player player, Location destination) {
        int y = destination.getBlockY() - 2;
        Location beacon = new Location(destination.getWorld(), destination.getBlockX(), y, destination.getBlockZ());
        if (beacon.getWorld().getBlockAt(beacon).getType() == Material.BEACON) {
            return true;
        } else {
            return false;
        }
    }

    public static void setLocationToItem(ItemStack itemStack, String locationString) {
        ItemMeta meta = itemStack.getItemMeta();
        List<String> lore = meta.getLore();
        if (lore.size() > 1) {
            lore.set(1, locationString);
            meta.setLore(lore);
            itemStack.setItemMeta(meta);
        }
    }

    public static String getStringFromLocation(Location location) {
        return location.getWorld().getName() + "." +
                location.getBlockX() + "." +
                location.getBlockY() + "." +
                location.getBlockZ();
    }

    public static Location getLocationFromString(String locationString) {
        String[] s = locationString.split("\\.");
        if (s.length >= 4) {
            World w = BeMayor.getPlugin(BeMayor.class).getServer().getWorld(s[0]);
            return new Location(w, Double.valueOf(s[1]), Double.valueOf(s[2]), Double.valueOf(s[3]));
        } else {
            return null;
        }
    }

    public static void transitionDestination(Player player, Location destination) {
        if (player == null || destination == null) {
            return;
        }
        PaperLib.teleportAsync(player, new Location(destination.getWorld(), destination.getBlockX(), 1000, destination.getBlockZ()));

        //if(BeMayor.getApiManager().getIntegrationsManager().isNBTAPIInstalled()){
        //    Plugin integration = BeMayor.getMyServer().getPluginManager().getPlugin("NBTAPI");
        //    NBTAPI nbtApi
        //} else{
        NBTEntity nbti = new NBTEntity(destination.getWorld().spawnEntity(destination, EntityType.ENDER_PEARL));
        nbti.setUUID("Owner", player.getUniqueId());
        //}
    }

}
