package com.github.justinplayzz.init.BayouBlues;

import net.minecraft.block.PressurePlateBlock;

public class BBPressurePlateBlock extends PressurePlateBlock {

    public BBPressurePlateBlock(Settings settings) {
        super(ActivationRule.EVERYTHING, settings);
    }

    public BBPressurePlateBlock(ActivationRule rule, Settings settings) {
        super(rule, settings);
    }

}
