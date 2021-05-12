package net.adriantodt.fallflyinglib.mixin;

import net.adriantodt.fallflyinglib.FallFlyingLib;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin {
    @Inject(
        at = @At(value = "HEAD"),
        method = "onSpawn"
    )
    public void fallflyinglib_forceSyncOnSpawn(CallbackInfo info) {
        FallFlyingLib.ABILITY.getTracker((PlayerEntity) (Object) this).refresh(true);
    }
}
