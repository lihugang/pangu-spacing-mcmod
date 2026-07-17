/*
 * SPDX-License-Identifier: GPL-3.0-only
 */

package top.lihugang.mc.mod.pangu_spacing.spacing;

final class PanguCharacterClassifier {
	static final int END = 0;
	static final int CJK = 1;
	static final int WESTERN = 2;
	static final int OPEN = 3;
	static final int CLOSE = 4;
	static final int QUOTE = 5;
	static final int PUNCTUATION = 6;
	static final int OPERATOR = 7;
	static final int PREFIX = 8;
	static final int WHITESPACE = 9;
	static final int MARK = 10;
	static final int OTHER = 11;

	private PanguCharacterClassifier() {
	}

	static int classify(int codePoint, boolean backwards) {
		if (codePoint < 0) {
			return END;
		}

		if (codePoint <= 0x7F) {
			if (isAsciiLetterOrDigit(codePoint)) {
				return WESTERN;
			}
			return switch (codePoint) {
				case '(', '[', '{' -> backwards ? CLOSE : OPEN;
				case ')', ']', '}' -> backwards ? OPEN : CLOSE;
				case '\'', '"', '`' -> QUOTE;
				case '!', ';', ',', '?', ':', '.', '~' -> PUNCTUATION;
				case '+', '-', '*', '=', '&', '<', '>', '/', '\\', '|' -> OPERATOR;
				case '#', '@', '$', '%', '^', '_' -> PREFIX;
				default -> Character.isWhitespace(codePoint) ? WHITESPACE : OTHER;
			};
		}

		if (codePoint == 0x00B7 || codePoint == 0x2022 || codePoint == 0x2027 || codePoint == 0x30FB) {
			return OTHER;
		}
		if (Character.isWhitespace(codePoint) || Character.isSpaceChar(codePoint)) {
			return WHITESPACE;
		}
		if (codePoint == 0x2018 || codePoint == 0x201C) {
			return backwards ? CLOSE : OPEN;
		}
		if (codePoint == 0x2019 || codePoint == 0x201D) {
			return backwards ? OPEN : CLOSE;
		}
		if (codePoint == 0x05F4) {
			return QUOTE;
		}
		if (isCjk(codePoint)) {
			return CJK;
		}
		if (isWestern(codePoint)) {
			return WESTERN;
		}

		int type = Character.getType(codePoint);
		if (type == Character.NON_SPACING_MARK
				|| type == Character.COMBINING_SPACING_MARK
				|| type == Character.ENCLOSING_MARK
				|| codePoint == 0x200C
				|| codePoint == 0x200D
				|| codePoint == 0xFE0E
				|| codePoint == 0xFE0F) {
			return MARK;
		}
		return OTHER;
	}

	private static boolean isAsciiLetterOrDigit(int codePoint) {
		return codePoint >= '0' && codePoint <= '9'
				|| codePoint >= 'A' && codePoint <= 'Z'
				|| codePoint >= 'a' && codePoint <= 'z';
	}

	private static boolean isCjk(int codePoint) {
		return codePoint >= 0x2E80 && codePoint <= 0x2EFF
				|| codePoint >= 0x2F00 && codePoint <= 0x2FDF
				|| codePoint >= 0x3040 && codePoint <= 0x309F
				|| codePoint >= 0x30A0 && codePoint <= 0x30FA
				|| codePoint >= 0x30FC && codePoint <= 0x30FF
				|| codePoint >= 0x3100 && codePoint <= 0x312F
				|| codePoint >= 0x3200 && codePoint <= 0x32FF
				|| codePoint >= 0x3400 && codePoint <= 0x4DBF
				|| codePoint >= 0x4E00 && codePoint <= 0x9FFF
				|| codePoint >= 0xF900 && codePoint <= 0xFAFF
				|| codePoint >= 0x20000 && codePoint <= 0x2FA1F
				|| codePoint >= 0x30000 && codePoint <= 0x323AF;
	}

	private static boolean isWestern(int codePoint) {
		return codePoint >= 0x00A1 && codePoint <= 0x02AF
				|| codePoint >= 0x0370 && codePoint <= 0x03FF
				|| codePoint >= 0x1D00 && codePoint <= 0x1D7F
				|| codePoint >= 0x1E00 && codePoint <= 0x1EFF
				|| codePoint >= 0x2070 && codePoint <= 0x209F
				|| codePoint >= 0x2150 && codePoint <= 0x218F
				|| codePoint >= 0x2700 && codePoint <= 0x27BF
				|| codePoint >= 0x2C60 && codePoint <= 0x2C7F
				|| codePoint >= 0xA720 && codePoint <= 0xA7FF
				|| codePoint >= 0xAB30 && codePoint <= 0xAB6F
				|| codePoint >= 0x10780 && codePoint <= 0x107BF
				|| codePoint >= 0x1DF00 && codePoint <= 0x1DFFF;
	}
}
