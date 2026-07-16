/*
 * SPDX-License-Identifier: GPL-3.0-only
 */

package top.lihugang.mc.mod.pangu_spacing.mixin.client;

import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Style;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import top.lihugang.mc.mod.pangu_spacing.spacing.GapAdvancer;
import top.lihugang.mc.mod.pangu_spacing.spacing.GapWidths;

@Mixin(targets = "net.minecraft.client.gui.Font$PreparedTextBuilder")
abstract class FontPreparedTextBuilderMixin implements GapAdvancer {
	@Shadow
	private float x;

	@Shadow
	@Final
	private Font this$0;

	@Override
	public boolean panguSpacing$advanceGap(int boundaryIndex, Style boundaryStyle, Style westernStyle) {
		x += GapWidths.forFont(this$0, westernStyle);
		return true;
	}
}
