package stubborn.morebarrier.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import stubborn.morebarrier.ModBlocks;

public class MoreBarrierClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ModBlocks.BLOCKS_FOR_TAB.forEach(block -> {
			BlockRenderLayerMap.putBlock(block, ChunkSectionLayer.CUTOUT);
		});
		BarrierVisibilityHandler.register();
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
	}
}