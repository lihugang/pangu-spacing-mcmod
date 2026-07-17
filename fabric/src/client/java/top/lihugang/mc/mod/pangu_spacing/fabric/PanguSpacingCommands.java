/*
 * SPDX-License-Identifier: GPL-3.0-only
 */

package top.lihugang.mc.mod.pangu_spacing.fabric;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ClickEvent;
import top.lihugang.mc.mod.pangu_spacing.PanguSpacingConfig;
import top.lihugang.mc.mod.pangu_spacing.PanguSpacingConstants;

import java.net.URI;

public final class PanguSpacingCommands {
	private PanguSpacingCommands() {
	}

	public static void register() {
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> register(dispatcher));
	}

	private static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
		dispatcher.register(ClientCommands.literal("pangu")
				.executes(context -> sendInfo(context.getSource()))
				.then(ClientCommands.literal("toggle")
						.executes(context -> {
							PanguSpacingConfig.toggle();
							return sendStatus(context.getSource());
						})));
	}

	private static int sendInfo(FabricClientCommandSource source) {
		source.sendFeedback(withPrefix(Component.translatable("pangu_spacing.info.version", getVersion())
				.withStyle(ChatFormatting.AQUA)));
		source.sendFeedback(withPrefix(Component.translatable(
				"pangu_spacing.info.developer", PanguSpacingConstants.DEVELOPER)
				.withStyle(ChatFormatting.AQUA)));
		source.sendFeedback(withPrefix(createStatusLine()));
		source.sendFeedback(withPrefix(Component.translatable("pangu_spacing.info.modrinth")
				.withStyle(ChatFormatting.WHITE)
				.append(createUrl(PanguSpacingConstants.MODRINTH_URL))));
		source.sendFeedback(withPrefix(Component.translatable("pangu_spacing.info.sponsor")
				.withStyle(ChatFormatting.WHITE)
				.append(createUrl(PanguSpacingConstants.SPONSOR_URL))));
		return 1;
	}

	private static int sendStatus(FabricClientCommandSource source) {
		source.sendFeedback(withPrefix(createStatusLine()));
		return 1;
	}

	private static Component createStatusLine() {
		boolean enabled = PanguSpacingConfig.isEnabled();
		Component state = Component.translatable(
				enabled ? "pangu_spacing.status.enabled" : "pangu_spacing.status.disabled")
				.withStyle(enabled ? ChatFormatting.GREEN : ChatFormatting.RED);
		return Component.translatable("pangu_spacing.info.status", state)
				.withStyle(ChatFormatting.WHITE);
	}

	private static Component withPrefix(Component line) {
		return Component.translatable("pangu_spacing.status.prefix")
				.withStyle(ChatFormatting.YELLOW)
				.append(Component.literal(" ").withStyle(ChatFormatting.WHITE))
				.append(line);
	}

	private static Component createUrl(String url) {
		return Component.literal(url)
				.withStyle(style -> style.withColor(ChatFormatting.GRAY)
						.withUnderlined(true)
						.withClickEvent(new ClickEvent.OpenUrl(URI.create(url))));
	}

	private static String getVersion() {
		return FabricLoader.getInstance().getModContainer(PanguSpacingConstants.MOD_ID)
				.map(container -> container.getMetadata().getVersion().getFriendlyString())
				.orElse(PanguSpacingConstants.UNKNOWN_VERSION);
	}
}
