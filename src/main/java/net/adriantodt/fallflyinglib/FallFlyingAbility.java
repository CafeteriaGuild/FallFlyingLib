package net.adriantodt.fallflyinglib;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/**
 * An interface for allowing not-elytra fall flying.
 *
 * <p> Additionally, it adds an clientside endpoint to hide the Minecraft's Elytra.
 */
@Deprecated
public interface FallFlyingAbility {
    /**
     * Allows the player to start fall flying.
     * @return true if the player has fall flying capabilities.
     */
    boolean allowFallFlying();

    /**
     * Hides the cape, if the player is wearing one.
     * <p>
     * Deprecated, use VanillaVanity.
     *
     * @return true if the Elytra should not be shown.
     */
    @Environment(EnvType.CLIENT)
    default boolean shouldHideCape() {
        return false;
    }

    /**
     * Deprecated, replaced by {@link FallFlyingAbility#shouldHideCape()}.
     *
     * @return by default, never returns.
     */
    @Environment(EnvType.CLIENT)
    default boolean shouldHideElytra() {
        return false;
    }
}
