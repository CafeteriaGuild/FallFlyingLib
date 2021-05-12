package net.adriantodt.fallflyinglib.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;

public interface PreFallFlyingCallback {
    Event<PreFallFlyingCallback> EVENT = EventFactory.createArrayBacked(
        PreFallFlyingCallback.class,
        (player) -> {
        },
        (callbacks) -> (player) -> {
            for (PreFallFlyingCallback callback : callbacks) {
                callback.preTick(player);
            }
        });

    void preTick(PlayerEntity player);
}
