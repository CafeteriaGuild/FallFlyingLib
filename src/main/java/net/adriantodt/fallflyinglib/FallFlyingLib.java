package net.adriantodt.fallflyinglib;

import io.github.ladysnake.pal.Pal;
import io.github.ladysnake.pal.PlayerAbility;
import net.adriantodt.fallflyinglib.impl.FallFlyingAbilityTracker;
import net.minecraft.util.Identifier;

/**
 * FallFlyingLib's main class. Provides a {@link net.minecraft.entity.player.PlayerEntity} for Fall Flying.
 */
public final class FallFlyingLib {
    public static final Identifier ABILITY_ID = new Identifier("fallflyinglib", "fallflying");
    public static final PlayerAbility ABILITY = Pal.registerAbility(ABILITY_ID, FallFlyingAbilityTracker::new);
}
