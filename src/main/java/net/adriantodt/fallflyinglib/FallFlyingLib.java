package net.adriantodt.fallflyinglib;

import io.github.ladysnake.pal.Pal;
import io.github.ladysnake.pal.PlayerAbility;
import net.adriantodt.fallflyinglib.impl.FallFlyingAbilityTracker;
import net.adriantodt.fallflyinglib.impl.support.LegacySupport;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

import java.util.function.Function;

/**
 * FallFlyingLib's main class. Provides a method to register a accessor {@link FallFlyingAbility}.
 *
 * <p> The implementation of {@link FallFlyingAbility} is let to the user. It can be implemented as a
 * Entity Component from Cardinal Components API.
 */
public final class FallFlyingLib {
    public static final Identifier ABILITY_ID = new Identifier("fallflyinglib", "fallflying");
    public static final PlayerAbility ABILITY = Pal.registerAbility(ABILITY_ID, FallFlyingAbilityTracker::new);

    /**
     * Registers a way to access a {@link FallFlyingAbility}.
     *
     * @param accessor the accessor, might be a lambda.
     */
    @Deprecated
    public static void registerAccessor(Function<LivingEntity, FallFlyingAbility> accessor) {
        LegacySupport.registerAccessor(accessor);
    }
}
