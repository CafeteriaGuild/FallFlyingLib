package net.adriantodt.fallflyinglib.mixin;

import net.adriantodt.fallflyinglib.impl.FallFlyingPipeline;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    protected FallFlyingPipeline fallflyinglib$pipeline;

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Shadow
    protected abstract void initAi();

    @Redirect(
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/LivingEntity;initAi()V"
        ), method = "tickMovement"
    )
    public void fallflyinglib_flightPipeline(LivingEntity thisObj) {
        if (thisObj instanceof PlayerEntity) {
            if (fallflyinglib$pipeline == null) {
                fallflyinglib$pipeline = new FallFlyingPipeline(((PlayerEntity) thisObj));
            }
            fallflyinglib$pipeline.tick();
        } else {
            this.initAi();
        }
    }
}
