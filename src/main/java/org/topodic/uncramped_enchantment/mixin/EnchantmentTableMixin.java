package org.topodic.uncramped_enchantment.mixin;
import net.minecraft.block.EnchantingTableBlock;

import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(EnchantingTableBlock.class)
public interface EnchantmentTableMixin {
	@Final
	@Mutable
	@Accessor("POWER_PROVIDER_OFFSETS")
	public static void setBookshelfOffsets(List<BlockPos> bookshelfOffsets) {
		throw new AssertionError();
	}
}