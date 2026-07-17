/*
 * SPDX-License-Identifier: GPL-3.0-only
 */

package top.lihugang.mc.mod.pangu_spacing.neoforge;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;
import net.neoforged.neoforge.common.NeoForge;
import top.lihugang.mc.mod.pangu_spacing.PanguSpacingConfig;
import top.lihugang.mc.mod.pangu_spacing.PanguSpacingConstants;

import java.net.URI;

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
				.executes(context -> sendInfo(context.getSource()))
				.then(Commands.literal("toggle")
						.executes(context -> {
							PanguSpacingConfig.toggle();
							return sendStatus(context.getSource());
						})));
	}

	private static int sendInfo(CommandSourceStack source) {
		send(source, withPrefix(Component.translatable("pangu_spacing.info.version", getVersion())
				.withStyle(ChatFormatting.AQUA)));
		send(source, withPrefix(Component.translatable(
				"pangu_spacing.info.developer", PanguSpacingConstants.DEVELOPER)
				.withStyle(ChatFormatting.AQUA)));
		send(source, withPrefix(createStatusLine()));
		send(source, withPrefix(Component.translatable("pangu_spacing.info.modrinth")
				.withStyle(ChatFormatting.WHITE)
				.append(createUrl(PanguSpacingConstants.MODRINTH_URL))));
		send(source, withPrefix(Component.translatable("pangu_spacing.info.sponsor")
				.withStyle(ChatFormatting.WHITE)
				.append(createUrl(PanguSpacingConstants.SPONSOR_URL))));
		return 1;
	}

	private static int sendStatus(CommandSourceStack source) {
		send(source, withPrefix(createStatusLine()));
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

	private static void send(CommandSourceStack source, Component component) {
		source.sendSuccess(() -> component, false);
	}

	private static String getVersion() {
		return ModList.get().getModContainerById(PanguSpacingConstants.MOD_ID)
				.map(container -> container.getModInfo().getVersion().toString())
				.orElse(PanguSpacingConstants.UNKNOWN_VERSION);
	}
}
