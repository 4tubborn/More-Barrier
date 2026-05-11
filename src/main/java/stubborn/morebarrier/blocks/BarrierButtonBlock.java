package stubborn.morebarrier.blocks;

import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;

public class BarrierButtonBlock extends ButtonBlock {
    public BarrierButtonBlock(BlockSetType blockSetType, int i, Properties properties) {
        super(blockSetType, i, properties);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }
}
