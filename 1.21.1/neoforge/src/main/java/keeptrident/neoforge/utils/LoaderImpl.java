package keeptrident.neoforge.utils;

import keeptrident.utils.ILoader;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.LoadingModList;

public class LoaderImpl implements ILoader {
    @Override
    public ILoader.LoaderType getLoaderType() {
        return ILoader.LoaderType.Fabric;
    }

    @Override
    public boolean isModLoaded(String id) {
        return LoadingModList.get().getModFileById(id) != null;
    }
}