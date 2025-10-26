package keeptrident.utils;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.apache.commons.lang3.mutable.MutableFloat;

public class TridentUtils {
    public static byte getLoyalty(TridentEntity tridentEntity, ItemStack stack) {
        World var3 = tridentEntity.getWorld();
        if (var3 instanceof ServerWorld serverWorld) {
            return (byte) MathHelper.clamp(EnchantmentHelper.getTridentReturnAcceleration(serverWorld, stack, tridentEntity), 0, 127);
        } else {
            return 0;
        }
    }

    public static float getRiptide(ItemStack stack, LivingEntity user) {
        return EnchantmentHelper.getTridentSpinAttackStrength(stack, user);
    }
}
