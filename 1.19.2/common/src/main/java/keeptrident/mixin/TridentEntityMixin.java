package keeptrident.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.*;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Objects;

@Mixin(TridentEntity.class)
public abstract class TridentEntityMixin {

    @Unique
    private final TridentEntity self = (TridentEntity) (Object) this;

    @Inject(method = "tick()V", at = @At("HEAD"))
    private void returnFromVoid(CallbackInfo ci) {
        if (self.getDataTracker().get(TridentEntity.LOYALTY) == 0 || self.dealtDamage) return;

        if (self.getY() <= self.getWorld().getBottomY()) {
            self.dealtDamage = true;
            self.setVelocity(0, 0, 0);
        }
    }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/TridentEntity;dropStack(Lnet/minecraft/item/ItemStack;F)Lnet/minecraft/entity/ItemEntity;"))
    private ItemEntity redirectDropStack(TridentEntity instance, ItemStack stack, float v) {
        return null;
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
    private void injectOnPlayerCollision(CallbackInfo ci, @Local PlayerEntity player) {
        var loyalty = self.getDataTracker().get(TridentEntity.LOYALTY);
        if (!self.world.isClient && (self.inGround || self.isNoClip()) && self.shake <= 0) {
            if (loyalty <= 0 && self.getOwner() != null) {
                if (self.tryPickup(player)) {
                    player.sendPickup(self, 1);
                    self.discard();
                }
            } else if (loyalty > 0 && player == self.getOwner()) {
                var inventory = player.getInventory();
                for (DefaultedList<ItemStack> defaultedList : List.of(inventory.main, inventory.offHand)) {
                    for (ItemStack stack : defaultedList) {
                        if (!stack.isEmpty() && stack.hasNbt()) {
                            var nbt = stack.getNbt();
                            if (Objects.equals(nbt.getString("thrown"), self.getUuidAsString())) {
                                nbt.remove("thrown");
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