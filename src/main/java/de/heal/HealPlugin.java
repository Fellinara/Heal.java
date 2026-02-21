package de.heal;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * HealPlugin – Main entry point for the Heal Paper plugin.
 * Registers the /heal command on enable.
 */
public final class HealPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("HealPlugin enabled! Use /heal to restore your health (OP required).");
        Heal healCommand = new Heal();
        var command = getCommand("heal");
        if (command != null) {
            command.setExecutor(healCommand);
            command.setTabCompleter(healCommand);
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("HealPlugin disabled.");
    }
}
