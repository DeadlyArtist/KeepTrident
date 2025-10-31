package keeptrident.forge.mixin.compat.upgradablecrystaltools;

import com.llamalad7.mixinextras.sugar.Local;
import dev.willyelton.crystal_tools.entity.CrystalTridentEntity;
import dev.willyelton.crystal_tools.levelable.tool.CrystalTrident;
import dev.willyelton.crystal_tools.utils.NBTUtils;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtInt;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = CrystalTrident.class)
public class CrystalTridentMixin {
    @Inject(
            method = "onStoppedUsing",
            at = @At(
                    value = "INVOKE",
                    target = "Ldev/willyelton/crystal_tools/entity/CrystalTridentEntity;setVelocity(Lnet/minecraft/entity/Entity;FFFFF)V"
            )
    )
    private void injectSetVelocity(ItemStack stack, World world, LivingEntity user, int remainingUseTicks, CallbackInfo ci, @Local PlayerEntity player, @Local CrystalTridentEntity tridentEntity) {
        var loyalty = NBTUtils.getFloatOrAddKey(stack, "loyalty");
        if (loyalty > 0) {
            stack.setSubNbt("trident_thrown", NbtHelper.fromUuid(tridentEntity.getUuid()));
            stack.setSubNbt("trident_thrown_ticks", NbtInt.of(0));
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
        var loyalty = NBTUtils.getFloatOrAddKey(stack, "loyalty");
        if (loyalty <= 0) {
            instance.removeOne(stack);
        }
    }

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    public void use(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (itemStack.hasNbt() && itemStack.getNbt().contains("trident_thrown")) {
            cir.setReturnValue(TypedActionResult.fail(itemStack));
        }
    }
}
