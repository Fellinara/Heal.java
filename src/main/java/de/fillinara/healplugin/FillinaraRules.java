package de.fillinara.healplugin;

/**
 * Pure business-logic constants and calculations for all Fillinara perks.
 * This class has no Bukkit dependency and can be unit-tested without a server.
 */
public final class FillinaraRules {

    private FillinaraRules() {}

    /** Target player name (case-insensitive comparison is done by callers). */
    public static final String TARGET_NAME = "Fillinara";

    // ⚔️ Feature 1 – extra damage dealt
    /** HP added to every outgoing attack (0.5 hearts = 1 HP). */
    public static final double DAMAGE_BONUS = 1.0;

    // 🛡️ Feature 2 – damage reduction
    /** HP subtracted from every incoming hit (0.5 hearts = 1 HP). */
    public static final double DAMAGE_REDUCTION = 1.0;

    // 💚 Feature 3 – auto-heal
    /** HP threshold (≤) that triggers the auto-heal (1 heart = 2 HP). */
    public static final double AUTO_HEAL_THRESHOLD = 2.0;
    /** HP restored when auto-heal fires (4 hearts = 8 HP). */
    public static final double AUTO_HEAL_AMOUNT = 8.0;
    /** Ticks before auto-heal may fire again (3 seconds = 60 ticks). */
    public static final int AUTO_HEAL_COOLDOWN_TICKS = 60;

    // 👊 Feature 4 – attack speed
    /** Flat bonus added to GENERIC_ATTACK_SPEED attribute. */
    public static final double ATTACK_SPEED_BONUS = 2.0;

    // 🛡️ Feature 5 – reduced shield cooldown
    /** Ticks the shield cooldown is clamped to after an axe hit (4 s = 80 ticks). */
    public static final int SHIELD_COOLDOWN_TICKS = 80;

    // 🪖 Feature 6 – bonus armor durability
    /** Invisible extra durability each armor piece starts with. */
    public static final int BONUS_DURABILITY = 100;

    // -------------------------------------------------------------------------
    // Stateless helpers (used both by the listener and by tests)
    // -------------------------------------------------------------------------

    /**
     * Returns the damage Fillinara deals after the outgoing bonus is applied.
     */
    public static double applyDamageBonus(double baseDamage) {
        return baseDamage + DAMAGE_BONUS;
    }

    /**
     * Returns the effective incoming damage after reduction.
     * Returns {@code -1.0} to signal that the hit should be fully cancelled.
     */
    public static double applyDamageReduction(double baseDamage) {
        if (baseDamage <= DAMAGE_REDUCTION) {
            return -1.0;
        }
        return baseDamage - DAMAGE_REDUCTION;
    }

    /**
     * Returns {@code true} when Fillinara's current health is low enough to
     * trigger the auto-heal.
     */
    public static boolean shouldAutoHeal(double currentHealth) {
        return currentHealth <= AUTO_HEAL_THRESHOLD;
    }

    /**
     * Absorbs item damage using the invisible bonus-durability pool.
     *
     * @param bonusPool current invisible durability pool
     * @param damage    incoming item damage
     * @return {@code int[]{newBonusPool, realDamage}} where {@code realDamage == 0}
     *         means the real durability should not be touched (event cancelled).
     */
    public static int[] absorbBonusDurability(int bonusPool, int damage) {
        if (bonusPool <= 0) {
            return new int[]{0, damage};
        }
        if (damage <= bonusPool) {
            return new int[]{bonusPool - damage, 0};
        }
        return new int[]{0, damage - bonusPool};
    }
}
