package de.fellinara.healplugin;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HealCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cThis command can only be used by players.");
            return true;
        }

        if (!player.hasPermission("healplugin.heal")) {
            player.sendMessage("§cYou don't have permission to use this command. (OP required)");
            return true;
        }

        AttributeInstance maxHealthAttribute = player.getAttribute(Attribute.MAX_HEALTH);
        if (maxHealthAttribute == null) {
            player.sendMessage("§cCould not determine your max health.");
            return true;
        }

        double maxHealth = maxHealthAttribute.getValue();
        player.setHealth(maxHealth);
        player.setFoodLevel(20);
        player.setSaturation(20f);

        player.sendMessage("§aYou have been healed!");
        return true;
    }
}
