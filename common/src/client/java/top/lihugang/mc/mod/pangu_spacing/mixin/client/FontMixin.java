/*
 * SPDX-License-Identifier: GPL-3.0-only
 */

package top.lihugang.mc.mod.pangu_spacing.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.FormattedCharSink;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import top.lihugang.mc.mod.pangu_spacing.spacing.GapAdvancer;
import top.lihugang.mc.mod.pangu_spacing.spacing.GapWidths;
import top.lihugang.mc.mod.pangu_spacing.spacing.SpacingFormattedCharSink;

@Mixin(Font.class)
abstract class FontMixin {
	@WrapOperation(
			method = "prepareText(Ljava/lang/String;FFIZI)Lnet/minecraft/client/gui/Font$PreparedText;",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/util/StringDecomposer;iterateFormatted(Ljava/lang/String;Lnet/minecraft/network/chat/Style;Lnet/minecraft/util/FormattedCharSink;)Z"
			)
	)
	private boolean panguSpacing$spacePreparedString(String text, Style style, FormattedCharSink sink,
			Operation<Boolean> original) {
		SpacingFormattedCharSink spacingSink = builderSpacingSink(sink);
		boolean completed = original.call(text, style, spacingSink);
		return completed && spacingSink.finish();
	}

	@WrapOperation(
			method = "prepareText(Lnet/minecraft/util/FormattedCharSequence;FFIZZI)Lnet/minecraft/client/gui/Font$PreparedText;",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/util/FormattedCharSequence;accept(Lnet/minecraft/util/FormattedCharSink;)Z"
			)
	)
	private boolean panguSpacing$spacePreparedSequence(FormattedCharSequence sequence, FormattedCharSink sink,
			Operation<Boolean> original) {
		SpacingFormattedCharSink spacingSink = builderSpacingSink(sink);
		boolean completed = original.call(sequence, spacingSink);
		return completed && spacingSink.finish();
	}

	@WrapOperation(
			method = "drawInBatch8xOutline",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/util/FormattedCharSequence;accept(Lnet/minecraft/util/FormattedCharSink;)Z",
					ordinal = 0
			)
	)
	private boolean panguSpacing$spaceOutlinePass(FormattedCharSequence sequence, FormattedCharSink sink,
			Operation<Boolean> original, @Local(ordinal = 0) float[] cursor) {
		Font font = (Font) (Object) this;
		GapAdvancer cursorAdvancer = (boundaryIndex, boundaryStyle, westernStyle) -> {
			cursor[0] += GapWidths.forFont(font, westernStyle);
			return true;
		};
		SpacingFormattedCharSink spacingSink = new SpacingFormattedCharSink(sink, cursorAdvancer, false);
		boolean completed = original.call(sequence, spacingSink);
		return completed && spacingSink.finish();
	}

	@WrapOperation(
			method = "drawInBatch8xOutline",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/util/FormattedCharSequence;accept(Lnet/minecraft/util/FormattedCharSink;)Z",
					ordinal = 1
			)
	)
	private boolean panguSpacing$spaceOutlinedText(FormattedCharSequence sequence, FormattedCharSink sink,
			Operation<Boolean> original) {
		SpacingFormattedCharSink spacingSink = builderSpacingSink(sink);
		boolean completed = original.call(sequence, spacingSink);
		return completed && spacingSink.finish();
	}

	private static SpacingFormattedCharSink builderSpacingSink(FormattedCharSink sink) {
		return new SpacingFormattedCharSink(sink, (GapAdvancer) sink, false);
	}
}
