package net.adriantodt.fallflyinglib.mixin;

import net.adriantodt.fallflyinglib.FallFlyingLib;
import net.adriantodt.fallflyinglib.impl.mod.FFLCommon;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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
        fallflyinglib_syncWithClient();
    }

    @Inject(
        at = @At(value = "RETURN"),
        method = "moveToWorld"
    )
    public void fallflyinglib_moveToWorld(ServerWorld destination, CallbackInfoReturnable<Entity> cir) {
        fallflyinglib_syncWithClient();
    }

    private void fallflyinglib_syncWithClient() {
        FallFlyingLib.ABILITY.getTracker((PlayerEntity) (Object) this).refresh(true);

        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeBoolean(fallflyinglib$lock);
        ServerPlayNetworking.send((ServerPlayerEntity) (Object) this, FFLCommon.FFL_LOCK_PACKET, buf);
    }
}
