package keeptrident.forge;

import com.google.common.collect.ImmutableMap;
import keeptrident.utils.Loader;
import keeptrident.utils.XIDs;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public class MixinPluginForge implements IMixinConfigPlugin {

    static {
        KPTridentForge.preInitialize();
    }

    private static final Supplier<Boolean> TRUE = () -> true;

    private static final Map<String, Supplier<Boolean>> CONDITIONS = ImmutableMap.of(
            "keeptrident.forge.mixin.compat.upgradablecrystaltools.CrystalTridentEntityMixin", () -> Loader.isModLoaded(XIDs.UpgradableCrystalTools),
            "keeptrident.forge.mixin.compat.upgradablecrystaltools.CrystalTridentMixin", () -> Loader.isModLoaded(XIDs.UpgradableCrystalTools)
    );

    @Override
    public void onLoad(String s) {

    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String s, String s1) {
        return CONDITIONS.getOrDefault(s1, TRUE).get();
    }

    @Override
    public void acceptTargets(Set<String> set, Set<String> set1) {

    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {

    }

    @Override
    public void postApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {

    }
}
