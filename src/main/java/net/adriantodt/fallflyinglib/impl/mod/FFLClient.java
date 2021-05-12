package net.adriantodt.fallflyinglib.impl.mod;

import net.adriantodt.fallflyinglib.impl.FallFlyingPlayerEntity;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;

import java.util.UUID;

public class FFLClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(FFLCommon.FFL_PACKET, this::handlePacket);
    }

    private void handlePacket(MinecraftClient c, ClientPlayNetworkHandler h, PacketByteBuf p, PacketSender s) {
        UUID uuid = p.readUuid();
        boolean enabled = p.readBoolean();

        c.execute(() -> {
            ClientWorld world = c.world;
            if (world != null) {
                PlayerEntity player = world.getPlayerByUuid(uuid);
                if (player instanceof FallFlyingPlayerEntity) {
                    ((FallFlyingPlayerEntity) player).setFallFlyingAbilityEnabled(enabled);
                }
            }
        });
    }
}
