package com.github.justinplayzz.init.BayouBlues.util;

import com.github.justinplayzz.SkyCore;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.Direction;

public class BBOptiLeavesBlock extends ExtendedLeavesBlock {
    public BBOptiLeavesBlock(AbstractBlock.Settings settings) {
        super(settings);
    }

    @Override
    public boolean isSideInvisible(BlockState state, BlockState neighborState, Direction offset) {
        return SkyCore.getConfigManager().getClientConfig().isOptiLeavesEnabled() && super.isSideInvisible(state, neighborState, offset);
    }
}
