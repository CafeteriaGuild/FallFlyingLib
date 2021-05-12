package net.adriantodt.fallflyinglib.mixin;

import net.adriantodt.fallflyinglib.impl.FallFlyingPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntityMixin implements FallFlyingPlayerEntity {
    protected boolean fallflyinglib$ability;

    @Override
    public boolean isFallFlyingAbilityEnabled() {
        return this.fallflyinglib$ability;
    }

    @Override
    public void setFallFlyingAbilityEnabled(boolean enabled) {
        this.fallflyinglib$ability = enabled;
    }

    @Shadow
    public abstract boolean checkFallFlying();

    @Shadow
    public abstract void startFallFlying();

    @Inject(
        at = @At(value = "HEAD"),
        method = "checkFallFlying",
        cancellable = true
    )
    public void patchCheckFallFlying(CallbackInfoReturnable<Boolean> info) {
        if (this.fallflyinglib$pipeline.checkConditions() && this.fallflyinglib$pipeline.canFallFly()) {
            this.startFallFlying();
            info.setReturnValue(true);
        }
    }
}
