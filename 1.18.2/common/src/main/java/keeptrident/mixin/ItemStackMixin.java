package keeptrident.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.item.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    @Unique
    private ItemStack self = (ItemStack) (Object) this;

    @Inject(method = "inventoryTick", at = @At("HEAD"))
    private void inventoryTick(World world, Entity entity, int slot, boolean selected, CallbackInfo ci) {
        var item = self.getItem();
        if (world instanceof ServerWorld serverWorld && self.hasNbt()) {
            var nbt = self.getNbt();
            if (nbt.contains("trident_thrown")) {
                var thrownUuid = nbt.getUuid("trident_thrown");
                var thrown = serverWorld.getEntity(thrownUuid);
                if (thrown == null) {
                    nbt.remove("trident_thrown");
                } else {
                    var thrownTicks = nbt.getInt("trident_thrown_ticks");
                    thrownTicks++;
                    nbt.putInt("trident_thrown_ticks", thrownTicks);
                    if (thrownTicks > 20 * 20) { // 20 seconds
                        nbt.remove("trident_thrown");
                    }
                }
            }
        }
    }
}
