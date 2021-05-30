package net.adriantodt.fallflyinglib.impl.support;

import io.github.ladysnake.pal.AbilitySource;
import io.github.ladysnake.pal.Pal;
import net.adriantodt.fallflyinglib.event.FallFlyingCallback;
import net.adriantodt.fallflyinglib.event.PreFallFlyingCallback;
import net.adriantodt.fallflyinglib.mixin.LivingEntityAccessor;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

import static net.adriantodt.fallflyinglib.FallFlyingLib.ABILITY;

public class VanillaSupport {
    public static final Identifier SOURCE_ID = new Identifier("minecraft", "chestplate_elytra");
    public static final AbilitySource SOURCE = Pal.getAbilitySource(SOURCE_ID);

    public static void configure() {
        PreFallFlyingCallback.EVENT.register(VanillaSupport::preTick);
        FallFlyingCallback.EVENT.register(VanillaSupport::postTick);
    }

    private static boolean shouldDamage(PlayerEntity player, ItemStack itemStack) {
        return itemStack.getItem() == Items.ELYTRA
            && ElytraItem.isUsable(itemStack)
            && (((LivingEntityAccessor) player).getRoll() + 1) % 20 == 0;
    }

    private static void preTick(PlayerEntity player) {
        if (player.world.isClient) {
            return;
        }
        ItemStack itemStack = player.getEquippedStack(EquipmentSlot.CHEST);
        if (itemStack.getItem() == Items.ELYTRA && ElytraItem.isUsable(itemStack)) {
            Pal.grantAbility(player, ABILITY, SOURCE);
        } else {
            Pal.revokeAbility(player, ABILITY, SOURCE);
        }
    }

    private static void postTick(PlayerEntity player) {
        if (player.world.isClient || !SOURCE.grants(player, ABILITY)) {
            return;
        }
        ItemStack itemStack = player.getEquippedStack(EquipmentSlot.CHEST);
        if (shouldDamage(player, itemStack)) {
            itemStack.damage(1, player, le -> le.sendEquipmentBreakStatus(EquipmentSlot.CHEST));
        }
    }
}
