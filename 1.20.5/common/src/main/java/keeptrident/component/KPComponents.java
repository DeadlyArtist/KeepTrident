package keeptrident.component;

import com.mojang.serialization.Codec;
import keeptrident.KPTrident;
import net.minecraft.component.DataComponentType;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Uuids;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.UnaryOperator;

public class KPComponents {

    public static Map<Identifier, DataComponentType<?>> data = new HashMap<>();

    public static final DataComponentType<UUID> THROWN = register("THROWN", builder -> builder.codec(Uuids.CODEC).packetCodec(Uuids.PACKET_CODEC));
    public static final DataComponentType<Integer> THROWN_TICKS = register("THROWN_TICKS", builder -> builder.codec(Codec.INT).packetCodec(PacketCodecs.INTEGER));


    public static <T> DataComponentType<T> register(String id, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        var result = ((DataComponentType.Builder) builderOperator.apply(DataComponentType.builder())).build();
        data.put(new Identifier(KPTrident.MOD_ID, id.toLowerCase()), result);
        return result;
    }

    public static void init() {

    }
}
