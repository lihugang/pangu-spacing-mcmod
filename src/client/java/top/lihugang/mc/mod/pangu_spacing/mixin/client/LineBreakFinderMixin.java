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

@Mixin(targets = "net.minecraft.client.StringSplitter$LineBreakFinder")
abstract class LineBreakFinderMixin implements GapAdvancer, SpacingSinkOwner {
	@Shadow
	@Final
	private StringSplitter this$0;

	@Shadow
	@Final
	private float maxWidth;

	@Shadow
	private float width;

	@Shadow
	private boolean hadNonZeroWidthChar;

	@Shadow
	private int lastSpace;

	@Shadow
	private Style lastSpaceStyle;

	@Shadow
	private int lineBreak;

	@Shadow
	private Style lineBreakStyle;

	@Shadow
	private int offset;

	@Unique
	private SpacingFormattedCharSink panguSpacing$spacingSink;

	@Override
	public boolean panguSpacing$advanceGap(int boundaryIndex, Style boundaryStyle, Style westernStyle) {
		StringSplitter.WidthProvider provider = ((StringSplitterAccessor) this$0).panguSpacing$getWidthProvider();
		float gap = GapWidths.forProvider(provider, westernStyle);
		width += gap;
		if (hadNonZeroWidthChar && width > maxWidth) {
			if (lastSpace != -1) {
				lineBreak = lastSpace;
				lineBreakStyle = lastSpaceStyle;
			} else {
				lineBreak = boundaryIndex + offset;
				lineBreakStyle = boundaryStyle;
			}
			return false;
		}
		hadNonZeroWidthChar |= gap != 0.0F;
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
