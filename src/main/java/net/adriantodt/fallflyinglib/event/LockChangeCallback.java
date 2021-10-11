package net.adriantodt.fallflyinglib.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public interface LockChangeCallback {
    Event<LockChangeCallback> EVENT = EventFactory.createArrayBacked(LockChangeCallback.class, (status) -> {
    }, (callbacks) -> (status) -> {
        for (LockChangeCallback callback : callbacks) {
            callback.interact(status);
        }
    });

    void interact(Boolean lockStatus);
}
