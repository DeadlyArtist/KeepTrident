package keeptrident.mixin;

import com.google.common.collect.ImmutableMultimap;
import com.llamalad7.mixinextras.sugar.Local;
import keeptrident.component.KPComponents;
import keeptrident.utils.TridentUtils;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TridentItem;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtInt;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TridentItem.class)
public class TridentItemMixin {

    @Inject(
            method = "onStoppedUsing",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/projectile/TridentEntity;setVelocity(Lnet/minecraft/entity/Entity;FFFFF)V"
            )
    )
    private void injectSetVelocity(ItemStack stack, World world, LivingEntity user, int remainingUseTicks, CallbackInfo ci, @Local PlayerEntity player, @Local TridentEntity tridentEntity) {
        var loyalty = TridentUtils.getLoyalty(tridentEntity, stack);
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
    private void redirectRemoveTrident(PlayerInventory instance, ItemStack stack, @Local TridentEntity tridentEntity) {
        var loyalty = TridentUtils.getLoyalty(tridentEntity, stack);
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
