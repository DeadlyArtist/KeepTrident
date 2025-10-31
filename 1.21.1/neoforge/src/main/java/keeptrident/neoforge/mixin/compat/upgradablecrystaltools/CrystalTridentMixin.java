package keeptrident.neoforge.mixin.compat.upgradablecrystaltools;

import com.llamalad7.mixinextras.sugar.Local;
import dev.willyelton.crystal_tools.common.components.DataComponents;
import dev.willyelton.crystal_tools.common.entity.CrystalTridentEntity;
import dev.willyelton.crystal_tools.common.levelable.tool.CrystalTrident;
import keeptrident.component.KPComponents;
import keeptrident.utils.TridentUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = CrystalTrident.class, remap = false)
public class CrystalTridentMixin {
    @Inject(
            method = "onStoppedUsing",
            at = @At(
                    value = "INVOKE",
                    target = "Ldev/willyelton/crystal_tools/common/entity/CrystalTridentEntity;setVelocity(Lnet/minecraft/entity/Entity;FFFFF)V"
            )
    )
    private void injectSetVelocity(ItemStack stack, World world, LivingEntity user, int remainingUseTicks, CallbackInfo ci, @Local PlayerEntity player, @Local CrystalTridentEntity tridentEntity) {
        var loyalty = stack.getOrDefault(DataComponents.LOYALTY, 0);
        if (loyalty > 0) {
            stack.set(KPComponents.THROWN, tridentEntity.getUuid());
            stack.set(KPComponents.THROWN_TICKS, 0);
        }
    }

    @Redirect(
            method = "onStoppedUsing",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/PlayerInventory;removeOne(Lnet/minecraft/item/ItemStack;)V"
            )
    )
    private void redirectRemoveTrident(PlayerInventory instance, ItemStack stack, @Local CrystalTridentEntity tridentEntity) {
        var loyalty = stack.getOrDefault(DataComponents.LOYALTY, 0);
        if (loyalty <= 0) {
            instance.removeOne(stack);
        }
    }

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    public void use(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (itemStack.contains(KPComponents.THROWN)) {
            cir.setReturnValue(TypedActionResult.fail(itemStack));
        }
    }
}
