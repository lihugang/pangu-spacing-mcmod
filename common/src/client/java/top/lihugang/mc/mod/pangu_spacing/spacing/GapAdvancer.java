/*
 * SPDX-License-Identifier: GPL-3.0-only
 */

package top.lihugang.mc.mod.pangu_spacing.spacing;

import net.minecraft.network.chat.Style;

public interface GapAdvancer {
	boolean panguSpacing$advanceGap(int boundaryIndex, Style boundaryStyle, Style westernStyle);
}
