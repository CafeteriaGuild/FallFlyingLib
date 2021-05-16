package net.adriantodt.fallflyinglib.impl;

public interface FallFlyingPlayerEntity {
    boolean ffl_isFallFlyingAbilityEnabled();

    void ffl_setFallFlyingAbilityEnabled(boolean enabled);

    void ffl_toggleFallFlyingLock();

    boolean ffl_getFallFlyingLock();

    void ffl_setFallFlyingLock(boolean value);
}
