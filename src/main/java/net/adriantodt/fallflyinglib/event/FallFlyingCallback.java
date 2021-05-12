package net.adriantodt.fallflyinglib.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;

public interface FallFlyingCallback {
    Event<FallFlyingCallback> EVENT = EventFactory.createArrayBacked(
        FallFlyingCallback.class,
        (player) -> {
        },
        (callbacks) -> (player) -> {
            for (FallFlyingCallback callback : callbacks) {
                callback.postTick(player);
            }
        });

    void postTick(PlayerEntity player);
}
