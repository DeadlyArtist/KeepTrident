package keeptrident.forge;

import keeptrident.forge.utils.LoaderImpl;
import keeptrident.utils.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import keeptrident.KPTrident;

@Mod(KPTrident.MOD_ID)
public final class KPTridentForge {
    public KPTridentForge() {
        // Run our common setup.
        KPTrident.init();
    }

    public static void preInitialize() {
        Loader._impl = new LoaderImpl();
    }
}
