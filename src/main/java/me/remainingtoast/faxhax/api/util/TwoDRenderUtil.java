package me.remainingtoast.faxhax.api.util;

import me.remainingtoast.faxhax.api.setting.Setting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

import java.awt.*;

public class TwoDRenderUtil extends DrawableHelper {

    private static final MinecraftClient mc = MinecraftClient.getInstance();

    public static void drawText(MatrixStack matrices, String text, int x, int y, int color){
        mc.textRenderer.drawWithShadow(matrices, text, x, y, color);
        matrices.push();
        matrices.pop();
    }

    /**
     * @param color
     * format 0xAARRGGBB
     */
    public static void drawRect(MatrixStack matrices, int x, int y, int width, int height, int color){
        fill(matrices, x, y, x + width, y + height, color);
    }

    public static void drawHollowRect(MatrixStack matrices, int x, int y, int width, int height, int lineWidth, int color){
        drawRect(matrices, x - lineWidth, y - lineWidth, width + lineWidth * 2, lineWidth, color); // top line
        drawRect(matrices, x - lineWidth, y, lineWidth, height, color);// left line
        drawRect(matrices, x - lineWidth, y + height, width + lineWidth * 2, lineWidth, color); // bottom line
        drawRect(matrices, x + width, y, lineWidth, height, color); // right line
    }

    public static void drawCenteredTextBox(MatrixStack matrices, String text, Rectangle rect, int bgColor, int textColor){
        drawRect(matrices, rect.x - 2, rect.y - 2, rect.width, rect.height, bgColor);
        drawCenteredString(matrices, mc.textRenderer, text, (int) rect.getCenterX(), rect.y, textColor);
    }

    public static void drawTextBox(MatrixStack matrices, String text, Rectangle rect, int bgColor, int textColor){
        drawRect(matrices, rect.x - 2, rect.y - 2, rect.width, rect.height, bgColor);
        drawText(matrices, text, rect.x + 1, rect.y, textColor);
    }

    public static void drawSettingTextBox(MatrixStack matrices, String text, Rectangle rect, int lineColor, int bgColor, int textColor){
        drawRect(matrices, rect.x, rect.y - 2, rect.width - 2, rect.height, bgColor);
        drawRect(matrices, rect.x - 2, rect.y - 3, 2, rect.height + 1, lineColor);
        drawText(matrices, text, rect.x + 2, rect.y, textColor);
    }

    public static void drawSettingTextBox(MatrixStack matrices, String text, String value, Rectangle rect, int lineColor, int bgColor, int textColor){
        drawRect(matrices, rect.x, rect.y - 2, rect.width, rect.height, bgColor);
        drawRect(matrices, rect.x - 2, rect.y - 3, 2, rect.height + 1, lineColor);
        drawText(matrices, text, rect.x + 2, rect.y, textColor);
        drawText(matrices, value, rect.x + (rect.width - mc.textRenderer.getWidth(value)) - 2, rect.y, textColor);
    }

    public static boolean mouseOverRect(double mouseX, double mouseY, Rectangle rect){
        return mouseX >= rect.x && mouseX <= rect.width + rect.x && mouseY >= rect.y && mouseY <= rect.height + rect.y;
    }



}
