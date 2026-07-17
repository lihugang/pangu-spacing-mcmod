/*
 * SPDX-License-Identifier: GPL-3.0-only
 */

package top.lihugang.mc.mod.pangu_spacing.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSink;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import top.lihugang.mc.mod.pangu_spacing.spacing.SpacingFormattedCharSink;
import top.lihugang.mc.mod.pangu_spacing.spacing.SpacingSinkOwner;

@Mixin(targets = "net.minecraft.client.StringSplitter$1")
abstract class StringSplitterVisitorMixin {
	@WrapOperation(
			method = "accept",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/util/StringDecomposer;iterateFormatted(Ljava/lang/String;Lnet/minecraft/network/chat/Style;Lnet/minecraft/util/FormattedCharSink;)Z"
			)
	)
	private boolean panguSpacing$headByWidth(String text, Style style, FormattedCharSink sink,
			Operation<Boolean> original) {
		SpacingFormattedCharSink spacingSink = ((SpacingSinkOwner) sink).panguSpacing$spacingSink();
		boolean completed = original.call(text, style, spacingSink);
		return completed && spacingSink.finishSegment();
	}
}
