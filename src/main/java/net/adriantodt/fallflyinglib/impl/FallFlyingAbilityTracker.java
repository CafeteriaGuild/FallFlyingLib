package net.adriantodt.fallflyinglib.impl;

import io.github.ladysnake.pal.AbilitySource;
import io.github.ladysnake.pal.PlayerAbility;
import io.github.ladysnake.pal.SimpleAbilityTracker;
import net.adriantodt.fallflyinglib.impl.mod.FFLCommon;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class FallFlyingAbilityTracker extends SimpleAbilityTracker {
    private static final Logger LOGGER = LogManager.getLogger();

    public FallFlyingAbilityTracker(PlayerAbility ability, PlayerEntity player) {
        super(ability, player);
    }

    @Override
    protected void updateState(boolean enabled) {
        ((FallFlyingPlayerEntity) player).ffl_setFallFlyingAbilityEnabled(enabled);
        super.updateState(enabled);
    }

    @Override
    protected void sync() {
        if (player.world.isClient) {
            return;
        }

        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeUuid(player.getUuid());
        buf.writeBoolean(isEnabled());

        Set<ServerPlayerEntity> all = new LinkedHashSet<>(PlayerLookup.tracking(player));
        all.add((ServerPlayerEntity) player);

        for (ServerPlayerEntity player : all) {
            if (player.networkHandler != null) {
                ServerPlayNetworking.send(player, FFLCommon.FFL_UPDATE_PACKET, buf);
            } else {
                LOGGER.warn("Player {} could not be synced because server networking isn't set up yet.", player);
            }
        }
    }

    @Override
    public String toString() {
        return ability.getId() + " {" +
            "sources=" + super.abilitySources.stream().map(AbilitySource::getId).collect(Collectors.toList()) +
            '}';
    }
}
