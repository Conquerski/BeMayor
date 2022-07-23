package me.bemayor.components.common;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;

import static org.bukkit.Bukkit.getServer;

public class EconomyUtils {
    public static Economy econ;
    public static void setupEcon(){
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) { return; }
        econ = rsp.getProvider();
    }

}
