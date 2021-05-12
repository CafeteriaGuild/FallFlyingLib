package net.adriantodt.fallflyinglib.impl.mod;

import net.adriantodt.fallflyinglib.FallFlyingLib;
import net.adriantodt.fallflyinglib.impl.support.VanillaSupport;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

public class FFLCommon implements ModInitializer {
    public static Identifier FFL_PACKET = new Identifier("fallflyinglib", "update_fall_flying");

    @SuppressWarnings("InstantiationOfUtilityClass")
    @Override
    public void onInitialize() {
        new FallFlyingLib();
        VanillaSupport.configure();
    }
}
