package net.adriantodt.fallflyinglib.impl.support;

import io.github.ladysnake.pal.AbilitySource;
import io.github.ladysnake.pal.Pal;
import net.adriantodt.fallflyinglib.FallFlyingAbility;
import net.adriantodt.fallflyinglib.event.PreFallFlyingCallback;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import static net.adriantodt.fallflyinglib.FallFlyingLib.ABILITY;

@Deprecated
public class LegacySupport {
    private static final Identifier SOURCE_ID = new Identifier("fallflyinglib", "legacy_compat");
    private static final AbilitySource SOURCE = Pal.getAbilitySource(SOURCE_ID);

    private static final Logger logger = LogManager.getLogger();
    private static final Set<Function<LivingEntity, FallFlyingAbility>> accessors = ConcurrentHashMap.newKeySet();
    private static boolean warning = false;

    public static void configure() {
        PreFallFlyingCallback.EVENT.register(LegacySupport::preTick);
    }

    private static void preTick(PlayerEntity player) {
        if (!player.world.isClient && isFallFlyingAllowed(player) != SOURCE.grants(player, ABILITY)) {
            if (isFallFlyingAllowed(player)) {
                SOURCE.grantTo(player, ABILITY);
            } else {
                SOURCE.revokeFrom(player, ABILITY);
            }
        }
    }

    public static void registerAccessor(Function<LivingEntity, FallFlyingAbility> accessor) {
        accessors.add(accessor);
        if (!warning) {
            warning = true;
            logger.warn(
                "A mod is using FallFlyingLib's Legacy API! This is not supported, and features may not work correctly."
            );
            configure();
        }
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
}
