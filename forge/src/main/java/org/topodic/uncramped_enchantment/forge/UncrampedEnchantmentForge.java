package org.topodic.uncramped_enchantment.forge;

import net.minecraftforge.fml.loading.FMLPaths;
import org.topodic.uncramped_enchantment.UncrampedEnchantment;
import net.minecraftforge.fml.common.Mod;

@Mod("uncramped_enchantment")
public class UncrampedEnchantmentForge {
	public UncrampedEnchantmentForge() {
		UncrampedEnchantment.init(FMLPaths.CONFIGDIR.get());
	}
}
