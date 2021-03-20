package me.remainingtoast.faxhax.api.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;

public class TwoDRenderUtil extends DrawableHelper {

    private static final MinecraftClient mc = MinecraftClient.getInstance();

    public static void drawText(MatrixStack matrices, String text, int x, int y, int color){
        mc.textRenderer.drawWithShadow(matrices, text, x, y, color);
        matrices.push();
        matrices.pop();
    }

    public static void drawRect(MatrixStack matrices, int x, int y, int width, int height, int color){
        fill(matrices, x, y, width, height, color);
        matrices.push();
        matrices.pop();
    }

    public static void drawHollowRect(MatrixStack matrices, int x, int y, int width, int height, int lineWidth, int color){
        drawRect(matrices, x - lineWidth, y - lineWidth, width + lineWidth * 2, lineWidth, color);
        drawRect(matrices, x - lineWidth, y, lineWidth, height, color);
        drawRect(matrices, x - lineWidth, y + height, width + lineWidth * 2, lineWidth, color);
        drawRect(matrices, x + width, y, lineWidth, height, color);
    }

    public static void drawTextBox(MatrixStack matrices, String text, int x, int y, int width, int height, int boxColor, int textColor){
        drawRect(matrices, x, y, width, height, boxColor);
        drawText(matrices, text, x, y, textColor);
    }

}
