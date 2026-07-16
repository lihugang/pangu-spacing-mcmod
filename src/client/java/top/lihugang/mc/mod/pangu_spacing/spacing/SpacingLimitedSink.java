/*
 * SPDX-License-Identifier: GPL-3.0-only
 */

package top.lihugang.mc.mod.pangu_spacing.spacing;

import net.minecraft.client.StringSplitter;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSink;

public final class SpacingLimitedSink implements FormattedCharSink {
	private final StringSplitter.WidthProvider widthProvider;
	private final PanguSpacingProcessor<Style> processor;
	private final boolean backwards;
	private float remainingWidth;
	private int position;

	public SpacingLimitedSink(StringSplitter.WidthProvider widthProvider, float maxWidth, int initialPosition,
			boolean backwards) {
		this.widthProvider = widthProvider;
		this.remainingWidth = maxWidth;
		this.position = initialPosition;
		this.backwards = backwards;
		this.processor = new PanguSpacingProcessor<>(new PanguSpacingProcessor.Target<>() {
			@Override
			public boolean advanceGap(int boundaryIndex, Style boundaryStyle, Style westernStyle) {
				return tryAdvance(GapWidths.forProvider(SpacingLimitedSink.this.widthProvider, westernStyle));
			}

			@Override
			public boolean accept(int index, Style style, int codePoint) {
				if (!tryAdvance(SpacingLimitedSink.this.widthProvider.getWidth(codePoint, style))) {
					return false;
				}
				position = SpacingLimitedSink.this.backwards
						? index
						: index + Character.charCount(codePoint);
				return true;
			}
		}, backwards);
	}

	@Override
	public boolean accept(int index, Style style, int codePoint) {
		return processor.accept(index, style, codePoint);
	}

	public void finish() {
		processor.finish();
	}

	public int position() {
		return position;
	}

	private boolean tryAdvance(float width) {
		if (remainingWidth < width) {
			return false;
		}
		remainingWidth -= width;
		return true;
	}
}
