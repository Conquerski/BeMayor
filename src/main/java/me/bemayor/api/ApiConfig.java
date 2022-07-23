package me.bemayor.api;

import io.github.bakedlibs.dough.config.Config;
import org.bukkit.plugin.java.JavaPlugin;

public class ApiConfig extends Config {

    public ApiConfig(JavaPlugin plugin) { super(plugin, ApiManagement.APICONFIG_YML); }

}
