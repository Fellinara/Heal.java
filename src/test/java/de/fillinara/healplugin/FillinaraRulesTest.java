package de.fillinara.healplugin;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for FillinaraRules – validates all six plugin features
 * without requiring a running Minecraft server.
 */
class FillinaraRulesTest {

    // ⚔️ Feature 1 – extra damage dealt

    @Test
    void damageBonus_addsOneHPToNormalHit() {
        assertEquals(6.0, FillinaraRules.applyDamageBonus(5.0));
    }

    @Test
    void damageBonus_worksOnZeroBaseDamage() {
        assertEquals(FillinaraRules.DAMAGE_BONUS, FillinaraRules.applyDamageBonus(0.0));
    }

    // 🛡️ Feature 2 – damage reduction

    @Test
    void damageReduction_subtractsOneHPFromNormalHit() {
        assertEquals(4.0, FillinaraRules.applyDamageReduction(5.0));
    }

    @Test
    void damageReduction_cancelsHitOfExactlyOneHP() {
        assertEquals(-1.0, FillinaraRules.applyDamageReduction(1.0));
    }

    @Test
    void damageReduction_cancelsHitBelowOneHP() {
        assertEquals(-1.0, FillinaraRules.applyDamageReduction(0.5));
    }

    @Test
    void damageReduction_reducesEdgeCaseJustAboveOneHP() {
        assertEquals(0.1, FillinaraRules.applyDamageReduction(1.1), 1e-9);
    }

    // 💚 Feature 3 – auto-heal

    @Test
    void autoHeal_triggersAtExactlyOneHeart() {
        assertTrue(FillinaraRules.shouldAutoHeal(2.0));
    }

    @Test
    void autoHeal_triggersWellBelowOneHeart() {
        assertTrue(FillinaraRules.shouldAutoHeal(0.5));
    }

    @Test
    void autoHeal_doesNotTriggerAboveThreshold() {
        assertFalse(FillinaraRules.shouldAutoHeal(2.1));
        assertFalse(FillinaraRules.shouldAutoHeal(8.0));
    }

    @Test
    void autoHeal_amountIsFourHearts() {
        assertEquals(8.0, FillinaraRules.AUTO_HEAL_AMOUNT);
    }

    @Test
    void autoHeal_cooldownIsThreeSeconds() {
        assertEquals(60, FillinaraRules.AUTO_HEAL_COOLDOWN_TICKS);
    }

    // 👊 Feature 4 – attack speed (constant check)

    @Test
    void attackSpeedBonus_isPositive() {
        assertTrue(FillinaraRules.ATTACK_SPEED_BONUS > 0);
    }

    // 🛡️ Feature 5 – shield cooldown

    @Test
    void shieldCooldown_isLessThanVanilla() {
        // vanilla axe cooldown = 100 ticks (5 s)
        assertTrue(FillinaraRules.SHIELD_COOLDOWN_TICKS < 100);
        assertEquals(80, FillinaraRules.SHIELD_COOLDOWN_TICKS);
    }

    // 🪖 Feature 6 – bonus armor durability

    @Test
    void bonusDurability_absorbsSmallDamageCompletely() {
        int[] result = FillinaraRules.absorbBonusDurability(100, 5);
        assertArrayEquals(new int[]{95, 0}, result);
    }

    @Test
    void bonusDurability_absorbsExactDamage() {
        int[] result = FillinaraRules.absorbBonusDurability(5, 5);
        assertArrayEquals(new int[]{0, 0}, result);
    }

    @Test
    void bonusDurability_spillsOverflowToRealDurability() {
        int[] result = FillinaraRules.absorbBonusDurability(3, 8);
        assertArrayEquals(new int[]{0, 5}, result);
    }

    @Test
    void bonusDurability_emptyPoolPassesThroughAllDamage() {
        int[] result = FillinaraRules.absorbBonusDurability(0, 7);
        assertArrayEquals(new int[]{0, 7}, result);
    }

    @Test
    void bonusDurability_initialPoolIs100() {
        assertEquals(100, FillinaraRules.BONUS_DURABILITY);
    }
}
