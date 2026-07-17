/*
 * SPDX-License-Identifier: GPL-3.0-only
 */

package top.lihugang.mc.mod.pangu_spacing.fabric;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import top.lihugang.mc.mod.pangu_spacing.PanguSpacingConfig;

public final class PanguSpacingCommands {
	private PanguSpacingCommands() {
	}

	public static void register() {
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> register(dispatcher));
	}

	private static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
		dispatcher.register(ClientCommands.literal("pangu")
				.executes(context -> sendStatus(context.getSource()))
				.then(ClientCommands.literal("toggle")
						.executes(context -> {
							PanguSpacingConfig.toggle();
							return sendStatus(context.getSource());
						})));
	}

	private static int sendStatus(FabricClientCommandSource source) {
		boolean enabled = PanguSpacingConfig.isEnabled();
		Component state = Component.translatable(
				enabled ? "pangu_spacing.status.enabled" : "pangu_spacing.status.disabled")
				.withStyle(enabled ? ChatFormatting.GREEN : ChatFormatting.RED);
		Component feedback = Component.literal("§e")
				.append(Component.translatable("pangu_spacing.status.prefix"))
				.append(Component.literal("§f"))
				.append(Component.translatable("pangu_spacing.status", state));
		source.sendFeedback(feedback);
		return 1;
	}
}
