package keeptrident.neoforge.mixin.compat.upgradablecrystaltools;

import com.llamalad7.mixinextras.sugar.Local;
import dev.willyelton.crystal_tools.common.entity.CrystalTridentEntity;
import keeptrident.component.KPComponents;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = CrystalTridentEntity.class, remap = false)
public abstract class CrystalTridentEntityMixin {

    @Unique
    private final CrystalTridentEntity self = (CrystalTridentEntity) (Object) this;

    @Shadow
    protected boolean dealtDamage;
    @Shadow
    protected ItemStack tridentStack;
    @Shadow
    @Final
    private static TrackedData<Byte> ID_LOYALTY;

    @Shadow
    protected abstract boolean tryPickup(PlayerEntity player);

    @Redirect(method = "<init>(Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;)V", at = @At(value = "NEW", target = "Lnet/minecraft/item/ItemStack;"))
    public ItemStack redirectNewStack(ItemConvertible item, @Local ItemStack stack) {
        return stack;
    }

    @Redirect(method = "<init>(Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;)V", at = @At(value = "FIELD", target = "Ldev/willyelton/crystal_tools/common/entity/CrystalTridentEntity;tridentStack:Lnet/minecraft/item/ItemStack;", ordinal = 1))
    public void redirectSetStack(CrystalTridentEntity instance, ItemStack value, @Local ItemStack stack) {
        instance.stack = stack.copy();
    }

    @Inject(method = "tick()V", at = @At("HEAD"))
    private void returnFromVoid(CallbackInfo ci) {
        if (self.getDataTracker().get(ID_LOYALTY) == 0 || dealtDamage) return;

        if (self.getY() <= self.getWorld().getBottomY()) {
            dealtDamage = true;
            self.setVelocity(0, 0, 0);
        }
    }


    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Ldev/willyelton/crystal_tools/common/entity/CrystalTridentEntity;dropStack(Lnet/minecraft/item/ItemStack;F)Lnet/minecraft/entity/ItemEntity;"))
    private ItemEntity redirectDropStack(CrystalTridentEntity instance, ItemStack stack, float v) {
        return null;
    }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;insertStack(Lnet/minecraft/item/ItemStack;)Z"))
    private boolean redirectInsertStack(PlayerInventory instance, ItemStack stack) {
        return true;
    }


    @Inject(method = "age", at = @At("HEAD"), cancellable = true)
    private void injectAge(CallbackInfo ci) {
        self.life++;
        if (self.life >= 20 * 20) { // 20 seconds
            self.discard();
        }
        ci.cancel();
    }

    @Inject(method = "onPlayerCollision", at = @At("HEAD"), cancellable = true)
    private void injectOnPlayerCollision(PlayerEntity player, CallbackInfo ci) {
        var loyalty = self.getDataTracker().get(ID_LOYALTY);
        if (!self.getWorld().isClient && (self.inGround || self.isNoClip()) && self.shake <= 0) {
            if (loyalty <= 0 && self.getOwner() != null) {
                if (tryPickup(player)) {
                    player.sendPickup(self, 1);
                    self.discard();
                }
            } else if (loyalty > 0 && player == self.getOwner()) {
                var inventory = player.getInventory();
                for (DefaultedList<ItemStack> defaultedList : List.of(inventory.main, inventory.offHand)) {
                    for (ItemStack stack : defaultedList) {
                        if (!stack.isEmpty() && stack.contains(KPComponents.THROWN)) {
                            var uuid = stack.get(KPComponents.THROWN);
                            if (uuid.equals(self.getUuid())) {
                                stack.remove(KPComponents.THROWN);
                                stack.remove(KPComponents.THROWN_TICKS);
                            }
                        }
                    }
                }
                self.discard();
            }
        }
        ci.cancel();
    }
}
