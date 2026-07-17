/*
 * SPDX-License-Identifier: GPL-3.0-only
 */

package top.lihugang.mc.mod.pangu_spacing;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class PanguSpacingConfig {
	private static final Logger LOGGER = LoggerFactory.getLogger("pangu_spacing");
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final String ENABLED_KEY = "enabled";
	private static final boolean DEFAULT_ENABLED = true;

	private static volatile boolean enabled = DEFAULT_ENABLED;
	private static Path configPath;

	private PanguSpacingConfig() {
	}

	public static synchronized void initialize(Path configDirectory) {
		load(configDirectory.resolve("pangu_spacing.json"));
	}

	static synchronized void load(Path path) {
		configPath = path;
		if (!Files.isRegularFile(path)) {
			enabled = DEFAULT_ENABLED;
			save();
			return;
		}

		try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
			JsonObject json = GSON.fromJson(reader, JsonObject.class);
			enabled = json != null && json.has(ENABLED_KEY)
					&& json.get(ENABLED_KEY).isJsonPrimitive()
					&& json.getAsJsonPrimitive(ENABLED_KEY).isBoolean()
					? json.get(ENABLED_KEY).getAsBoolean()
					: DEFAULT_ENABLED;
		} catch (IOException | JsonParseException | IllegalStateException exception) {
			enabled = DEFAULT_ENABLED;
			LOGGER.warn("Could not read {}, using enabled state", path, exception);
		}
	}

	public static boolean isEnabled() {
		return enabled;
	}

	public static synchronized boolean toggle() {
		enabled = !enabled;
		save();
		return enabled;
	}

	public static synchronized void save() {
		if (configPath == null) {
			throw new IllegalStateException("Pangu Spacing config has not been initialized");
		}
		try {
			Files.createDirectories(configPath.getParent());
			JsonObject json = new JsonObject();
			json.addProperty(ENABLED_KEY, enabled);
			try (Writer writer = Files.newBufferedWriter(configPath, StandardCharsets.UTF_8)) {
				GSON.toJson(json, writer);
			}
		} catch (IOException exception) {
			LOGGER.warn("Could not save {}", configPath, exception);
		}
	}
}
