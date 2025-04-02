package org.topodic.uncramped_enchantment.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.topodic.uncramped_enchantment.UncrampedEnchantment;

public class UncrampedEnchantmentFabric implements ModInitializer {
	@Override
	public void onInitialize() {
		UncrampedEnchantment.init(FabricLoader.getInstance().getConfigDir());
	}
}
