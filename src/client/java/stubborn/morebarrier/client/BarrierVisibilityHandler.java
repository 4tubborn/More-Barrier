package stubborn.morebarrier.client;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import stubborn.morebarrier.ModBlocks;

public class BarrierVisibilityHandler {
    // 记录上一帧的显形状态
    private static boolean wasHoldingVisualizer = false;

    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null || client.level == null) return;

            // 1. 获取手持物品及其 Item 实例（避开 is 重载歧义）
            ItemStack mainHand = client.player.getMainHandItem();
            ItemStack offHand = client.player.getOffhandItem();
            Item mainItem = mainHand.getItem();
            Item offItem = offHand.getItem();

            // 2. 核心逻辑判断：是否应该显形
            // 只要手里拿着原版屏障，或者拿着你 List 里的任何一个方块物品
            boolean isHolding = mainHand.is(Items.BARRIER) || offHand.is(Items.BARRIER) ||
                    ModBlocks.BLOCKS_FOR_TAB.stream().anyMatch(b -> mainItem == b.asItem() || offItem == b.asItem());

            // 3. 状态切换检测
            // 在你的 BarrierVisibilityHandler 的 ClientTick 逻辑中
            if (isHolding != wasHoldingVisualizer) {
                int radius = 32;
                BlockPos playerPos = client.player.blockPosition();

                // 定义局部刷新的范围
                BlockPos minPos = playerPos.offset(-radius, -radius, -radius);
                BlockPos maxPos = playerPos.offset(radius, radius, radius);

                // 遍历该范围内的区块并标记为“脏”
                // 注意：为了性能，我们通常调用 levelRenderer 的方法或者 level 的通知方法
                for (BlockPos pos : BlockPos.betweenClosed(minPos, maxPos)) {
                    BlockState state = client.level.getBlockState(pos);

                    // 只有当该位置是你的 Mod 方块或者是原版屏障时，才触发状态更新通知
                    // 这样可以避免对普通方块造成不必要的计算开销
                    if (ModBlocks.BLOCKS_FOR_TAB.contains(state.getBlock()) || state.is(Blocks.BARRIER)) {
                        // 这里的参数通常是 (pos, oldState, newState, flags)
                        // 我们传入相同的 state 即可触发重新渲染
                        client.level.sendBlockUpdated(pos, state, state, 3);
                    }
                }

                wasHoldingVisualizer = isHolding;
            }
        });
    }
}