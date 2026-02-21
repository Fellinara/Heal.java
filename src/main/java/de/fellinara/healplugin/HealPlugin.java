package de.fellinara.healplugin;

import org.bukkit.plugin.java.JavaPlugin;

public class HealPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        HealCommand healCommand = new HealCommand();
        if (getCommand("heal") != null) {
            getCommand("heal").setExecutor(healCommand);
        }
        getLogger().info("HealPlugin enabled! Use /heal to restore health (OP required).");
    }

    @Override
    public void onDisable() {
        getLogger().info("HealPlugin disabled.");
    }
}
