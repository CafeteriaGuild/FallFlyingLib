package net.adriantodt.fallflyinglib.impl.mod;

import net.adriantodt.fallflyinglib.impl.FallFlyingPlayerEntity;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

import java.util.UUID;

public class FFLClient implements ClientModInitializer {
    private static final Logger LOGGER = LogManager.getLogger();

    private static final KeyBinding kbToggle = new KeyBinding(
        "key.fallflyinglib.toggle",
        InputUtil.Type.KEYSYM,
        GLFW.GLFW_KEY_H,
        "key.categories.gameplay"
    );

    @Override
    public void onInitializeClient() {
        KeyBindingHelper.registerKeyBinding(kbToggle);
        ClientPlayNetworking.registerGlobalReceiver(FFLCommon.FFL_UPDATE_PACKET, this::handleUpdatePacket);
        ClientPlayNetworking.registerGlobalReceiver(FFLCommon.FFL_LOCK_PACKET, this::handleLockPacket);
        ClientTickEvents.END_CLIENT_TICK.register(this::onClientEndTick);
    }

    private void onClientEndTick(MinecraftClient client) {
        FallFlyingPlayerEntity data = client.player instanceof FallFlyingPlayerEntity
            ? ((FallFlyingPlayerEntity) client.player)
            : null;

        while (kbToggle.wasPressed()) {
            if (data != null) {
                data.ffl_toggleFallFlyingLock();
            }
        }
    }

    private void handleUpdatePacket(MinecraftClient c, ClientPlayNetworkHandler h, PacketByteBuf p, PacketSender s) {
        UUID uuid = p.readUuid();
        boolean enabled = p.readBoolean();

        c.execute(() -> {
            ClientWorld world = c.world;
            if (world != null) {
                PlayerEntity player = world.getPlayerByUuid(uuid);
                if (player == null) {
                    LOGGER.warn("Could not sync player {} because the player is null.", uuid);
                } else if (player instanceof FallFlyingPlayerEntity) {
                    ((FallFlyingPlayerEntity) player).ffl_setFallFlyingAbilityEnabled(enabled);
                } else {
                    LOGGER.warn("Player {} could not be synced because the mixin wasn't loaded.", player);
                }
            } else {
                LOGGER.warn("Could not sync player {} because the world wasn't set.", uuid);
            }
        });
    }

    private void handleLockPacket(MinecraftClient c, ClientPlayNetworkHandler h, PacketByteBuf p, PacketSender s) {
        boolean value = p.readBoolean();
        c.execute(() -> {
            ClientPlayerEntity player = c.player;
            if (player != null) {
                if (player instanceof FallFlyingPlayerEntity) {
                    ((FallFlyingPlayerEntity) player).ffl_setFallFlyingLock(value);
                } else {
                    LOGGER.warn("Player {} could not be synced because the mixin wasn't loaded.", player);
                }
            } else {
                LOGGER.warn("Could not sync player's lock settings because the player wasn't set.");
            }
        });
    }
}
