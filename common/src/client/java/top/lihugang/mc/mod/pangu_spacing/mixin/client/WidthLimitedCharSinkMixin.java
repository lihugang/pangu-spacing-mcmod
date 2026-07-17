/*
 * SPDX-License-Identifier: GPL-3.0-only
 */

package top.lihugang.mc.mod.pangu_spacing.mixin.client;

import net.minecraft.client.StringSplitter;
import net.minecraft.network.chat.Style;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import top.lihugang.mc.mod.pangu_spacing.spacing.GapAdvancer;
import top.lihugang.mc.mod.pangu_spacing.spacing.GapWidths;
import top.lihugang.mc.mod.pangu_spacing.spacing.SpacingFormattedCharSink;
import top.lihugang.mc.mod.pangu_spacing.spacing.SpacingSinkOwner;

@Mixin(targets = "net.minecraft.client.StringSplitter$WidthLimitedCharSink")
abstract class WidthLimitedCharSinkMixin implements GapAdvancer, SpacingSinkOwner {
	@Shadow
	private float maxWidth;

	@Shadow
	@Final
	private StringSplitter this$0;

	@Unique
	private SpacingFormattedCharSink panguSpacing$spacingSink;

	@Override
	public boolean panguSpacing$advanceGap(int boundaryIndex, Style boundaryStyle, Style westernStyle) {
		StringSplitter.WidthProvider provider = ((StringSplitterAccessor) this$0).panguSpacing$getWidthProvider();
		float gap = GapWidths.forProvider(provider, westernStyle);
		if (maxWidth < gap) {
			return false;
		}
		maxWidth -= gap;
		return true;
	}

	@Override
	public SpacingFormattedCharSink panguSpacing$spacingSink() {
		if (panguSpacing$spacingSink == null) {
			panguSpacing$spacingSink = new SpacingFormattedCharSink(
					(net.minecraft.util.FormattedCharSink) (Object) this, this, false);
		}
		return panguSpacing$spacingSink;
	}
}
