package com.github.justinplayzz.init.BayouBlues.util;

import com.github.justinplayzz.init.BareSmallLogBlock;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MiningToolItem;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Property;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.Random;
import java.util.function.Supplier;

public class SmallLogBlock extends BareSmallLogBlock {
    public static final BooleanProperty HAS_LEAVES = BooleanProperty.of("has_leaves");
    private static final int UP_MASK;
    private static final int DOWN_MASK;
    private static final int NORTH_MASK;
    private static final int EAST_MASK;
    private static final int SOUTH_MASK;
    private static final int WEST_MASK;
    protected final VoxelShape[] collisionShapes;
    protected final VoxelShape[] boundingShapes;
    private final Object2IntMap<BlockState> SHAPE_INDEX_CACHE = new Object2IntOpenHashMap();
    private final Block leaves;
    private final Supplier<Block> stripped;

    public SmallLogBlock(Block leaves, Supplier<Block> stripped, AbstractBlock.Settings settings) {
        super(stripped, settings);
        this.setDefaultState((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.getStateManager().getDefaultState()).with(UP, false)).with(DOWN, false)).with(WEST, false)).with(EAST, false)).with(NORTH, false)).with(SOUTH, false)).with(WATERLOGGED, false)).with(HAS_LEAVES, false));
        this.collisionShapes = this.createShapes(5.0D);
        this.boundingShapes = this.createShapes(5.0D);
        this.leaves = leaves;
        this.stripped = stripped;
    }

    @Environment(EnvType.CLIENT)
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if ((Boolean)state.get(HAS_LEAVES)) {
            Blocks.OAK_LEAVES.randomDisplayTick(state, world, pos, random);
        }

    }

    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult result) {
        ItemStack held = player.getStackInHand(hand);
        BlockState previous;
        if (held.getCount() >= 1 && held.getItem() == Item.BLOCK_ITEMS.get(this.leaves) && !(Boolean)state.get(HAS_LEAVES)) {
            if (!player.isCreative()) {
                held.decrement(1);
            }

            BlockSoundGroup sounds = this.leaves.getDefaultState().getSoundGroup();
            world.playSound(player, pos, sounds.getPlaceSound(), SoundCategory.BLOCKS, (sounds.getVolume() + 1.0F) / 2.0F, sounds.getPitch() * 0.8F);
            previous = state;
            state = (BlockState)state.with(HAS_LEAVES, true);
            if ((Boolean)state.get(UP) && world.getBlockState(pos.up()).getBlock() instanceof LeavesBlock) {
                state = (BlockState)state.with(UP, false);
            }

            if ((Boolean)state.get(DOWN) && world.getBlockState(pos.down()).getBlock() instanceof LeavesBlock) {
                state = (BlockState)state.with(DOWN, false);
            }

            if ((Boolean)state.get(WEST) && world.getBlockState(pos.west()).getBlock() instanceof LeavesBlock) {
                state = (BlockState)state.with(WEST, false);
            }

            if ((Boolean)state.get(EAST) && world.getBlockState(pos.east()).getBlock() instanceof LeavesBlock) {
                state = (BlockState)state.with(EAST, false);
            }

            if ((Boolean)state.get(NORTH) && world.getBlockState(pos.north()).getBlock() instanceof LeavesBlock) {
                state = (BlockState)state.with(NORTH, false);
            }

            if ((Boolean)state.get(SOUTH) && world.getBlockState(pos.south()).getBlock() instanceof LeavesBlock) {
                state = (BlockState)state.with(SOUTH, false);
            }

            world.setBlockState(pos, pushEntitiesUpBeforeBlockChange(previous, state, world, pos));
            return ActionResult.SUCCESS;
        } else {
            if (this.stripped != null && held.getItem() instanceof MiningToolItem) {
                MiningToolItem tool = (MiningToolItem)held.getItem();
                if (tool.isEffectiveOn(state) || tool.getMiningSpeedMultiplier(held, state) > 1.0F) {
                    world.playSound(player, pos, SoundEvents.ITEM_AXE_STRIP, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    if (!world.isClient) {
                        previous = (BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((Block)this.stripped.get()).getDefaultState().with(BareSmallLogBlock.UP, (Boolean)state.get(BareSmallLogBlock.UP))).with(BareSmallLogBlock.DOWN, (Boolean)state.get(BareSmallLogBlock.DOWN))).with(BareSmallLogBlock.NORTH, (Boolean)state.get(BareSmallLogBlock.NORTH))).with(BareSmallLogBlock.SOUTH, (Boolean)state.get(BareSmallLogBlock.SOUTH))).with(BareSmallLogBlock.EAST, (Boolean)state.get(BareSmallLogBlock.EAST))).with(BareSmallLogBlock.WEST, (Boolean)state.get(BareSmallLogBlock.WEST))).with(BareSmallLogBlock.WATERLOGGED, (Boolean)state.get(BareSmallLogBlock.WATERLOGGED))).with(HAS_LEAVES, (Boolean)state.get(HAS_LEAVES));
                        world.setBlockState(pos, previous);
                        held.damage(1, player, (consumedPlayer) -> {
                            consumedPlayer.sendToolBreakStatus(hand);
                        });
                    }

                    return ActionResult.SUCCESS;
                }
            }

            return ActionResult.FAIL;
        }
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(new Property[]{HAS_LEAVES});
    }

    private boolean shouldConnectTo(BlockState state, boolean solid, boolean leaves) {
        Block block = state.getBlock();
        return solid || !leaves && block instanceof LeavesBlock || block instanceof BareSmallLogBlock;
    }

    public BlockState getNeighborUpdateState(BlockState state, Direction fromDirection, BlockState neighbor, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if ((Boolean)state.get(WATERLOGGED)) {
            world.getFluidTickScheduler().schedule(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }

        boolean leaves = (Boolean)state.get(HAS_LEAVES);
        boolean up = fromDirection == Direction.UP && this.shouldConnectTo(neighbor, neighbor.isSideSolidFullSquare(world, neighborPos, Direction.DOWN), leaves) || (Boolean)state.get(UP);
        boolean down = fromDirection == Direction.DOWN && this.shouldConnectTo(neighbor, neighbor.isSideSolidFullSquare(world, neighborPos, Direction.UP), leaves) || (Boolean)state.get(DOWN);
        boolean north = fromDirection == Direction.NORTH && this.shouldConnectTo(neighbor, neighbor.isSideSolidFullSquare(world, neighborPos, Direction.SOUTH), leaves) || (Boolean)state.get(NORTH);
        boolean east = fromDirection == Direction.EAST && this.shouldConnectTo(neighbor, neighbor.isSideSolidFullSquare(world, neighborPos, Direction.WEST), leaves) || (Boolean)state.get(EAST);
        boolean south = fromDirection == Direction.SOUTH && this.shouldConnectTo(neighbor, neighbor.isSideSolidFullSquare(world, neighborPos, Direction.NORTH), leaves) || (Boolean)state.get(SOUTH);
        boolean west = fromDirection == Direction.WEST && this.shouldConnectTo(neighbor, neighbor.isSideSolidFullSquare(world, neighborPos, Direction.EAST), leaves) || (Boolean)state.get(WEST);
        return (BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)state.with(UP, up)).with(DOWN, down)).with(NORTH, north)).with(EAST, east)).with(SOUTH, south)).with(WEST, west);
    }

    private int getShapeIndex(BlockState requested) {
        return this.SHAPE_INDEX_CACHE.computeIntIfAbsent(requested, (state) -> {
            int mask = 0;
            if ((Boolean)state.get(UP)) {
                mask |= UP_MASK;
            }

            if ((Boolean)state.get(DOWN)) {
                mask |= DOWN_MASK;
            }

            if ((Boolean)state.get(NORTH)) {
                mask |= NORTH_MASK;
            }

            if ((Boolean)state.get(EAST)) {
                mask |= EAST_MASK;
            }

            if ((Boolean)state.get(SOUTH)) {
                mask |= SOUTH_MASK;
            }

            if ((Boolean)state.get(WEST)) {
                mask |= WEST_MASK;
            }

            return mask;
        });
    }

    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        return (Boolean)state.get(HAS_LEAVES) ? VoxelShapes.fullCube() : this.boundingShapes[this.getShapeIndex(state)];
    }

    public VoxelShape getCollisionShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        return (Boolean)state.get(HAS_LEAVES) ? VoxelShapes.fullCube() : this.collisionShapes[this.getShapeIndex(state)];
    }

    public VoxelShape getCullingShape(BlockState state, BlockView world, BlockPos pos) {
        return this.collisionShapes[this.getShapeIndex(state)];
    }

    public VoxelShape getVisualShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return this.collisionShapes[this.getShapeIndex(state)];
    }

    static {
        UP_MASK = 1 << Direction.UP.ordinal();
        DOWN_MASK = 1 << Direction.DOWN.ordinal();
        NORTH_MASK = 1 << Direction.NORTH.ordinal();
        EAST_MASK = 1 << Direction.EAST.ordinal();
        SOUTH_MASK = 1 << Direction.SOUTH.ordinal();
        WEST_MASK = 1 << Direction.WEST.ordinal();
    }
}
