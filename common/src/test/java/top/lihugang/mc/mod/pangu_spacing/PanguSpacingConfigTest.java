/*
 * SPDX-License-Identifier: GPL-3.0-only
 */

package top.lihugang.mc.mod.pangu_spacing;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class PanguSpacingConfigTest {
	@TempDir
	Path temporaryDirectory;

	@Test
	void defaultsToEnabledWhenConfigIsMissing() {
		Path config = temporaryDirectory.resolve("missing.json");
		PanguSpacingConfig.load(config);

		assertTrue(PanguSpacingConfig.isEnabled());
		assertTrue(Files.exists(config));
	}

	@Test
	void persistsToggledState() throws IOException {
		Path config = temporaryDirectory.resolve("config").resolve("pangu_spacing.json");
		PanguSpacingConfig.load(config);

		assertFalse(PanguSpacingConfig.toggle());
		String saved = Files.readString(config, StandardCharsets.UTF_8);
		assertFalse(saved.contains("developer"));
		assertFalse(saved.contains("modrinth_url"));
		assertFalse(saved.contains("ifdian.net"));
		PanguSpacingConfig.load(config);
		assertFalse(PanguSpacingConfig.isEnabled());

		assertTrue(PanguSpacingConfig.toggle());
		PanguSpacingConfig.load(config);
		assertTrue(PanguSpacingConfig.isEnabled());
	}

	@Test
	void fallsBackToEnabledForMalformedOrMissingState() throws IOException {
		Path config = temporaryDirectory.resolve("pangu_spacing.json");

		Files.writeString(config, "{not json", StandardCharsets.UTF_8);
		PanguSpacingConfig.load(config);
		assertTrue(PanguSpacingConfig.isEnabled());

		Files.writeString(config, "{}", StandardCharsets.UTF_8);
		PanguSpacingConfig.load(config);
		assertTrue(PanguSpacingConfig.isEnabled());

		Files.writeString(config, "{\"enabled\":\"false\"}", StandardCharsets.UTF_8);
		PanguSpacingConfig.load(config);
		assertTrue(PanguSpacingConfig.isEnabled());
	}
}
