package com.github.justinplayzz.init;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.MaterialColor;
import net.minecraft.block.PillarBlock;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MiningToolItem;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Property;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.function.Supplier;

public class QuarterLogBlock extends PillarBlock {
    public static final EnumProperty<BarkSide> BARK_SIDE = EnumProperty.of("bark_side", QuarterLogBlock.BarkSide.class);
    private final Supplier<Block> stripped;

    public QuarterLogBlock(Supplier<Block> stripped, MaterialColor color, Settings settings) {
        super(settings);
        this.stripped = stripped;
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(new Property[]{BARK_SIDE});
    }

    public BlockState getPlacementState(ItemPlacementContext context) {
        Vec3d pos = context.getHitPos();
        BlockPos blockPos = context.getBlockPos();
        float hitX = (float)(pos.getX() - (double)blockPos.getX());
        float hitY = (float)(pos.getY() - (double)blockPos.getY());
        float hitZ = (float)(pos.getZ() - (double)blockPos.getZ());
        QuarterLogBlock.BarkSide side = QuarterLogBlock.BarkSide.fromHit(context.getSide().getAxis(), hitX, hitY, hitZ);
        return (BlockState)super.getPlacementState(context).with(BARK_SIDE, side);
    }

    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack heldStack = player.getEquippedStack(hand == Hand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
        if (heldStack.isEmpty()) {
            return ActionResult.FAIL;
        } else {
            Item held = heldStack.getItem();
            if (!(held instanceof MiningToolItem)) {
                return ActionResult.FAIL;
            } else {
                MiningToolItem tool = (MiningToolItem)held;
                if (this.stripped == null || !tool.isEffectiveOn(state) && !(tool.getMiningSpeedMultiplier(heldStack, state) > 1.0F)) {
                    return ActionResult.SUCCESS;
                } else {
                    world.playSound(player, pos, SoundEvents.ITEM_AXE_STRIP, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    if (!world.isClient) {
                        BlockState target = (BlockState)((BlockState)((Block)this.stripped.get()).getDefaultState().with(AXIS, (Direction.Axis)state.get(AXIS))).with(BARK_SIDE, (QuarterLogBlock.BarkSide)state.get(BARK_SIDE));
                        world.setBlockState(pos, target);
                        heldStack.damage(1, player, (consumedPlayer) -> {
                            consumedPlayer.sendToolBreakStatus(hand);
                        });
                    }

                    return ActionResult.SUCCESS;
                }
            }
        }
    }

    public static enum BarkSide implements StringIdentifiable {
        SOUTHWEST("southwest"),
        NORTHWEST("northwest"),
        NORTHEAST("northeast"),
        SOUTHEAST("southeast");

        final String name;

        private BarkSide(String name) {
            this.name = name;
        }

        public static QuarterLogBlock.BarkSide fromHit(Direction.Axis axis, float hitX, float hitY, float hitZ) {
            boolean hitEast;
            boolean hitSouth;
            switch(axis) {
                case Y:
                    hitEast = (double)hitX >= 0.5D;
                    hitSouth = (double)hitZ >= 0.5D;
                    break;
                case X:
                    hitEast = (double)hitY <= 0.5D;
                    hitSouth = (double)hitZ >= 0.5D;
                    break;
                default:
                    hitEast = (double)hitX >= 0.5D;
                    hitSouth = (double)hitY >= 0.5D;
            }

            return fromHalves(!hitEast, !hitSouth);
        }

        public static QuarterLogBlock.BarkSide fromHalves(boolean east, boolean south) {
            if (east) {
                return south ? SOUTHEAST : NORTHEAST;
            } else {
                return south ? SOUTHWEST : NORTHWEST;
            }
        }

        public String toString() {
            return this.name;
        }

        public String asString() {
            return this.name;
        }
    }
}
