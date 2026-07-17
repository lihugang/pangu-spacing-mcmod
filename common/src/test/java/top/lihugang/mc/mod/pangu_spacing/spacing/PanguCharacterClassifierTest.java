/*
 * SPDX-License-Identifier: GPL-3.0-only
 */

package top.lihugang.mc.mod.pangu_spacing.spacing;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class PanguCharacterClassifierTest {
	@Test
	void classifiesPanguCjkRangesAndSupplementaryHan() {
		assertEquals(PanguCharacterClassifier.CJK, PanguCharacterClassifier.classify('中', false));
		assertEquals(PanguCharacterClassifier.CJK, PanguCharacterClassifier.classify('あ', false));
		assertEquals(PanguCharacterClassifier.CJK, PanguCharacterClassifier.classify('ア', false));
		assertEquals(PanguCharacterClassifier.CJK, PanguCharacterClassifier.classify('ㄅ', false));
		assertEquals(PanguCharacterClassifier.CJK, PanguCharacterClassifier.classify(0x20000, false));
		assertEquals(PanguCharacterClassifier.CJK, PanguCharacterClassifier.classify(0x31350, false));
	}

	@Test
	void excludesHangulAndMiddleDots() {
		assertEquals(PanguCharacterClassifier.OTHER, PanguCharacterClassifier.classify('한', false));
		assertEquals(PanguCharacterClassifier.OTHER, PanguCharacterClassifier.classify(0x00B7, false));
		assertEquals(PanguCharacterClassifier.OTHER, PanguCharacterClassifier.classify(0x30FB, false));
	}

	@Test
	void reversesDirectionalBrackets() {
		assertEquals(PanguCharacterClassifier.OPEN, PanguCharacterClassifier.classify('(', false));
		assertEquals(PanguCharacterClassifier.CLOSE, PanguCharacterClassifier.classify('(', true));
		assertEquals(PanguCharacterClassifier.CLOSE, PanguCharacterClassifier.classify(')', false));
		assertEquals(PanguCharacterClassifier.OPEN, PanguCharacterClassifier.classify(')', true));
	}
}
