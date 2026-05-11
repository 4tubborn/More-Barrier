package stubborn.morebarrier.client.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import stubborn.morebarrier.ModBlocks;

@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class MixinBlockRender {



    @Shadow public abstract Block getBlock();

    @Shadow
    public boolean is(Block block) {
        return false;
    }

    @Inject(method = "getRenderShape", at = @At("HEAD"), cancellable = true)
    private void morebarrier$forceShowIfHeld(CallbackInfoReturnable<RenderShape> cir) {
        Minecraft client = Minecraft.getInstance();
        if (client.player == null || client.player.isSpectator()) return;

        // 获取手持物品及其对应的 Item 实例，避免 .is() 方法的歧义
        ItemStack mainHand = client.player.getMainHandItem();
        ItemStack offHand = client.player.getOffhandItem();
        Item mainItem = mainHand.getItem();
        Item offItem = offHand.getItem();

        // 核心逻辑：判断玩家是否持有原版屏障或本 Mod 中的任何屏障变种
        // 使用 .stream().anyMatch 并显式比较 Item 实例
        boolean holdingModBlock = ModBlocks.BLOCKS_FOR_TAB.stream()
                .anyMatch(block -> mainItem == block.asItem() || offItem == block.asItem());

        boolean isHoldingVisualizer = mainHand.is(Items.BARRIER) ||
                offHand.is(Items.BARRIER) ||
                holdingModBlock;

        if (isHoldingVisualizer) {
            // 如果当前渲染的方块属于本 Mod 的屏障系列
            if (ModBlocks.BLOCKS_FOR_TAB.contains(this.getBlock()) || this.is(Blocks.BARRIER)) {
                // 强制返回 MODEL 状态，使渲染引擎（BlockRenderDispatcher）加载对应的 JSON 模型
                cir.setReturnValue(RenderShape.MODEL);
            }
        }
    }
}