package org.topodic.uncramped_enchantment;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import net.minecraft.util.math.BlockPos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.topodic.uncramped_enchantment.mixin.EnchantmentTableMixin;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;

public final class UncrampedEnchantment {
	public static final String MOD_ID = "uncramped_enchantment";
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	private static final int MAX_DISTANCE = 16;
	private static final List<BlockPos> DEFAULT_OFFSETS =
			BlockPos.stream(-5, -1, -5, 5, 3, 5)
					.filter((blockPos) ->
							(Math.abs(blockPos.getX()) >= 2 && Math.abs(blockPos.getX()) <= 5) ||
									(Math.abs(blockPos.getZ()) >= 2 && Math.abs(blockPos.getZ()) <= 5))
					.map(BlockPos::toImmutable).toList();

	public static void init(Path configPath) {
		var configFile = configPath.resolve(MOD_ID.concat(".json"));
		if (!Files.isRegularFile(configFile)) {
			writeDefaultConfig(configFile);
		}
		try {
			JsonElement json = JsonParser.parseString(new String(Files.readAllBytes(configFile)));
			TypeToken<HashMap<String, Integer>> token = new TypeToken<>(){};
			HashMap<String, Integer> config = GSON.fromJson(json, token);
			try {
				var minD = config.getOrDefault("minDistance", 2);
				var maxD = config.getOrDefault("maxDistance", 5);
				var minH = config.getOrDefault("minHeight", -1);
				var maxH = config.getOrDefault("maxHeight", 3);

				boolean dumbConfig = minD > maxD || minH > maxH;
				if (dumbConfig) LOGGER.warn("Minimum values exceed max, they will be clamped. Behavior may be unexpected.");

				boolean warnForOverMax = minD < 0 || maxD < 0 || minD > MAX_DISTANCE || maxD > MAX_DISTANCE || minH < -MAX_DISTANCE || minH > MAX_DISTANCE || maxH < -MAX_DISTANCE || maxH > MAX_DISTANCE;
				if (warnForOverMax) LOGGER.warn("Config values are out of expected range, they will be clamped.");

				minD = Math.min(Math.max(minD, 0), MAX_DISTANCE);
				maxD = Math.min(Math.max(maxD, minD), MAX_DISTANCE);
				minH = Math.min(Math.max(minH, -MAX_DISTANCE), MAX_DISTANCE);
				maxH = Math.min(Math.max(maxH, minH), MAX_DISTANCE);

				int finalMinD = minD;
				int finalMaxD = maxD;

				var offsets = BlockPos.stream(-maxD, minH, -maxD, maxD, maxH, maxD)
						.filter((blockPos) ->
								(Math.abs(blockPos.getX()) >= finalMinD && Math.abs(blockPos.getX()) <= finalMaxD) || (Math.abs(blockPos.getZ()) >= finalMinD && Math.abs(blockPos.getZ()) <= finalMaxD))
						.map(BlockPos::toImmutable).toList();

				EnchantmentTableMixin.setBookshelfOffsets(offsets);
			} catch (Exception e) {
				LOGGER.error("Config incorrect or malformed, reinitializing config with default values.");
				writeDefaultConfig(configFile);
				EnchantmentTableMixin.setBookshelfOffsets(DEFAULT_OFFSETS);
			}
		} catch (IOException e) {
			LOGGER.error("Invalid config file, using defaults.");
			writeDefaultConfig(configFile);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static void writeDefaultConfig(Path configFile) {
		var defaultConfig = new HashMap<String, Integer>();
		defaultConfig.put("minimumDistance", 2);
		defaultConfig.put("maximumDistance", 5);
		defaultConfig.put("minimumHeight", -1);
		defaultConfig.put("maximumHeight", 3);
		try (BufferedWriter writer = Files.newBufferedWriter(configFile)) {
			writer.write(GSON.toJson(defaultConfig));
		} catch (Exception e) {
			LOGGER.error("Failed to write default config to file.", e);
		}
	}

}