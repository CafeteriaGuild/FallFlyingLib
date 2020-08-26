package net.adriantodt.fallflyinglib.impl;

import net.adriantodt.fallflyinglib.FallFlyingAbility;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.LivingEntity;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class FallFlyingLibInternals {
    private static final Set<Function<LivingEntity, FallFlyingAbility>> accessors = ConcurrentHashMap.newKeySet();

    public static void registerAccessor(Function<LivingEntity, FallFlyingAbility> accessor) {
        accessors.add(accessor);
    }

    public static boolean isFallFlyingAllowed(LivingEntity livingEntity) {
        for (Function<LivingEntity, FallFlyingAbility> accessor : accessors) {
            if (accessor.apply(livingEntity).allowFallFlying()) {
                return true;
            }
        }
        return false;
    }

    @Environment(EnvType.CLIENT)
    public static boolean shouldHideElytra(LivingEntity livingEntity) {
        for (Function<LivingEntity, FallFlyingAbility> accessor : accessors) {
            if (accessor.apply(livingEntity).shouldHideElytra()) {
                return true;
            }
        }
        return false;
    }
}
