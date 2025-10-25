package keeptrident.forge;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import keeptrident.KPTrident;

@Mod(KPTrident.MOD_ID)
public final class KPTridentForge {
    public KPTridentForge() {
        // Run our common setup.
        KPTrident.init();
    }
}
