package com.example.trialspawnermace;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.List;

/**
 * Client-side initializer for the Trial Spawner Mace Helper mod.
 *
 * <p>Automatically moves the player toward any {@link Items#HEAVY_CORE} item
 * entity that spawns within {@value #SEARCH_RADIUS} blocks, so the item is
 * picked up without manual effort. Works entirely client-side – the server
 * does not need to have the mod installed.</p>
 *
 * <p>The feature can be toggled at runtime with the keybind
 * {@code KEY_TOGGLE} (default: {@code H}).</p>
 */
@Environment(EnvType.CLIENT)
public class TrialSpawnerMaceModClient implements ClientModInitializer {

	private static final String MOD_ID = "trial-spawner-mace";
	private static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	/** Radius (in blocks) to search for Heavy Core item entities. */
	private static final double SEARCH_RADIUS = 16.0;

	/**
	 * Squared distance at which we consider the player "close enough" to
	 * trigger vanilla item pickup (≈ 1 block).
	 */
	private static final double PICKUP_DIST_SQ = 1.0 * 1.0;

	/**
	 * Maximum horizontal speed when moving toward the item (blocks/tick).
	 * Matches vanilla walking speed (~0.13 b/t) so the server never flags
	 * the movement as illegal – preventing ReadTimeoutException disconnects.
	 */
	private static final double WALK_SPEED = 0.13;

	/**
	 * How many ticks to wait between velocity nudges.
	 * 4 ticks = 5 Hz – reduces position-packet rate without losing responsiveness.
	 */
	private static final int NUDGE_INTERVAL = 4;

	private static final String KEYBIND_CATEGORY = "key.categories.trial-spawner-mace";
	private static final String KEYBIND_TOGGLE   = "key.trial-spawner-mace.toggle";

	private boolean enabled = true;
	/** Bounded tick counter: 0 … NUDGE_INTERVAL-1, wraps without overflow. */
	private int tickCount = 0;

	private KeyBinding toggleKey;

	@Override
	public void onInitializeClient() {
		toggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				KEYBIND_TOGGLE,
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_H,
				KEYBIND_CATEGORY
		));

		ClientTickEvents.END_CLIENT_TICK.register(this::onTick);

		LOGGER.info("Trial Spawner Mace Helper initialized – press H to toggle.");
	}

	// --------------------------------------------------------------------- //

	private void onTick(MinecraftClient client) {
		// Handle toggle keybind
		while (toggleKey.wasPressed()) {
			enabled = !enabled;
			if (client.player != null) {
				client.player.sendMessage(
						Text.literal("[HeavyCore] Auto-pickup " + (enabled ? "§aenabled" : "§cdisabled")),
						true // action bar
				);
			}
		}

		if (!enabled) return;
		if (client.player == null || client.world == null) return;
		if (client.player.isDead()) return;
		// Do not interfere while a screen (inventory, chat, …) is open
		if (client.currentScreen != null) return;

		// Throttle: only nudge every NUDGE_INTERVAL ticks to keep position-packet
		// rate low and avoid server-side movement detection.
		tickCount = (tickCount + 1) % NUDGE_INTERVAL;
		if (tickCount != 0) return;

		// Find all Heavy Core item entities within the search radius
		List<ItemEntity> candidates = client.world.getEntitiesByClass(
				ItemEntity.class,
				client.player.getBoundingBox().expand(SEARCH_RADIUS),
				e -> !e.isRemoved() && e.getStack().isOf(Items.HEAVY_CORE)
		);

		if (candidates.isEmpty()) return;

		// Pick the nearest one
		Vec3d playerPos = client.player.getPos();
		ItemEntity target = candidates.stream()
				.min(Comparator.comparingDouble(e -> e.squaredDistanceTo(playerPos.x, playerPos.y, playerPos.z)))
				.orElse(null);

		if (target == null) return;

		Vec3d targetPos = target.getPos();
		double dx = targetPos.x - playerPos.x;
		double dy = targetPos.y - playerPos.y;
		double dz = targetPos.z - playerPos.z;
		double distSq = dx * dx + dy * dy + dz * dz;

		if (distSq <= PICKUP_DIST_SQ) {
			// Already within pickup range – vanilla will handle the rest
			return;
		}

		double dist = Math.sqrt(distSq);
		// Slow down when close to avoid overshooting; cap at walking speed so the
		// server never flags the movement as a speed hack (prevents ReadTimeoutException).
		double speed = Math.min(WALK_SPEED, dist * 0.2);

		// Set horizontal velocity toward the item; preserve vertical velocity so
		// gravity and jumping still work naturally. Using setVelocity (not addVelocity)
		// prevents velocity from accumulating across ticks.
		Vec3d currentVel = client.player.getVelocity();
		client.player.setVelocity(
				dx / dist * speed,
				currentVel.y,
				dz / dist * speed
		);
	}
}
