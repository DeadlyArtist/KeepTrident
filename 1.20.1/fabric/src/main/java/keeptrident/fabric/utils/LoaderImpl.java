package keeptrident.fabric.utils;

import keeptrident.utils.ILoader;
import net.fabricmc.loader.api.FabricLoader;

public class LoaderImpl implements ILoader {
    @Override
    public LoaderType getLoaderType() {
        return LoaderType.Fabric;
    }

    @Override
    public boolean isModLoaded(String id) {
        return FabricLoader.getInstance().isModLoaded(id);
    }
}
