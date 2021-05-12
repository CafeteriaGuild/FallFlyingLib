package net.adriantodt.fallflyinglib.mixin;

import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Entity.class)
public interface LivingEntityAccessor {
    @Invoker
    boolean callGetFlag(int index);

    @Invoker
    void callSetFlag(int index, boolean value);

    @Invoker
    boolean callHasVehicle();

    @Invoker
    boolean callIsOnGround();
}
