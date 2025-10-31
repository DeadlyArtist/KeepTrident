package keeptrident.mixin;

import net.minecraft.client.gui.DrawContext;
import org.spongepowered.asm.mixin.Mixin;
import com.llamalad7.mixinextras.sugar.Local;
import keeptrident.client.utils.RenderUtils;
import net.minecraft.client.render.*;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TridentItem;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DrawContext.class)
public class DrawContextMixin {

    @Unique
    private final DrawContext drawContext = (DrawContext) (Object) this;

    @Inject(method = "drawItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;IIII)V", at = @At("TAIL"))
    private void injectGreyOverlay(LivingEntity entity, World world, ItemStack stack, int x, int y, int seed, int z, CallbackInfo ci) {
        if (stack.hasNbt() && stack.getNbt().contains("trident_thrown")) {
            var matrices = drawContext.getMatrices();
            RenderUtils.renderGreyOverlay(matrices, x, y);
        }
    }
}
