package stubborn.morebarrier.blocks;

import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;

public class BarrierFenceGateBlock extends FenceGateBlock {
    public BarrierFenceGateBlock(WoodType woodType, Properties properties) {
        super(woodType, properties);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }
}