package net.adriantodt.fallflyinglib.mixin;

import net.adriantodt.fallflyinglib.impl.FallFlyingPipeline;
import net.adriantodt.fallflyinglib.impl.FallFlyingPlayerEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntityMixin implements FallFlyingPlayerEntity {
    protected boolean fallflyinglib$ability;
    protected boolean fallflyinglib$lock;

    public PlayerEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    public boolean ffl_isFallFlyingAbilityEnabled() {
        return this.fallflyinglib$ability;
    }

    @Override
    public void ffl_setFallFlyingAbilityEnabled(boolean enabled) {
        this.fallflyinglib$ability = enabled;
    }

    @Shadow
    public abstract boolean checkFallFlying();

    @Shadow
    public abstract void startFallFlying();

    @Shadow
    public abstract void sendMessage(Text message, boolean actionBar);

    @Inject(
        at = @At(value = "HEAD"),
        method = "checkFallFlying",
        cancellable = true
    )
    public void ffl_patchCheckFallFlying(CallbackInfoReturnable<Boolean> info) {
        if (fallflyinglib$pipeline == null) {
            fallflyinglib$pipeline = new FallFlyingPipeline((PlayerEntity) (Object) this);
        }
        if (!fallflyinglib$lock && this.fallflyinglib$pipeline.canFallFly()) {
            this.startFallFlying();
            info.setReturnValue(true);
        } else {
            info.setReturnValue(false);
        }
    }

    @Inject(method = "writeCustomDataToTag", at = @At("RETURN"))
    private void writeCustomDataToTag(CompoundTag tag, CallbackInfo ci) {
        tag.putBoolean("fallflyinglib$lock", fallflyinglib$lock);
    }

    @Inject(method = "readCustomDataFromTag", at = @At(value = "RETURN"))
    private void readCustomDataFromTag(CompoundTag tag, CallbackInfo info) {
        fallflyinglib$lock = tag.getBoolean("fallflyinglib$lock");
    }

    @Override
    public void ffl_toggleFallFlyingLock() {
        if (!fallflyinglib$ability) {
            return;
        }

        this.fallflyinglib$lock = !this.fallflyinglib$lock;
    }

    @Override
    public boolean ffl_getFallFlyingLock() {
        return fallflyinglib$lock;
    }

    @Override
    public void ffl_setFallFlyingLock(boolean value) {
        fallflyinglib$lock = value;
    }
}
