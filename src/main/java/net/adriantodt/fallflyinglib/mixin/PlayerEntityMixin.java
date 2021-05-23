package net.adriantodt.fallflyinglib.mixin;

import net.adriantodt.fallflyinglib.impl.FallFlyingPlayerEntity;
import net.adriantodt.fallflyinglib.impl.mod.FFLCommon;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
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

        boolean value = !this.fallflyinglib$lock;
        this.fallflyinglib$lock = value;

        if (!this.world.isClient) {
            return;
        }

        this.sendMessage(new TranslatableText("text.fallflyinglib.toggle_" + !value), true);
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeBoolean(value);
        ClientPlayNetworking.send(FFLCommon.FFL_LOCK_PACKET, buf);
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
