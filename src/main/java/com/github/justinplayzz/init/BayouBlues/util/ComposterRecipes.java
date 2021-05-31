package com.github.justinplayzz.init.BayouBlues.util;

import net.minecraft.block.*;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;

public class ComposterRecipes {
    private static void registerCompostableItem(ItemConvertible item, float chance) {
        if (item.asItem() != Items.AIR) {
            ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE.put(item.asItem(), chance);
        }
    }

    public static void registerCompostableBlock(Block block) {
        if (block instanceof LeavesBlock || block instanceof SaplingBlock || block instanceof SeagrassBlock) {
            registerCompostableItem(block, 0.3F);
        } else if (block instanceof FernBlock || block instanceof FlowerBlock) {
            registerCompostableItem(block, 0.65F);
        }
    }
}
