package com.github.justinplayzz.init;

import com.github.justinplayzz.mixin.InvokerFoliagePlacerType;
import com.github.justinplayzz.mixin.InvokerTrunkPlacerType;
import com.mojang.serialization.Codec;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.foliage.FoliagePlacer;
import net.minecraft.world.gen.foliage.FoliagePlacerType;
import net.minecraft.world.gen.trunk.TrunkPlacer;
import net.minecraft.world.gen.trunk.TrunkPlacerType;

public class PlacerTypes {
    public PlacerTypes() {
    }

    public static <P extends FoliagePlacer> FoliagePlacerType<P> registerFoliagePlacer(String id, Codec<P> codec) {
        return InvokerFoliagePlacerType.callRegister(id, codec);
    }

    public static <P extends TrunkPlacer> TrunkPlacerType<P> registerTrunkPlacer(String id, Codec<P> codec) {
        return InvokerTrunkPlacerType.callRegister(id, codec);
    }

    public static <P extends FoliagePlacer> FoliagePlacerType<P> registerFoliagePlacer(Identifier id, Codec<P> codec) {
        return registerFoliagePlacer(id.toString(), codec);
    }

    public static <P extends TrunkPlacer> TrunkPlacerType<P> registerTrunkPlacer(Identifier id, Codec<P> codec) {
        return registerTrunkPlacer(id.toString(), codec);
    }
}
