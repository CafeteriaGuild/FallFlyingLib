package net.adriantodt.fallflyinglib.mixin;

import net.adriantodt.fallflyinglib.impl.FallFlyingLibInternals;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @ModifyArg(
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/LivingEntity;setFlag(IZ)V"
        ), method = "initAi", index = 1
    )
    private boolean injectAiFix(boolean value) {
        EntityAccessor accessor = (EntityAccessor) this;
        boolean bl = accessor.callGetFlag(7);

        if (bl && !accessor.callIsOnGround() && !accessor.callHasVehicle()) {
            return FallFlyingLibInternals.isFallFlyingAllowed((LivingEntity) (Object) this) || value;
        }
        return value;
    }
}
