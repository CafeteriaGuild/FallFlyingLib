package net.adriantodt.fallflyinglib;

import net.adriantodt.fallflyinglib.impl.FallFlyingLibInternals;
import net.minecraft.entity.LivingEntity;

import java.util.function.Function;

public final class FallFlyingLib {
    public static void registerAccessor(Function<LivingEntity, FallFlyingAbility> accessor) {
        FallFlyingLibInternals.registerAccessor(accessor);
    }
}
