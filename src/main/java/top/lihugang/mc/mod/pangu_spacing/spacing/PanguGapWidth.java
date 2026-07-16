/*
 * SPDX-License-Identifier: GPL-3.0-only
 */

package top.lihugang.mc.mod.pangu_spacing.spacing;

public final class PanguGapWidth {
	private PanguGapWidth() {
	}

	public static float clamp(float spaceAdvance, float lineHeight) {
		float minimum = lineHeight * 0.25F;
		float maximum = lineHeight / 3.0F;
		return Math.max(minimum, Math.min(spaceAdvance, maximum));
	}
}
