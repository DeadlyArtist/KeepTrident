package keeptrident.mixin;

import keeptrident.component.KPComponents;
import net.minecraft.component.DataComponentTypes;
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
        if (item instanceof TridentItem && world instanceof ServerWorld serverWorld && self.contains(KPComponents.THROWN)) {
            var thrownUuid = self.get(KPComponents.THROWN);
            var thrown = serverWorld.getEntity(thrownUuid);
            if (thrown == null) {
                self.remove(KPComponents.THROWN);
                self.remove(KPComponents.THROWN_TICKS);
            } else {
                var thrownTicks = self.get(KPComponents.THROWN_TICKS).intValue();
                thrownTicks++;
                self.set(KPComponents.THROWN_TICKS, thrownTicks);
                if (thrownTicks > 20 * 20) { // 20 seconds
                    self.remove(KPComponents.THROWN);
                    self.remove(KPComponents.THROWN_TICKS);
                }
            }
        }
    }
}
