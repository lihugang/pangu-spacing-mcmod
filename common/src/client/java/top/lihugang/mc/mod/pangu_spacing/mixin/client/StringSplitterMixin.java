/*
 * SPDX-License-Identifier: GPL-3.0-only
 */

package top.lihugang.mc.mod.pangu_spacing.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.StringSplitter;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.FormattedCharSink;
import net.minecraft.util.StringDecomposer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.lihugang.mc.mod.pangu_spacing.spacing.SpacingFormattedCharSink;
import top.lihugang.mc.mod.pangu_spacing.spacing.SpacingLimitedSink;
import top.lihugang.mc.mod.pangu_spacing.spacing.SpacingSinkOwner;
import top.lihugang.mc.mod.pangu_spacing.spacing.SpacingWidthSink;

@Mixin(StringSplitter.class)
abstract class StringSplitterMixin {
	@Shadow
	@Final
	private StringSplitter.WidthProvider widthProvider;

	@Inject(method = "stringWidth(Ljava/lang/String;)F", at = @At("HEAD"), cancellable = true)
	private void panguSpacing$stringWidth(String text, CallbackInfoReturnable<Float> callback) {
		if (text == null) {
			callback.setReturnValue(0.0F);
			return;
		}
		SpacingWidthSink sink = new SpacingWidthSink(widthProvider, false);
		StringDecomposer.iterateFormatted(text, Style.EMPTY, sink);
		callback.setReturnValue(sink.finishAndGetWidth());
	}

	@Inject(method = "stringWidth(Lnet/minecraft/network/chat/FormattedText;)F", at = @At("HEAD"), cancellable = true)
	private void panguSpacing$formattedWidth(FormattedText text, CallbackInfoReturnable<Float> callback) {
		SpacingWidthSink sink = new SpacingWidthSink(widthProvider, false);
		StringDecomposer.iterateFormatted(text, Style.EMPTY, sink);
		callback.setReturnValue(sink.finishAndGetWidth());
	}

	@Inject(method = "stringWidth(Lnet/minecraft/util/FormattedCharSequence;)F", at = @At("HEAD"), cancellable = true)
	private void panguSpacing$sequenceWidth(FormattedCharSequence sequence, CallbackInfoReturnable<Float> callback) {
		SpacingWidthSink sink = new SpacingWidthSink(widthProvider, false);
		sequence.accept(sink);
		callback.setReturnValue(sink.finishAndGetWidth());
	}

	@Inject(method = "plainIndexAtWidth", at = @At("HEAD"), cancellable = true)
	private void panguSpacing$plainIndexAtWidth(String text, int maxWidth, Style style,
			CallbackInfoReturnable<Integer> callback) {
		SpacingLimitedSink sink = new SpacingLimitedSink(widthProvider, maxWidth, 0, false);
		StringDecomposer.iterate(text, style, sink);
		sink.finish();
		callback.setReturnValue(sink.position());
	}

	@Inject(method = "plainTailByWidth", at = @At("HEAD"), cancellable = true)
	private void panguSpacing$plainTailByWidth(String text, int maxWidth, Style style,
			CallbackInfoReturnable<String> callback) {
		SpacingLimitedSink sink = new SpacingLimitedSink(widthProvider, maxWidth, text.length(), true);
		StringDecomposer.iterateBackwards(text, style, sink);
		sink.finish();
		callback.setReturnValue(text.substring(sink.position()));
	}

	@WrapOperation(
			method = "findLineBreak",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/util/StringDecomposer;iterateFormatted(Ljava/lang/String;Lnet/minecraft/network/chat/Style;Lnet/minecraft/util/FormattedCharSink;)Z"
			)
	)
	private boolean panguSpacing$findLineBreak(String text, Style style, FormattedCharSink sink,
			Operation<Boolean> original) {
		SpacingFormattedCharSink spacingSink = spacingSink(sink);
		boolean completed = original.call(text, style, spacingSink);
		return completed && spacingSink.finishSegment();
	}

	@WrapOperation(
			method = {
					"splitLines(Ljava/lang/String;ILnet/minecraft/network/chat/Style;ZLnet/minecraft/client/StringSplitter$LinePosConsumer;)V",
					"splitLines(Lnet/minecraft/network/chat/FormattedText;ILnet/minecraft/network/chat/Style;Ljava/util/function/BiConsumer;)V"
			},
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/util/StringDecomposer;iterateFormatted(Ljava/lang/String;ILnet/minecraft/network/chat/Style;Lnet/minecraft/network/chat/Style;Lnet/minecraft/util/FormattedCharSink;)Z"
			)
	)
	private boolean panguSpacing$splitLines(String text, int start, Style style, Style resetStyle,
			FormattedCharSink sink, Operation<Boolean> original) {
		SpacingFormattedCharSink spacingSink = spacingSink(sink);
		boolean completed = original.call(text, start, style, resetStyle, spacingSink);
		return completed && spacingSink.finishSegment();
	}

	private static SpacingFormattedCharSink spacingSink(FormattedCharSink sink) {
		return ((SpacingSinkOwner) sink).panguSpacing$spacingSink();
	}
}
