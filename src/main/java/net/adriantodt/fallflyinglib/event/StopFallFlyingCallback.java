package net.adriantodt.fallflyinglib.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;

public interface StopFallFlyingCallback {
    Event<StopFallFlyingCallback> EVENT = EventFactory.createArrayBacked(
        StopFallFlyingCallback.class,
        (player, reason, cancel) -> {
        },
        (callbacks) -> (player, reason, cancel) -> {
            for (StopFallFlyingCallback callback : callbacks) {
                callback.onStop(player, reason, cancel);
            }
        });

    void onStop(PlayerEntity player, Reason reason, Runnable cancel);

    enum Reason {
        CONDITIONS_NOT_MET,
        NO_SOURCE
    }
}
