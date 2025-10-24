package keeptrident.mixin;

import com.google.common.collect.ImmutableMultimap;
import com.llamalad7.mixinextras.sugar.Local;
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
        var loyalty = EnchantmentHelper.getLoyalty(stack);
        if (loyalty > 0) {
            stack.setSubNbt("thrown", NbtHelper.fromUuid(tridentEntity.getUuid()));
            stack.setSubNbt("thrown_ticks", NbtInt.of(0));
        }
    }

    @Redirect(
            method = "onStoppedUsing",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/PlayerInventory;removeOne(Lnet/minecraft/item/ItemStack;)V"
            )
    )
    private void redirectRemoveTrident(PlayerInventory instance, ItemStack stack) {
        var loyalty = EnchantmentHelper.getLoyalty(stack);
        if (loyalty <= 0) {
            instance.removeOne(stack);
        }
    }

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    public void use(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        ItemStack itemStack = user.getStackInHand(hand);
        var result = TypedActionResult.fail(itemStack);
        if (itemStack.hasNbt() && itemStack.getNbt().contains("thrown")) {
            // fail
        } else if (itemStack.getDamage() >= itemStack.getMaxDamage() - 1 && itemStack.isDamageable()) {
            // fail
        } else if (EnchantmentHelper.getRiptide(itemStack) > 0 && !user.isTouchingWaterOrRain()) {
            // fail
        } else {
            user.setCurrentHand(hand);
            result = TypedActionResult.consume(itemStack);
        }

        cir.setReturnValue(result);
    }
}
