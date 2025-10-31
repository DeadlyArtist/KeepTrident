package keeptrident.neoforge;

import keeptrident.neoforge.utils.LoaderImpl;
import keeptrident.utils.Loader;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

import keeptrident.KPTrident;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;

@Mod(KPTrident.MOD_ID)
public final class KPTridentNeoForge {
    public KPTridentNeoForge(IEventBus eventBus, ModContainer container) {
        //NeoForge.EVENT_BUS.register(this); // Only with @SubscribeEvent present

        KPTrident.init();
        RegistryInitManagerNeo.init();

        eventBus.addListener(this::setup);
        RegistryInitManagerNeo.register(eventBus);
    }

    public static void preInitialize() {
        Loader._impl = new LoaderImpl();
    }

    public void setup(final FMLCommonSetupEvent event) {

    }
}
