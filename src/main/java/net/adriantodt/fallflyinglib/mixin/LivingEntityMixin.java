package net.adriantodt.fallflyinglib.mixin;

import net.adriantodt.fallflyinglib.impl.FallFlyingLibInternals;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.BiConsumer;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Redirect(
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/LivingEntity;initAi()V"
        ), method = "tickMovement"
    )
    public void replaceInitAi(LivingEntity thisObj) {
        EntityAccessor accessor = (EntityAccessor) thisObj;
        boolean fallFlying = accessor.callGetFlag(7);

        if (fallFlying && !accessor.callIsOnGround() && !accessor.callHasVehicle() && !thisObj.hasStatusEffect(StatusEffects.LEVITATION)) {
            fallFlying = FallFlyingLibInternals.isFallFlyingAllowed(thisObj);
        } else {
            fallFlying = false;
        }

        if (!thisObj.world.isClient) {
            BiConsumer<LivingEntity, Boolean> consumer = FallFlyingLibInternals.SET_IDX7_FLAG;
            if (consumer != null) {
                consumer.accept(thisObj, fallFlying);
            } else {
                accessor.callSetFlag(7, fallFlying);
            }
        }
    }

    @ModifyArg(
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/LivingEntity;setFlag(IZ)V"
        ), method = "initAi", index = 1
    )
    private boolean patchInitAi(boolean value) {
        EntityAccessor accessor = (EntityAccessor) this;
        boolean bl = accessor.callGetFlag(7);

        if (bl && !accessor.callIsOnGround() && !accessor.callHasVehicle()) {
            return FallFlyingLibInternals.isFallFlyingAllowed((LivingEntity) (Object) this) || value;
        }
        return value;
    }
}
