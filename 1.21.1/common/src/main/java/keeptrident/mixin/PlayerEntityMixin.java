package keeptrident.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.world.World;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = PlayerEntity.class, priority = 2000)
public class PlayerEntityMixin {

    public PlayerEntity self = (PlayerEntity) (Object) this;

    @Inject(at = @At(value = "HEAD"), method = "tick")
    private void tick(CallbackInfo ci) {
        var mainHand = self.getMainHandStack();
        if (mainHand.getItem() instanceof TridentItem && mainHand.hasNbt() && mainHand.getNbt().contains("thrown")) {
            self.resetLastAttackedTicks();
        }
    }


    @Inject(method = "attack", at = @At("HEAD"), cancellable = true)
    private void injectAttack(Entity target, CallbackInfo ci) {
        var mainHand = self.getMainHandStack();
        if (mainHand.getItem() instanceof TridentItem && mainHand.hasNbt() && mainHand.getNbt().contains("thrown"))
            ci.cancel();
    }
}
