package net.adriantodt.fallflyinglib.impl;

import net.adriantodt.fallflyinglib.event.FallFlyingCallback;
import net.adriantodt.fallflyinglib.event.PreFallFlyingCallback;
import net.adriantodt.fallflyinglib.event.StopFallFlyingCallback;
import net.adriantodt.fallflyinglib.event.StopFallFlyingCallback.Reason;
import net.adriantodt.fallflyinglib.mixin.LivingEntityAccessor;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;

import java.util.concurrent.atomic.AtomicBoolean;

public class FallFlyingPipeline {
    private static final int FALL_FLYING_FLAG = 7;

    private final PlayerEntity player;
    private final LivingEntityAccessor accessor;
    private final FallFlyingPlayerEntity data;

    public FallFlyingPipeline(PlayerEntity player) {
        this.player = player;
        this.accessor = (LivingEntityAccessor) player;
        this.data = ((FallFlyingPlayerEntity) player);
    }

    // @Redirect main method

    public void tick() {
        // This should be called regardless if player is flying or not.
        // It is used to implement Vanilla and FFLv1 support.
        PreFallFlyingCallback.EVENT.invoker().preTick(player);

        // Short circuiting. According to Minecraft, we just need to enforce that the flag is false.
        // TODO Check if this can be skipped, since we're setting the flag into itself.
        if (!getFallFlyingFlag()) {
            setFallFlyingFlag(false);
            return;
        }

        // Check conditions, then call the appropriate method.
        // If the method returns false, player is not fall flying anymore.
        boolean flag = (checkConditions() || stopFallFly(Reason.CONDITIONS_NOT_MET)) && (canFallFly() || stopFallFly(Reason.NO_SOURCE));

        // Generate a PostTick event if we're fall flying.
        // It is used to implement Vanilla support.
        if (flag) {
            FallFlyingCallback.EVENT.invoker().postTick(player);
        }

        // According to Minecraft, we need to enforce flag according, once per tick.
        if (!player.world.isClient) {
            setFallFlyingFlag(flag);
        }
    }

    // Exploitable methods

    public boolean checkConditions() {
        // This method is called to check if we can start or keep fall flying.
        // It's implementation comes from PlayerEntity#checkFallFlying.

        return !data.ffl_getFallFlyingLock()
            && !accessor.callIsOnGround()
            && !accessor.callHasVehicle()
            && !player.hasStatusEffect(StatusEffects.LEVITATION);
    }

    public boolean canFallFly() {
        // This method is called to check only if we can keep fall flying. Basically, the Elytra check.

        return data.ffl_isFallFlyingAbilityEnabled(); // Don't mixin it unless strictly necessary.
    }

    public boolean stopFallFly(Reason reason) {
        // This method is called if the fall flying condition keep fall flying are false.

        AtomicBoolean bool = new AtomicBoolean();
        StopFallFlyingCallback.EVENT.invoker().onStop(player, reason, () -> bool.set(true));
        return bool.get();
    }

    // Flag getting and setting

    public boolean getFallFlyingFlag() {
        return accessor.callGetFlag(FALL_FLYING_FLAG);
    }

    public void setFallFlyingFlag(boolean value) {
        accessor.callSetFlag(FALL_FLYING_FLAG, value);
    }

}
