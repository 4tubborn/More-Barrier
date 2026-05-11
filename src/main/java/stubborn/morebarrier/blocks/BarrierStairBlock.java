package stubborn.morebarrier.blocks;

import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;

public class BarrierStairBlock extends StairBlock {

    public BarrierStairBlock(BlockState blockState, Properties properties) {
        super(blockState, properties);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }
}