/*
 * SPDX-License-Identifier: GPL-3.0-only
 */

package top.lihugang.mc.mod.pangu_spacing.spacing;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class PanguSpacingProcessorTest {
	@Test
	void spacesDirectCjkWesternBoundaries() {
		assertVisual("中文·Minecraft", "中文Minecraft");
		assertVisual("Minecraft·中文", "Minecraft中文");
		assertVisual("版本·v2", "版本v2");
		assertVisual("中文 Minecraft", "中文 Minecraft");
		assertVisual("中文\tMinecraft", "中文\tMinecraft");
	}

	@Test
	void spacesBracketsAndQuotesOnlyOnTheirOuterEdges() {
		assertVisual("前面·(中文·123)·后面", "前面(中文123)后面");
		assertVisual("前面·\"中文·123\"·后面", "前面\"中文123\"后面");
		assertVisual("it's·中文", "it's中文");
	}

	@Test
	void spacesOperatorsOnBothSidesOnlyWhenCjkIsAdjacent() {
		assertVisual("中文·+·English", "中文+English");
		assertVisual("English·+·中文", "English+中文");
		assertVisual("中文·+·汉字", "中文+汉字");
		assertVisual("A+B", "A+B");
	}

	@Test
	void keepsPunctuationAttachedToTheLeftToken() {
		assertVisual("中文,·English", "中文,English");
		assertVisual("English,·中文", "English,中文");
		assertVisual("中文!·汉字", "中文!汉字");
		assertVisual("Hello,world", "Hello,world");
		assertVisual("版本·1.21", "版本1.21");
		assertVisual("1.21·版本", "1.21版本");
	}

	@Test
	void treatsPrefixesAsPartOfTheWesternToken() {
		assertVisual("中文·#topic", "中文#topic");
		assertVisual("topic#·中文", "topic#中文");
	}

	@Test
	void handlesSupplementaryHanAndCombiningMarks() {
		assertVisual("𠀀·A", "𠀀A");
		assertVisual("Á·中", "Á中");
		assertVisual("한A", "한A");
	}

	@Test
	void choosesTheWesternSideStyle() {
		RecordingTarget target = new RecordingTarget();
		PanguSpacingProcessor<Integer> processor = new PanguSpacingProcessor<>(target, false);
		processor.accept(0, 10, '中');
		processor.accept(1, 20, 'A');
		processor.accept(2, 30, '文');
		processor.finish();

		assertEquals(List.of(20, 20), target.gapStyles);
	}

	@Test
	void reverseTraversalCountsTheSameGaps() {
		String text = "前面(中文123)+English,后面";
		assertEquals(countGaps(text, false), countGaps(text, true));
	}

	private static void assertVisual(String expected, String input) {
		StringBuilder output = new StringBuilder();
		PanguSpacingProcessor<Integer> processor = new PanguSpacingProcessor<>(new PanguSpacingProcessor.Target<>() {
			@Override
			public boolean advanceGap(int boundaryIndex, Integer boundaryStyle, Integer westernStyle) {
				output.append('·');
				return true;
			}

			@Override
			public boolean accept(int index, Integer style, int codePoint) {
				output.appendCodePoint(codePoint);
				return true;
			}
		}, false);
		feed(input, processor, false);
		assertEquals(expected, output.toString());
	}

	private static int countGaps(String input, boolean backwards) {
		int[] count = {0};
		PanguSpacingProcessor<Integer> processor = new PanguSpacingProcessor<>(new PanguSpacingProcessor.Target<>() {
			@Override
			public boolean advanceGap(int boundaryIndex, Integer boundaryStyle, Integer westernStyle) {
				count[0]++;
				return true;
			}

			@Override
			public boolean accept(int index, Integer style, int codePoint) {
				return true;
			}
		}, backwards);
		feed(input, processor, backwards);
		return count[0];
	}

	private static void feed(String input, PanguSpacingProcessor<Integer> processor, boolean backwards) {
		if (backwards) {
			for (int index = input.length(); index > 0;) {
				int codePoint = input.codePointBefore(index);
				index -= Character.charCount(codePoint);
				processor.accept(index, 1, codePoint);
			}
		} else {
			for (int index = 0; index < input.length();) {
				int codePoint = input.codePointAt(index);
				processor.accept(index, 1, codePoint);
				index += Character.charCount(codePoint);
			}
		}
		processor.finish();
	}

	private static final class RecordingTarget implements PanguSpacingProcessor.Target<Integer> {
		private final List<Integer> gapStyles = new ArrayList<>();

		@Override
		public boolean advanceGap(int boundaryIndex, Integer boundaryStyle, Integer westernStyle) {
			gapStyles.add(westernStyle);
			return true;
		}

		@Override
		public boolean accept(int index, Integer style, int codePoint) {
			return true;
		}
	}
}
