package com.example.trialspawnermace;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TrialSpawnerMaceMod implements ModInitializer {

	public static final String MOD_ID = "trial-spawner-mace";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// Server-side initialization – nothing needed for this client-only feature
	}
}
