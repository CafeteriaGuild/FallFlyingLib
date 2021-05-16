package net.adriantodt.fallflyinglib.mixin;

import net.adriantodt.fallflyinglib.FallFlyingLib;
import net.adriantodt.fallflyinglib.impl.mod.FFLCommon;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntityMixin {
    public ServerPlayerEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(
        at = @At(value = "HEAD"),
        method = "onSpawn"
    )
    public void fallflyinglib_forceSyncOnSpawn(CallbackInfo info) {
        FallFlyingLib.ABILITY.getTracker((PlayerEntity) (Object) this).refresh(true);

        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeBoolean(fallflyinglib$lock);

        ServerPlayNetworking.send((ServerPlayerEntity) (Object) this, FFLCommon.FFL_LOCK_PACKET, buf);
    }
}
