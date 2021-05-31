package com.github.justinplayzz.init.BayouBlues.util;

import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

public class TransparentLeavesBlock extends LeavesBlock {
    public TransparentLeavesBlock(Settings settings) {
        super(settings);
    }

    public int getOpacity(BlockState state, BlockView view, BlockPos pos) {
        return 0;
    }
}
