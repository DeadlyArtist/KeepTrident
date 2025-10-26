package keeptrident.fabric;

import keeptrident.component.KPComponents;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class RegistryInitManagerFabric {
    public static void init() {
        registerComponents();
    }

    public static void registerComponents() {
        KPComponents.data.forEach((id, component) -> {
            Registry.register(Registries.DATA_COMPONENT_TYPE, id, component);
        });
    }
}
