/*
 * SPDX-License-Identifier: GPL-3.0-only
 */

package top.lihugang.mc.mod.pangu_spacing.mixin.client;

import net.minecraft.client.StringSplitter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(StringSplitter.class)
public interface StringSplitterAccessor {
	@Accessor("widthProvider")
	StringSplitter.WidthProvider panguSpacing$getWidthProvider();
}
