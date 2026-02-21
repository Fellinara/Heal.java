package de.heal;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Heal – Handles the /heal command.
 *
 * Usage:
 *   /heal          – Heals the command sender (must be a player).
 *   /heal <player> – Heals the specified player (OP only).
 *
 * Both usages require the sender to have OP status.
 */
public class Heal implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Only OPs (or players with the heal.use permission) may use this command
        if (!sender.hasPermission("heal.use")) {
            sender.sendMessage("§cYou need to be an OP to use /heal.");
            return true;
        }

        if (args.length == 0) {
            // Sender must be a player when no target is given
            if (!(sender instanceof Player player)) {
                sender.sendMessage("§cUsage: /heal <player>");
                return true;
            }
            healPlayer(player, sender);
        } else {
            // Heal the specified player
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage("§cPlayer §e" + args[0] + " §cnot found or not online.");
                return true;
            }
            healPlayer(target, sender);
        }

        return true;
    }

    /**
     * Restores the given player to full health and sends feedback messages.
     *
     * @param target the player to heal
     * @param sender the command sender (for feedback)
     */
    private void healPlayer(Player target, CommandSender sender) {
        target.setHealth(target.getMaxHealth());
        target.setFoodLevel(20);
        target.setSaturation(target.getFoodLevel());

        if (target.equals(sender)) {
            target.sendMessage("§aYou have been healed!");
        } else {
            target.sendMessage("§aYou have been healed by §e" + sender.getName() + "§a!");
            sender.sendMessage("§aYou healed §e" + target.getName() + "§a!");
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!sender.hasPermission("heal.use")) {
            return new ArrayList<>();
        }
        if (args.length == 1) {
            List<String> names = new ArrayList<>();
            String prefix = args[0].toLowerCase();
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.getName().toLowerCase().startsWith(prefix)) {
                    names.add(player.getName());
                }
            }
            return names;
        }
        return new ArrayList<>();
    }
}
