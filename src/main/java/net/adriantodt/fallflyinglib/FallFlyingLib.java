package net.adriantodt.fallflyinglib;

import net.adriantodt.fallflyinglib.impl.FallFlyingLibInternals;
import net.minecraft.entity.LivingEntity;

import java.util.function.Function;

/**
 * FallFlyingLib's main class. Provides a method to register a accessor {@link FallFlyingAbility}.
 *
 * <p> The implementation of {@link FallFlyingAbility} is let to the user. It can be implemented as a
 * Entity Compontent from Cardinal Components API.
 */
public final class FallFlyingLib {
    /**
     * Registers a way to access a {@link FallFlyingAbility}.
     * @param accessor the accessor, might be a lambda.
     */
    public static void registerAccessor(Function<LivingEntity, FallFlyingAbility> accessor) {
        FallFlyingLibInternals.registerAccessor(accessor);
    }
}
