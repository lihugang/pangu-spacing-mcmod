/*
 * SPDX-License-Identifier: GPL-3.0-only
 */

package top.lihugang.mc.mod.pangu_spacing.spacing;

import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSink;
import top.lihugang.mc.mod.pangu_spacing.PanguSpacingConfig;

public final class SpacingFormattedCharSink implements FormattedCharSink {
	private final FormattedCharSink delegate;
	private final PanguSpacingProcessor<Style> processor;

	public SpacingFormattedCharSink(FormattedCharSink delegate, GapAdvancer gapAdvancer, boolean backwards) {
		this.delegate = delegate;
		this.processor = new PanguSpacingProcessor<>(new PanguSpacingProcessor.Target<>() {
			@Override
			public boolean advanceGap(int boundaryIndex, Style boundaryStyle, Style westernStyle) {
				return gapAdvancer.panguSpacing$advanceGap(boundaryIndex, boundaryStyle, westernStyle);
			}

			@Override
			public boolean accept(int index, Style style, int codePoint) {
				return SpacingFormattedCharSink.this.delegate.accept(index, style, codePoint);
			}
		}, backwards, PanguSpacingConfig.isEnabled());
	}

	@Override
	public boolean accept(int index, Style style, int codePoint) {
		return processor.accept(index, style, codePoint);
	}

	public boolean finishSegment() {
		return processor.finishSegment();
	}

	public boolean finish() {
		return processor.finish();
	}
}
