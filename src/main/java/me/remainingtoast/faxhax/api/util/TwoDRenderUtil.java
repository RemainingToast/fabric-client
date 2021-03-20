package me.remainingtoast.faxhax.api.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;

import java.awt.*;

public class TwoDRenderUtil extends DrawableHelper {

    private static final MinecraftClient mc = MinecraftClient.getInstance();

    public static void drawText(MatrixStack matrices, String text, int x, int y, int color){
        mc.textRenderer.drawWithShadow(matrices, text, x, y, color);
        matrices.push();
        matrices.pop();
    }

    public static void drawCenteredText(MatrixStack matrices, String text, Rectangle rectangle, int color){
        mc.textRenderer.drawWithShadow(matrices, text, (int) rectangle.getCenterX(), (int) rectangle.getCenterY(), color);
        matrices.push();
        matrices.pop();
    }

    public static void drawRect(MatrixStack matrices, int x, int y, int width, int height, int color){
        fill(matrices, x, y, x + width, y + height, color);
        matrices.push();
        matrices.pop();
    }

    public static void drawHollowRect(MatrixStack matrices, int x, int y, int width, int height, int lineWidth, int color){
        drawRect(matrices, x - lineWidth, y - lineWidth, width + lineWidth * 2, lineWidth, color); // top line
        drawRect(matrices, x - lineWidth, y, lineWidth, height, color);// left line
        drawRect(matrices, x - lineWidth, y + height, width + lineWidth * 2, lineWidth, color); // bottom line
        drawRect(matrices, x + width, y, lineWidth, height, color); // right line
    }

    public static void drawTextBox(MatrixStack matrices, String text, Rectangle rect, int outlineColor, int boxColor, int textColor){
        drawRect(matrices, rect.x - 2, rect.y - 2, rect.width, rect.height, boxColor);
        drawHollowRect(matrices, rect.x - 2, rect.y - 2, rect.width, rect.height, 1, outlineColor);
        drawCenteredString(matrices, mc.textRenderer, text, (int) rect.getCenterX(), rect.y + 1, textColor);
    }

    public static boolean mouseOverRect(double mouseX, double mouseY, int x, int y, int width, int height){
        return mouseX >= x && mouseX <= width + x && mouseY >= y && mouseY <= height + y;
    }

}