package keeptrident.mixin;

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

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {

    @Unique
    public final ItemRenderer itemRenderer = (ItemRenderer) (Object) this;

    @Inject(method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V", at = @At(value = "HEAD"), cancellable = true)
    public void injectRenderItem(ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, BakedModel model, CallbackInfo ci) {
        boolean bl = renderMode == ModelTransformationMode.GUI || renderMode == ModelTransformationMode.GROUND || renderMode == ModelTransformationMode.FIXED;
        if (!bl && stack.hasNbt() && stack.getNbt().contains("trident_thrown")) {
            ci.cancel();
            return;
        }
    }

    @Inject(method = "renderGuiItemModel", at = @At("TAIL"))
    private void injectGreyOverlay(MatrixStack matrices, ItemStack stack, int x, int y, BakedModel model, CallbackInfo ci) {
        if (stack.hasNbt() && stack.getNbt().contains("trident_thrown")) {
            RenderUtils.renderGreyOverlay(matrices, x, y);
        }
    }
}
