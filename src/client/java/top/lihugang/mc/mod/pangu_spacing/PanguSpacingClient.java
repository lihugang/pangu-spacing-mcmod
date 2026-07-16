/*
 * SPDX-License-Identifier: GPL-3.0-only
 */

package top.lihugang.mc.mod.pangu_spacing;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class PanguSpacingClient implements ClientModInitializer {
	public static final String MOD_ID = "pangu_spacing";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitializeClient() {
		PanguSpacingConfig.initialize(FabricLoader.getInstance().getConfigDir());
		PanguSpacingCommands.register();
		LOGGER.info("Pangu Spacing initialized.");
	}
}
