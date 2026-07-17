/*
 * SPDX-License-Identifier: GPL-3.0-only
 */

package top.lihugang.mc.mod.pangu_spacing.neoforge;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;
import net.neoforged.neoforge.common.NeoForge;
import top.lihugang.mc.mod.pangu_spacing.PanguSpacingConfig;

public final class PanguSpacingNeoForgeCommands {
	private PanguSpacingNeoForgeCommands() {
	}

	public static void register() {
		NeoForge.EVENT_BUS.addListener(RegisterClientCommandsEvent.class,
				PanguSpacingNeoForgeCommands::register);
	}

	private static void register(RegisterClientCommandsEvent event) {
		CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
		dispatcher.register(Commands.literal("pangu")
				.executes(context -> sendStatus(context.getSource()))
				.then(Commands.literal("toggle")
						.executes(context -> {
							PanguSpacingConfig.toggle();
							return sendStatus(context.getSource());
						})));
	}

	private static int sendStatus(CommandSourceStack source) {
		boolean enabled = PanguSpacingConfig.isEnabled();
		Component state = Component.translatable(
				enabled ? "pangu_spacing.status.enabled" : "pangu_spacing.status.disabled")
				.withStyle(enabled ? ChatFormatting.GREEN : ChatFormatting.RED);
		Component feedback = Component.translatable("pangu_spacing.status.prefix")
				.withStyle(ChatFormatting.YELLOW)
				.append(Component.translatable("pangu_spacing.status", state));
		source.sendSuccess(() -> feedback, false);
		return 1;
	}
}
