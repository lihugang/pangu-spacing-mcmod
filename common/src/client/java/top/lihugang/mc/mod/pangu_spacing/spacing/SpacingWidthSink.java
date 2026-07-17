/*
 * SPDX-License-Identifier: GPL-3.0-only
 */

package top.lihugang.mc.mod.pangu_spacing.spacing;

import net.minecraft.client.StringSplitter;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSink;
import top.lihugang.mc.mod.pangu_spacing.PanguSpacingConfig;

public final class SpacingWidthSink implements FormattedCharSink {
	private final StringSplitter.WidthProvider widthProvider;
	private final PanguSpacingProcessor<Style> processor;
	private float width;

	public SpacingWidthSink(StringSplitter.WidthProvider widthProvider, boolean backwards) {
		this.widthProvider = widthProvider;
		this.processor = new PanguSpacingProcessor<>(new PanguSpacingProcessor.Target<>() {
			@Override
			public boolean advanceGap(int boundaryIndex, Style boundaryStyle, Style westernStyle) {
				width += GapWidths.forProvider(SpacingWidthSink.this.widthProvider, westernStyle);
				return true;
			}

			@Override
			public boolean accept(int index, Style style, int codePoint) {
				width += SpacingWidthSink.this.widthProvider.getWidth(codePoint, style);
				return true;
			}
		}, backwards, PanguSpacingConfig.isEnabled());
	}

	@Override
	public boolean accept(int index, Style style, int codePoint) {
		return processor.accept(index, style, codePoint);
	}

	public float finishAndGetWidth() {
		processor.finish();
		return width;
	}
}
