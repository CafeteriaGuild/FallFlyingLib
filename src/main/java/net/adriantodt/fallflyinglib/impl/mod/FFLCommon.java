package net.adriantodt.fallflyinglib.impl.mod;

import net.adriantodt.fallflyinglib.FallFlyingLib;
import net.adriantodt.fallflyinglib.impl.FallFlyingPlayerEntity;
import net.adriantodt.fallflyinglib.impl.support.VanillaSupport;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class FFLCommon implements ModInitializer {
    public static Identifier FFL_UPDATE_PACKET = new Identifier("fallflyinglib", "update_fall_flying");
    public static Identifier FFL_LOCK_PACKET = new Identifier("fallflyinglib", "fall_flying_lock");

    public static void handleLockPacket(
        MinecraftServer server,
        ServerPlayerEntity player,
        ServerPlayNetworkHandler handler,
        PacketByteBuf buf,
        PacketSender responseSender
    ) {
        boolean value = buf.readBoolean();
        server.execute(() -> ((FallFlyingPlayerEntity) player).ffl_setFallFlyingLock(value));
    }

    @SuppressWarnings("InstantiationOfUtilityClass")
    @Override
    public void onInitialize() {
        new FallFlyingLib();
        VanillaSupport.configure();
        ServerPlayNetworking.registerGlobalReceiver(FFL_LOCK_PACKET, FFLCommon::handleLockPacket);
    }
}
