package keeptrident.fabric;

import keeptrident.fabric.utils.LoaderImpl;
import keeptrident.utils.Loader;
import net.fabricmc.api.ModInitializer;

import keeptrident.KPTrident;

public final class KPTridentFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        // Run our common setup.
        KPTrident.init();
        RegistryInitManagerFabric.init();
    }

    public static void preInitialize() {
        Loader._impl = new LoaderImpl();
    }
}
