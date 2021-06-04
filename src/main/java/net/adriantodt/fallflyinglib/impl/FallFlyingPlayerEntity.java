package net.adriantodt.fallflyinglib.impl;

/**
 * Interface used for FallFlyingAbilityTracker to control the player.
 * <p>
 * Don't use those methods directly. Use the PAL API instead.
 */
public interface FallFlyingPlayerEntity {
    boolean ffl_isFallFlyingAbilityEnabled();

    void ffl_setFallFlyingAbilityEnabled(boolean enabled);

    void ffl_toggleFallFlyingLock();

    boolean ffl_getFallFlyingLock();

    void ffl_setFallFlyingLock(boolean value);
}
