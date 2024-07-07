package joe.wither_spawn_fix.mixin;

import net.minecraft.block.Blocks;
import net.minecraft.block.WitherSkullBlock;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.block.pattern.BlockPatternBuilder;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.predicate.block.BlockStatePredicate;
import net.minecraft.registry.tag.BlockTags;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WitherSkullBlock.class)
public class WitherSpawnMixin {
	@Shadow
	private static @Nullable BlockPattern witherBossPattern;

	@Inject(at = @At("HEAD"), cancellable = true, method = "getWitherBossPattern")
	private static void witherSpawnFix$getWitherBossPattern(CallbackInfoReturnable<BlockPattern> cir) {
		if (witherBossPattern == null) {
			witherBossPattern = BlockPatternBuilder.start().aisle(new String[]{"^^^", "###", "~#~"}).where('#', (pos) -> {
				return pos.getBlockState().isIn(BlockTags.WITHER_SUMMON_BASE_BLOCKS);
			}).where('^', CachedBlockPosition.matchesBlockState(BlockStatePredicate.forBlock(Blocks.WITHER_SKELETON_SKULL).or(BlockStatePredicate.forBlock(Blocks.WITHER_SKELETON_WALL_SKULL)))).where('~', (pos) -> {
				return !pos.getBlockState().isSolid();
			}).build();
		}

		cir.setReturnValue(witherBossPattern);
	}
}