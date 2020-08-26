package net.adriantodt.fallflyinglib;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/**
 *
 */
public interface FallFlyingAbility {
    boolean allowFallFlying();

    @Environment(EnvType.CLIENT)
    boolean shouldHideElytra();
}
