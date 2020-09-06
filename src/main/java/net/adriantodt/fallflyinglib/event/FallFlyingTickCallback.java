package net.adriantodt.fallflyinglib.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;

public interface FallFlyingTickCallback {
    Event<FallFlyingTickCallback> EVENT = EventFactory.createArrayBacked(FallFlyingTickCallback.class, (callbacks) -> (entity) -> {
        for (FallFlyingTickCallback callback : callbacks) {
            callback.fallFlyingTick(entity);
        }
    });

    void fallFlyingTick(LivingEntity entity);
}
