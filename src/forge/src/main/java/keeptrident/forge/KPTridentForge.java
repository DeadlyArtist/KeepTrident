package keeptrident.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import keeptrident.KPTrident;

@Mod(KPTrident.MOD_ID)
public final class KPTridentForge {
    public KPTridentForge() {
        // Submit our event bus to let Architectury API register our content on the right time.
        EventBuses.registerModEventBus(KPTrident.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());

        // Run our common setup.
        KPTrident.init();
    }
}
