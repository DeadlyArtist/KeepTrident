package keeptrident.client.utils;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.datafixers.util.Pair;
import keeptrident.utils.ItemUtils;
import net.minecraft.block.entity.BannerBlockEntity;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BannerBlockEntityRenderer;
import net.minecraft.client.render.entity.model.ShieldEntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.registry.entry.RegistryEntry;

import java.util.List;

import static net.minecraft.item.ShieldItem.getColor;

public class RenderUtils {
    public static void renderGreyOverlay(int x, int y) {
        RenderSystem.disableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);

        // Setting overlay color (semi-transparent black/grey)
        float alpha = 0.6f; // Adjust for desired transparency
        int grey = 80; // Adjust for how dark the overlay should be (0 = black, 255 = white)

        BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);

        buffer.vertex(x, y + 16, 200.0).color(grey, grey, grey, (int) (alpha * 255)).next();
        buffer.vertex(x + 16, y + 16, 200.0).color(grey, grey, grey, (int) (alpha * 255)).next();
        buffer.vertex(x + 16, y, 200.0).color(grey, grey, grey, (int) (alpha * 255)).next();
        buffer.vertex(x, y, 200.0).color(grey, grey, grey, (int) (alpha * 255)).next();

        BufferRenderer.drawWithGlobalProgram(buffer.end());

        RenderSystem.disableBlend();
        RenderSystem.enableDepthTest();
    }
}
