/*
 * SPDX-License-Identifier: GPL-3.0-only
 */

package top.lihugang.mc.mod.pangu_spacing.neoforge;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLPaths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.lihugang.mc.mod.pangu_spacing.PanguSpacingConstants;
import top.lihugang.mc.mod.pangu_spacing.PanguSpacingConfig;

@Mod(value = PanguSpacingConstants.MOD_ID, dist = Dist.CLIENT)
public final class PanguSpacingNeoForge {
	private static final Logger LOGGER = LoggerFactory.getLogger(PanguSpacingConstants.MOD_ID);

	public PanguSpacingNeoForge() {
		PanguSpacingConfig.initialize(FMLPaths.CONFIGDIR.get());
		PanguSpacingNeoForgeCommands.register();
		LOGGER.info("Pangu Spacing initialized.");
	}
}
