package org.topodic.uncramped_enchantment.neoforge;

import org.topodic.uncramped_enchantment.UncrampedEnchantment;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.fml.common.Mod;

@Mod("uncramped_enchantment")
public class UncrampedEnchantmentNeoForge {
	public UncrampedEnchantmentNeoForge() {
		UncrampedEnchantment.init(FMLPaths.CONFIGDIR.get());
	}
}
