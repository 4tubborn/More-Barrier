package stubborn.morebarrier;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.type.BlockSetTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.block.type.WoodTypeBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;
import stubborn.morebarrier.MoreBarrier;
import stubborn.morebarrier.blocks.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ModBlocks {

    // 在 ModBlocks 类中添加
    /*public static final java.util.Map<BlockPos, Long> ACTIVE_POSITIONS = new java.util.concurrent.ConcurrentHashMap<>();
    // 预存 Item 列表，防止在 Tick 里跑 Stream 导致掉帧
    public static java.util.Set<Item> BLOCKS_FOR_TAB_ITEMS = new java.util.HashSet<>();

    public static void markPosForRender(BlockPos pos) {
        // 500ms 的有效期，足够应付异步渲染延迟
        ACTIVE_POSITIONS.put(pos.immutable(), System.currentTimeMillis() + 500);
    }

    public static boolean isPosActive(BlockPos pos) {
        Long expiry = ACTIVE_POSITIONS.get(pos);
        if (expiry == null) return false;
        if (System.currentTimeMillis() > expiry) {
            ACTIVE_POSITIONS.remove(pos); // 清理过期坐标
            return false;
        }
        return true;
    }*/

    public static final List<Block> BLOCKS_FOR_TAB = new ArrayList<>();

    public static final BlockSetType BARRIER_SET = BlockSetTypeBuilder.copyOf(BlockSetType.OAK)
            .build(Identifier.fromNamespaceAndPath(MoreBarrier.MOD_ID, "barrier"));

    public static final WoodType BARRIER_WOOD_TYPE = WoodTypeBuilder.copyOf(WoodType.OAK)
            .build(Identifier.fromNamespaceAndPath(MoreBarrier.MOD_ID, "barrier"), BARRIER_SET);

    /*private static Block register(Block block, String id) {
        Identifier blockID = Identifier.fromNamespaceAndPath(MoreBarrier.MOD_ID, id);
        BlockItem blockItem = new BlockItem(block, new Item.Properties().setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(MoreBarrier.MOD_ID, id))));
        Registry.register(BuiltInRegistries.ITEM, blockID, blockItem);
        return Registry.register(BuiltInRegistries.BLOCK, blockID, block);
    }*/


    /*public static final Block BARRIER_STAIRS = register("barrier_stairs",
            (p) -> new BarrierStairBlock(Blocks.BARRIER.defaultBlockState(), p));

    public static final Block BARRIER_SLAB = register("barrier_slab", BarrierSlabBlock::new);*/

    // 2. 楼梯 (Stairs) - 需要传入 baseState
    public static final Block BARRIER_STAIRS = register("barrier_stairs",(p) -> new StairBlock(Blocks.BARRIER.defaultBlockState(), p){
        @Override
        protected RenderShape getRenderShape(BlockState state) {
            return RenderShape.INVISIBLE;
        }
    });

    // 1. 台阶 (Slab)
    public static final Block BARRIER_SLAB = register("barrier_slab", (p) -> new SlabBlock(p){
        @Override
        protected RenderShape getRenderShape(BlockState state) {
            return RenderShape.INVISIBLE;
        }
    });

    // 6. 栅栏 (Fence)
    public static final Block BARRIER_FENCE = register("barrier_fence", (p) -> new FenceBlock(p){
        @Override
        protected RenderShape getRenderShape(BlockState state) {
            return RenderShape.INVISIBLE;
        }
    });

    // 7. 栅栏门 (Fence Gate)
    public static final Block BARRIER_FENCE_GATE = register("barrier_fence_gate",
            (p) -> new FenceGateBlock(BARRIER_WOOD_TYPE, p){
                @Override
                protected RenderShape getRenderShape(BlockState state) {
                    return RenderShape.INVISIBLE;
                }
            });

    // 4. 门 (Door) - 需要传入 BlockSetType
    public static final Block BARRIER_DOOR = register("barrier_door",
            (p) -> new DoorBlock(BARRIER_SET, p){
                @Override
                protected RenderShape getRenderShape(BlockState state) {
                    return RenderShape.INVISIBLE;
                }
            });

    // 5. 活板门 (Trapdoor)
    public static final Block BARRIER_TRAPDOOR = register("barrier_trapdoor",
            (p) -> new TrapDoorBlock(BARRIER_SET, p){
                @Override
                protected RenderShape getRenderShape(BlockState state) {
                    return RenderShape.INVISIBLE;
                }
            });

    public static final Block BARRIER_PRESSURE_PLATE = register("barrier_pressure_plate",
            (p) -> new PressurePlateBlock(BARRIER_SET, p){
                @Override
                protected RenderShape getRenderShape(BlockState state) {
                    return RenderShape.INVISIBLE;
                }
            });

    public static final Block BARRIER_BUTTON = register("barrier_button",
            (p) -> new ButtonBlock(BARRIER_SET, 30, p){
                @Override
                protected RenderShape getRenderShape(BlockState state) {
                    return RenderShape.INVISIBLE;
                }
            });

    // 3. 墙 (Wall)
    public static final Block BARRIER_WALL = register("barrier_wall", (p) -> new WallBlock(p){
        @Override
        protected RenderShape getRenderShape(BlockState state) {
            return RenderShape.INVISIBLE;
        }
    });

    public static final Block BARRIER_LAYER = register("barrier_layer", BarrierLayerBlock::new);

    private static Block register(String name, Function<BlockBehaviour.Properties, Block> factory) {
        Identifier id = Identifier.fromNamespaceAndPath(MoreBarrier.MOD_ID, name);
        ResourceKey<Block> blockKey = ResourceKey.create(Registries.BLOCK, id);
        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, id);

        // 统一屏障属性配置
        BlockBehaviour.Properties props = BlockBehaviour.Properties.ofFullCopy(Blocks.BARRIER)
                .mapColor(MapColor.COLOR_RED)
                .setId(blockKey);

        Block block = factory.apply(props);


        // 注册
        Registry.register(BuiltInRegistries.BLOCK, id, block);
        Registry.register(BuiltInRegistries.ITEM, id, new BlockItem(block, new Item.Properties().setId(itemKey)));

        BLOCKS_FOR_TAB.add(block);
        return block;
    }

    private static void registerToTab(){
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.BUILDING_BLOCKS).register((entries) -> {
            BLOCKS_FOR_TAB.forEach(entries::accept);
        });
    }

    public static final ResourceKey<CreativeModeTab> MORE_BARRIER_TAB_KEY = ResourceKey.create(
            BuiltInRegistries.CREATIVE_MODE_TAB.key(), Identifier.fromNamespaceAndPath(MoreBarrier.MOD_ID, "creative_tab")
    );
    public static final CreativeModeTab MORE_BARRIER_TAB = FabricItemGroup.builder()
            .icon(() -> new ItemStack(ModBlocks.BARRIER_STAIRS))
            .title(Component.translatable("itemGroup.more-barrier"))
            .displayItems((params, output) -> {

                BLOCKS_FOR_TAB.forEach(output::accept);
            })
            .build();

    public static void initialize() {

        registerToTab();
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, MORE_BARRIER_TAB_KEY, MORE_BARRIER_TAB);

    }
}