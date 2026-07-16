/*
 * SPDX-License-Identifier: GPL-3.0-only
 */

package top.lihugang.mc.mod.pangu_spacing.spacing;

import java.util.Objects;

/**
 * Adds visual gap events to a styled code-point stream without changing its contents or indices.
 */
public final class PanguSpacingProcessor<S> {
	private static final int DOUBLE_QUOTE = 1;
	private static final int SINGLE_QUOTE = 1 << 1;
	private static final int BACKTICK_QUOTE = 1 << 2;
	private static final int HEBREW_QUOTE = 1 << 3;

	private final Target<S> target;
	private final boolean backwards;

	private boolean hasPending;
	private boolean stopped;
	private int pendingIndex;
	private int pendingCodePoint;
	private int pendingKind;
	private S pendingStyle;

	private int lastKind = PanguCharacterClassifier.END;
	private int beforeLastKind = PanguCharacterClassifier.END;
	private S lastStyle;
	private S beforeLastStyle;
	private int punctuationAnchorKind = PanguCharacterClassifier.END;
	private S punctuationAnchorStyle;
	private int operatorAnchorKind = PanguCharacterClassifier.END;
	private S operatorAnchorStyle;
	private int quoteMask;

	public PanguSpacingProcessor(Target<S> target, boolean backwards) {
		this.target = Objects.requireNonNull(target, "target");
		this.backwards = backwards;
	}

	public boolean accept(int index, S style, int codePoint) {
		if (stopped) {
			return false;
		}

		int kind = PanguCharacterClassifier.classify(codePoint, backwards);
		if (!hasPending) {
			storePending(index, style, codePoint, kind);
			return true;
		}

		if (!emitPending(kind)) {
			stopped = true;
			return false;
		}
		storePending(index, style, codePoint, kind);
		return true;
	}

	/** Flushes a source segment while retaining context for the next styled segment. */
	public boolean finishSegment() {
		if (stopped) {
			return false;
		}
		if (!hasPending) {
			return true;
		}
		if (!emitPending(PanguCharacterClassifier.END)) {
			stopped = true;
			return false;
		}
		hasPending = false;
		return true;
	}

	public boolean finish() {
		return finishSegment();
	}

	public void adjustPendingIndex(int delta) {
		if (hasPending) {
			pendingIndex += delta;
		}
	}

	private void storePending(int index, S style, int codePoint, int kind) {
		hasPending = true;
		pendingIndex = index;
		pendingStyle = style;
		pendingCodePoint = codePoint;
		pendingKind = kind;
	}

	private boolean emitPending(int nextKind) {
		int resolvedKind = resolveQuoteKind(pendingKind, pendingCodePoint, nextKind);
		S gapStyle = gapStyleBefore(resolvedKind, nextKind);
		if (gapStyle != null && !target.advanceGap(pendingIndex, pendingStyle, gapStyle)) {
			return false;
		}
		if (!target.accept(pendingIndex, pendingStyle, pendingCodePoint)) {
			return false;
		}
		updateHistory(resolvedKind, pendingStyle, pendingCodePoint);
		return true;
	}

	private int resolveQuoteKind(int kind, int codePoint, int nextKind) {
		if (kind != PanguCharacterClassifier.QUOTE) {
			return kind;
		}
		if (codePoint == '\'' && lastKind == PanguCharacterClassifier.WESTERN
				&& nextKind == PanguCharacterClassifier.WESTERN) {
			return PanguCharacterClassifier.WESTERN;
		}

		int bit = quoteBit(codePoint);
		return (quoteMask & bit) == 0 ? PanguCharacterClassifier.OPEN : PanguCharacterClassifier.CLOSE;
	}

	private S gapStyleBefore(int currentKind, int nextKind) {
		if (lastKind == PanguCharacterClassifier.WHITESPACE
				|| currentKind == PanguCharacterClassifier.WHITESPACE
				|| currentKind == PanguCharacterClassifier.MARK) {
			return null;
		}

		if (currentKind == PanguCharacterClassifier.OPERATOR
				&& isWord(lastKind) && isWord(nextKind)
				&& (lastKind == PanguCharacterClassifier.CJK || nextKind == PanguCharacterClassifier.CJK)) {
			return pendingStyle;
		}
		if (lastKind == PanguCharacterClassifier.OPERATOR
				&& isWord(currentKind) && isWord(operatorAnchorKind)
				&& (currentKind == PanguCharacterClassifier.CJK
						|| operatorAnchorKind == PanguCharacterClassifier.CJK)) {
			return westernStyle(currentKind, pendingStyle, operatorAnchorKind, operatorAnchorStyle, lastStyle);
		}

		if (lastKind == PanguCharacterClassifier.PUNCTUATION
				&& isWord(currentKind) && isWord(punctuationAnchorKind)
				&& (currentKind == PanguCharacterClassifier.CJK
						|| punctuationAnchorKind == PanguCharacterClassifier.CJK)) {
			return westernStyle(currentKind, pendingStyle, punctuationAnchorKind, punctuationAnchorStyle, lastStyle);
		}

		if (lastKind == PanguCharacterClassifier.CJK && currentKind == PanguCharacterClassifier.WESTERN) {
			return pendingStyle;
		}
		if (lastKind == PanguCharacterClassifier.WESTERN && currentKind == PanguCharacterClassifier.CJK) {
			return lastStyle;
		}
		if (lastKind == PanguCharacterClassifier.CJK
				&& (currentKind == PanguCharacterClassifier.OPEN || currentKind == PanguCharacterClassifier.PREFIX)) {
			return pendingStyle;
		}
		if ((lastKind == PanguCharacterClassifier.CLOSE || lastKind == PanguCharacterClassifier.PREFIX)
				&& currentKind == PanguCharacterClassifier.CJK) {
			return lastStyle;
		}
		return null;
	}

	private S westernStyle(int currentKind, S currentStyle, int anchorKind, S anchorStyle, S fallbackStyle) {
		if (currentKind == PanguCharacterClassifier.WESTERN) {
			return currentStyle;
		}
		if (anchorKind == PanguCharacterClassifier.WESTERN) {
			return anchorStyle;
		}
		return fallbackStyle;
	}

	private void updateHistory(int kind, S style, int codePoint) {
		if (pendingKind == PanguCharacterClassifier.QUOTE && kind != PanguCharacterClassifier.WESTERN) {
			quoteMask ^= quoteBit(codePoint);
		}

		if (kind == PanguCharacterClassifier.MARK) {
			return;
		}
		if (codePoint == '\n' || codePoint == '\r') {
			resetLineState();
			return;
		}

		beforeLastKind = lastKind;
		beforeLastStyle = lastStyle;
		lastKind = kind;
		lastStyle = style;

		if (kind == PanguCharacterClassifier.PUNCTUATION) {
			if (beforeLastKind != PanguCharacterClassifier.PUNCTUATION) {
				punctuationAnchorKind = beforeLastKind;
				punctuationAnchorStyle = beforeLastStyle;
			}
		} else {
			punctuationAnchorKind = PanguCharacterClassifier.END;
			punctuationAnchorStyle = null;
		}

		if (kind == PanguCharacterClassifier.OPERATOR) {
			if (beforeLastKind != PanguCharacterClassifier.OPERATOR) {
				operatorAnchorKind = beforeLastKind;
				operatorAnchorStyle = beforeLastStyle;
			}
		} else if (beforeLastKind != PanguCharacterClassifier.OPERATOR) {
			operatorAnchorKind = PanguCharacterClassifier.END;
			operatorAnchorStyle = null;
		}
	}

	private void resetLineState() {
		lastKind = PanguCharacterClassifier.END;
		beforeLastKind = PanguCharacterClassifier.END;
		lastStyle = null;
		beforeLastStyle = null;
		punctuationAnchorKind = PanguCharacterClassifier.END;
		punctuationAnchorStyle = null;
		operatorAnchorKind = PanguCharacterClassifier.END;
		operatorAnchorStyle = null;
		quoteMask = 0;
	}

	private static boolean isWord(int kind) {
		return kind == PanguCharacterClassifier.CJK || kind == PanguCharacterClassifier.WESTERN;
	}

	private static int quoteBit(int codePoint) {
		return switch (codePoint) {
			case '\'' -> SINGLE_QUOTE;
			case '`' -> BACKTICK_QUOTE;
			case 0x05F4 -> HEBREW_QUOTE;
			default -> DOUBLE_QUOTE;
		};
	}

	public interface Target<S> {
		boolean advanceGap(int boundaryIndex, S boundaryStyle, S westernStyle);

		boolean accept(int index, S style, int codePoint);
	}
}
