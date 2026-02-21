package de.fillinara.healplugin;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class FillinaraListener implements Listener {

    private static final String TARGET = "Fillinara";

    private final HealPlugin plugin;
    private final Set<UUID> healCooldown = new HashSet<>();
    private final NamespacedKey bonusDurabilityKey;

    public FillinaraListener(HealPlugin plugin) {
        this.plugin = plugin;
        this.bonusDurabilityKey = new NamespacedKey(plugin, "bonus_durability");
    }

    // ⚔️ Feature 1 & 🛡️ Feature 5: Extra damage dealt + shield cooldown reduction
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        // Feature 1: Fillinara deals +1 HP (0.5 hearts) more damage
        if (event.getDamager() instanceof Player damager
                && damager.getName().equalsIgnoreCase(TARGET)) {
            event.setDamage(event.getDamage() + 1.0);
        }

        // Feature 5: Reduce shield cooldown from 100 ticks (5s) to 80 ticks (4s) after axe hit
        if (event.getEntity() instanceof Player victim
                && victim.getName().equalsIgnoreCase(TARGET)
                && event.getDamager() instanceof Player attacker
                && isAxe(attacker.getInventory().getItemInMainHand().getType())) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (victim.isOnline() && victim.getCooldown(Material.SHIELD) > 80) {
                        victim.setCooldown(Material.SHIELD, 80);
                    }
                }
            }.runTaskLater(plugin, 1L);
        }
    }

    // 🛡️ Feature 2 & 💚 Feature 3: Reduced damage received + auto-heal
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)
                || !player.getName().equalsIgnoreCase(TARGET)) {
            return;
        }

        // Feature 2: Subtract 1 HP (0.5 hearts) from incoming base damage
        double baseDamage = event.getDamage();
        if (baseDamage <= 1.0) {
            event.setCancelled(true);
            return;
        }
        event.setDamage(baseDamage - 1.0);

        // Feature 3: Auto-heal to 4 hearts when health drops to 1 heart or below
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.isOnline() || player.isDead()) return;
                if (player.getHealth() <= 2.0
                        && !healCooldown.contains(player.getUniqueId())) {
                    player.setHealth(8.0); // 4 hearts = 8 HP
                    healCooldown.add(player.getUniqueId());
                    // Remove cooldown after 3 seconds (60 ticks)
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            healCooldown.remove(player.getUniqueId());
                        }
                    }.runTaskLater(plugin, 60L);
                }
            }
        }.runTaskLater(plugin, 1L);
    }

    // 👊 Feature 4: Faster attack speed on join
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (player.getName().equalsIgnoreCase(TARGET)) {
            applyAttackSpeed(player);
            initArmorBonusDurability(player);
        }
    }

    // 👊 Feature 4: Faster attack speed on respawn
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        if (player.getName().equalsIgnoreCase(TARGET)) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    applyAttackSpeed(player);
                }
            }.runTaskLater(plugin, 1L);
        }
    }

    // 🪖 Feature 6: Armor bonus durability
    @EventHandler(ignoreCancelled = true)
    public void onPlayerItemDamage(PlayerItemDamageEvent event) {
        Player player = event.getPlayer();
        if (!player.getName().equalsIgnoreCase(TARGET)) return;

        ItemStack item = event.getItem();
        if (!isArmorItem(item.getType())) return;

        ItemMeta meta = item.getItemMeta();
        if (!(meta instanceof Damageable)) return;

        int bonusDurability = meta.getPersistentDataContainer()
                .getOrDefault(bonusDurabilityKey, PersistentDataType.INTEGER, 0);

        if (bonusDurability <= 0) return;

        int dmg = event.getDamage();
        if (dmg <= bonusDurability) {
            // Absorb all damage with bonus pool
            meta.getPersistentDataContainer()
                    .set(bonusDurabilityKey, PersistentDataType.INTEGER, bonusDurability - dmg);
            item.setItemMeta(meta);
            event.setCancelled(true);
        } else {
            // Exhaust bonus pool and apply remaining damage
            meta.getPersistentDataContainer()
                    .set(bonusDurabilityKey, PersistentDataType.INTEGER, 0);
            item.setItemMeta(meta);
            event.setDamage(dmg - bonusDurability);
        }
    }

    private void applyAttackSpeed(Player player) {
        AttributeInstance attr = player.getAttribute(Attribute.GENERIC_ATTACK_SPEED);
        if (attr == null) return;
        UUID modifierUUID = UUID.nameUUIDFromBytes("fillinara_attack_speed".getBytes());
        // Remove any existing modifier to avoid stacking
        attr.getModifiers().stream()
                .filter(m -> m.getUniqueId().equals(modifierUUID))
                .forEach(attr::removeModifier);
        attr.addModifier(new AttributeModifier(
                modifierUUID,
                "fillinara_attack_speed",
                2.0,
                AttributeModifier.Operation.ADD_NUMBER
        ));
    }

    private void initArmorBonusDurability(Player player) {
        for (ItemStack item : player.getInventory().getArmorContents()) {
            if (item != null && isArmorItem(item.getType())) {
                ItemMeta meta = item.getItemMeta();
                if (meta == null) continue;
                int existing = meta.getPersistentDataContainer()
                        .getOrDefault(bonusDurabilityKey, PersistentDataType.INTEGER, 0);
                if (existing < 100) {
                    meta.getPersistentDataContainer()
                            .set(bonusDurabilityKey, PersistentDataType.INTEGER, 100);
                    item.setItemMeta(meta);
                }
            }
        }
    }

    private boolean isAxe(Material material) {
        return material == Material.WOODEN_AXE
                || material == Material.STONE_AXE
                || material == Material.IRON_AXE
                || material == Material.GOLDEN_AXE
                || material == Material.DIAMOND_AXE
                || material == Material.NETHERITE_AXE;
    }

    private boolean isArmorItem(Material material) {
        if (material == Material.TURTLE_HELMET) return true;
        String name = material.name();
        return name.endsWith("_HELMET") || name.endsWith("_CHESTPLATE")
                || name.endsWith("_LEGGINGS") || name.endsWith("_BOOTS");
    }
}
