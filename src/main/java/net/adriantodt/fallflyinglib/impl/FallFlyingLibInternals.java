package net.adriantodt.fallflyinglib.impl;

import net.adriantodt.fallflyinglib.FallFlyingAbility;
import net.adriantodt.fallflyinglib.event.FallFlyingTickCallback;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class FallFlyingLibInternals implements ModInitializer {
    /**
     * Setting this overrides the setFlag behaviour.
     */
    public static BiConsumer<LivingEntity, Boolean> SET_IDX7_FLAG = null;

    private static final Set<Function<LivingEntity, FallFlyingAbility>> accessors = ConcurrentHashMap.newKeySet();

    public static void registerAccessor(Function<LivingEntity, FallFlyingAbility> accessor) {
        accessors.add(accessor);
    }

    public static boolean isFallFlyingAllowed(LivingEntity livingEntity) {
        for (Function<LivingEntity, FallFlyingAbility> accessor : accessors) {
            if (accessor.apply(livingEntity).allowFallFlying()) {
                return true;
            }
        }
        return false;
    }

    @Environment(EnvType.CLIENT)
    public static boolean shouldHideCape(LivingEntity livingEntity) {
        for (Function<LivingEntity, FallFlyingAbility> accessor : accessors) {
            if (accessor.apply(livingEntity).shouldHideCape()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onInitialize() {
        accessors.add((e) -> () -> {
            ItemStack itemStack = e.getEquippedStack(EquipmentSlot.CHEST);
            return itemStack.getItem() == Items.ELYTRA && ElytraItem.isUsable(itemStack);
        });

        FallFlyingTickCallback.EVENT.register(e -> {
            ItemStack itemStack = e.getEquippedStack(EquipmentSlot.CHEST);
            if (itemStack.getItem() == Items.ELYTRA && ElytraItem.isUsable(itemStack)) {
                itemStack.damage(1, e, le -> le.sendEquipmentBreakStatus(EquipmentSlot.CHEST));
            }
        });
    }
}
