package stubborn.morebarrier.client.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.WritableLevelData;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import stubborn.morebarrier.ModBlocks;

@Mixin(ClientLevel.class)
public class MixinBarrierRender {

    @Shadow
    private final Minecraft minecraft = Minecraft.getInstance();

    @Inject(method = "getMarkerParticleTarget", at = @At("HEAD"), cancellable = true)
    private void injectGrassBlockMarker(CallbackInfoReturnable<Block> cir) {

        if (this.minecraft.gameMode.getPlayerMode() == GameType.CREATIVE) {
            Item mainhandItem = minecraft.player.getMainHandItem().getItem();
            Item offhandItem = minecraft.player.getOffhandItem().getItem();

            boolean isHoldingVisualizer = ModBlocks.BLOCKS_FOR_TAB.stream()
                    .anyMatch(block -> mainhandItem == block.asItem() || offhandItem == block.asItem())
                    || mainhandItem == (Items.BARRIER) || offhandItem == (Items.BARRIER);

            // 如果手持的是ModBlocks或者屏障
            if (isHoldingVisualizer) {
                // 直接返回草方块对象，强制触发 doAnimateTick 中的粒子生成逻辑
                cir.setReturnValue(Blocks.BARRIER);
            }
        }
    }


    /*private @Nullable Block getMarkerParticleTarget() {
        if (this.minecraft.gameMode.getPlayerMode() == GameType.CREATIVE) {
            ItemStack itemStack = this.minecraft.player.getMainHandItem();
            Item item = itemStack.getItem();
            if (MARKER_PARTICLE_ITEMS.contains(item) && item instanceof BlockItem) {
                BlockItem blockItem = (BlockItem)item;
                return blockItem.getBlock();
            }
        }

        return null;
    }*/
}