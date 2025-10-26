package keeptrident.neoforge;

import net.neoforged.fml.common.Mod;

import keeptrident.KPTrident;

@Mod(KPTrident.MOD_ID)
public final class KPTridentNeoForge {
    public KPTridentNeoForge() {
        // Run our common setup.
        KPTrident.init();
    }
}
