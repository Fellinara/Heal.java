package de.fillinara.healplugin;

import org.bukkit.plugin.java.JavaPlugin;

public class HealPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new FillinaraListener(this), this);
        getLogger().info("HealPlugin enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("HealPlugin disabled.");
    }
}
