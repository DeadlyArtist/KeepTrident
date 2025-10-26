package keeptrident.neoforge;

import keeptrident.KPTrident;
import keeptrident.component.KPComponents;
import net.minecraft.component.DataComponentType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class RegistryInitManagerNeo {
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES = DeferredRegister.createDataComponents(KPTrident.MOD_ID);


    public static void registerComponents() {
        KPComponents.data.forEach((id, component) -> {
            Supplier<DataComponentType<?>> sup = () -> component;
            DATA_COMPONENT_TYPES.register(id.getPath(), sup);
        });
    }

    public static void init() {
        registerComponents();
    }

    public static void register(IEventBus eventBus) {
        DATA_COMPONENT_TYPES.register(eventBus);
    }
}
