/*
 * SPDX-License-Identifier: GPL-3.0-only
 */

package top.lihugang.mc.mod.pangu_spacing.spacing;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class PanguGapWidthTest {
	@Test
	void clampsSpaceAdvanceToQuarterAndThirdEm() {
		assertEquals(2.25F, PanguGapWidth.clamp(0.0F, 9.0F));
		assertEquals(2.5F, PanguGapWidth.clamp(2.5F, 9.0F));
		assertEquals(3.0F, PanguGapWidth.clamp(4.0F, 9.0F));
		assertEquals(3.0F, PanguGapWidth.clamp(20.0F, 9.0F));
	}
}
