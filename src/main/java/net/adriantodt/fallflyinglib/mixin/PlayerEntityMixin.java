package net.adriantodt.fallflyinglib.mixin;

import net.adriantodt.fallflyinglib.impl.FallFlyingLibInternals;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntityMixin {
    @Shadow
    public abstract boolean checkFallFlying();

    @Shadow
    public abstract void startFallFlying();

    @Inject(
        at = @At(
            value = "INVOKE_ASSIGN",
            target = "Lnet/minecraft/entity/player/PlayerEntity;getEquippedStack(Lnet/minecraft/entity/EquipmentSlot;)Lnet/minecraft/item/ItemStack;"
        ),
        method = "checkFallFlying",
        cancellable = true
    )
    public void checkFallFlying(CallbackInfoReturnable<Boolean> info) {
        if (FallFlyingLibInternals.isFallFlyingAllowed((LivingEntity) (Object) this)) {
            this.startFallFlying();
            info.setReturnValue(true);
        }
    }
}
