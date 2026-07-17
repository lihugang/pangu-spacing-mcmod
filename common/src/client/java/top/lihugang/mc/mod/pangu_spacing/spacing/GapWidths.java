/*
 * SPDX-License-Identifier: GPL-3.0-only
 */

package top.lihugang.mc.mod.pangu_spacing.spacing;

import net.minecraft.client.StringSplitter;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Style;
import top.lihugang.mc.mod.pangu_spacing.mixin.client.FontAccessor;

public final class GapWidths {
	private static final float VANILLA_LINE_HEIGHT = 9.0F;

	private GapWidths() {
	}

	public static float forFont(Font font, Style style) {
		float advance = ((FontAccessor) font).panguSpacing$getGlyph(' ', style).info().getAdvance(style.isBold());
		return PanguGapWidth.clamp(advance, font.lineHeight);
	}

	public static float forProvider(StringSplitter.WidthProvider provider, Style style) {
		return PanguGapWidth.clamp(provider.getWidth(' ', style), VANILLA_LINE_HEIGHT);
	}
}
