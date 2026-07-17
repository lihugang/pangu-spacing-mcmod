/*
 * SPDX-License-Identifier: GPL-3.0-only
 */

package top.lihugang.mc.mod.pangu_spacing.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import top.lihugang.mc.mod.pangu_spacing.PanguSpacingConfig;
import top.lihugang.mc.mod.pangu_spacing.PanguSpacingConstants;

public final class PanguSpacingFabric implements ClientModInitializer {
	public static final String MOD_ID = PanguSpacingConstants.MOD_ID;
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitializeClient() {
		PanguSpacingConfig.initialize(FabricLoader.getInstance().getConfigDir());
		PanguSpacingCommands.register();
		LOGGER.info("Pangu Spacing initialized.");
	}
}
