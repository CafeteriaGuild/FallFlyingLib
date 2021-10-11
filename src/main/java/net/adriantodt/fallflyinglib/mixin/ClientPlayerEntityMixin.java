package net.adriantodt.fallflyinglib.mixin;

import net.adriantodt.fallflyinglib.event.LockChangeCallback;
import net.adriantodt.fallflyinglib.impl.mod.FFLCommon;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends PlayerEntityMixin {

    @Shadow
    @Final
    public ClientPlayNetworkHandler networkHandler;

    public ClientPlayerEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(
        at = @At(
            value = "INVOKE_ASSIGN",
            target = "Lnet/minecraft/client/network/ClientPlayerEntity;getEquippedStack(Lnet/minecraft/entity/EquipmentSlot;)Lnet/minecraft/item/ItemStack;"
        ),
        method = "tickMovement"
    )
    public void ffl_patchClientControls(CallbackInfo info) {
        ClientPlayerEntity thisObj = (ClientPlayerEntity) (Object) this;
        if (!fallflyinglib$lock && this.checkFallFlying()) {
            this.networkHandler.sendPacket(new ClientCommandC2SPacket(thisObj, ClientCommandC2SPacket.Mode.START_FALL_FLYING));
        }
    }

    @Redirect(
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/network/ClientPlayNetworkHandler;sendPacket(Lnet/minecraft/network/Packet;)V"
        ),
        method = "tickMovement"
    )
    public void ffl_disableVanilla(ClientPlayNetworkHandler clientPlayNetworkHandler, Packet<?> packet) {
        // NOOP
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
        LockChangeCallback.EVENT.invoker().interact(this.fallflyinglib$lock);
        this.sendMessage(new TranslatableText("text.fallflyinglib.toggle_" + !value), true);
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeBoolean(value);
        ClientPlayNetworking.send(FFLCommon.FFL_LOCK_PACKET, buf);
    }
}
