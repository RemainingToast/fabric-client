package me.remainingtoast.faxhax.api.util;

import me.remainingtoast.faxhax.api.setting.Setting;
import me.remainingtoast.faxhax.impl.modules.client.ClickGUIModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.awt.*;

public class TwoDRenderUtil extends DrawableHelper {

    private static final MinecraftClient mc = MinecraftClient.getInstance();

    private static final int BACKGROUND_COLOR = 0x50000000;
    private static final int BACKGROUND_COLOR_HOVER = 0x80000000;
    private static final int GENERAL_COLOR = 0x8000FF00;
    private static final int GENERAL_COLOR_HOVER = 0x9900FF00;
    private static final int TEXT_COLOR = 0xFFFFFFFF;
    private static final boolean SHOW_BOOL_VALUE = false;

    private static boolean CUSTOM_FONT;

    public static void drawText(MatrixStack matrices, Text text, int x, int y, int color){
        mc.textRenderer.drawWithShadow(
                matrices,
                addFontToText(text),
                (CUSTOM_FONT) ? x - 1 : x,
                (CUSTOM_FONT) ? y + 2: y,
                color);
        matrices.push();
        matrices.pop();
    }

    public static void drawText(MatrixStack matrices, String string, int x, int y, int color){
        mc.textRenderer.drawWithShadow(
                matrices,
                addFontToText(string),
                (CUSTOM_FONT) ? x - 2 : x,
                (CUSTOM_FONT) ? y + 2: y,
                color);
        matrices.push();
        matrices.pop();
    }

    public static void drawCenteredText(MatrixStack matrices, Text text, int centerX, int y, int color) {
        mc.textRenderer.drawWithShadow(
                matrices,
                text,
                (float) (centerX - mc.textRenderer.getWidth(text) / 2) + ((CUSTOM_FONT) ? - 2 : 0),
                (float) y,
                color);
    }

    private static MutableText addFontToText(String text){
        Identifier newFont = ClickGUIModule.getFont();
        CUSTOM_FONT = !newFont.equals(new Identifier("minecraft", "default"));
        return new LiteralText(text).styled(style -> style.withFont(newFont));
    }

    private static MutableText addFontToText(Text text){
        Identifier newFont = ClickGUIModule.getFont();
        CUSTOM_FONT = !newFont.equals(new Identifier("minecraft", "default"));
        return text.shallowCopy().styled(style -> style.withFont(newFont));
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

    public static void drawCenteredTextBox(MatrixStack matrices, String text, Rectangle rect, int bgColor, int textColor) {
        drawRect(matrices, rect.x - 2, rect.y - 2, rect.width, rect.height, bgColor);
        drawCenteredText(
                matrices,
                addFontToText(text),
                (int) rect.getCenterX(),
                rect.y,
                textColor
        );
    }

    public static void drawTextBox(MatrixStack matrices, String text, Rectangle rect, int bgColor, int textColor){
        drawRect(matrices, rect.x - 2, rect.y - 2, rect.width, rect.height, bgColor);
        drawText(matrices, text, rect.x + 1, rect.y, textColor);
    }

    public static void drawSetting(MatrixStack matrices, Setting setting, Rectangle rect, int mouseX, int mouseY, int lastMouseX, int lastMouseY, boolean leftClicked, boolean rightClicked) {
        final boolean hovering = mouseOverRect(mouseX, mouseY, rect);
        switch (setting.getType()){
            case BOOLEAN: {
                Setting.Boolean bool = (Setting.Boolean) setting;
                drawBooleanSetting(matrices, bool, rect, hovering, leftClicked, rightClicked, SHOW_BOOL_VALUE);
                break;
            }
            case DOUBLE: {
                assert setting instanceof Setting.Double;
                drawNumberSetting(matrices, (Setting.Double) setting, rect, mouseX, lastMouseX, hovering, leftClicked, rightClicked);
                break;
            }
            case MODE: {
                assert setting instanceof Setting.Mode;
                drawModeSetting(matrices, (Setting.Mode) setting, rect, hovering, leftClicked, rightClicked);
                break;
            }
            case COLOR: {
                assert setting instanceof Setting.ColorSetting;
                drawColorSetting(matrices, (Setting.ColorSetting) setting, rect, hovering, leftClicked, rightClicked);
                break;
            }
        }
    }

    public static void drawBooleanSetting(MatrixStack matrices, Setting.Boolean bool, Rectangle rect, boolean hovering, boolean leftClicked, boolean rightClicked, boolean showValue){
        final int COLOR = (bool.getValue()) ?
                (hovering) ? GENERAL_COLOR_HOVER : GENERAL_COLOR :
                (hovering) ? BACKGROUND_COLOR_HOVER : BACKGROUND_COLOR;
        drawRect(matrices, rect.x, rect.y - 2, rect.width - 2, rect.height, COLOR);
        drawRect(matrices, rect.x - 2, rect.y - 3, 2, rect.height + 1, GENERAL_COLOR);
        drawText(matrices, bool.getName(), rect.x + 2, rect.y, TEXT_COLOR);
        if(showValue) drawText(matrices, String.valueOf(bool.getValue()), rect.x + (rect.width - mc.textRenderer.getWidth(String.valueOf(bool.getValue()))) - 2, rect.y, TEXT_COLOR);
        if (hovering && leftClicked) bool.setValue(!bool.getValue());
    }

    public static void drawNumberSetting(MatrixStack matrices, Setting.Double setting, Rectangle rect, int mouseX, int lastMouseX, boolean hovering, boolean leftClicked, boolean rightClicked){
        double percentage = (setting.getValue() - setting.getMin()) / (setting.getMax() - setting.getMin());
        int progress = (int) (percentage * rect.width);
        drawRect(matrices, rect.x, rect.y - 2, progress - 2, rect.height, GENERAL_COLOR);
        drawRect(matrices, rect.x + progress - 2, rect.y - 2, rect.width - progress, rect.height, (hovering) ? 0x80000000 : 0x50000000);
        drawRect(matrices, rect.x - 2, rect.y - 3, 2, rect.height + 1, GENERAL_COLOR);
        drawText(matrices, setting.getName(), rect.x + 2, rect.y, TEXT_COLOR);
        Text text = new LiteralText(String.valueOf(setting.getValue())).styled(style -> style.withColor(Formatting.GRAY));
        drawText(matrices, text, rect.x + (rect.width - mc.textRenderer.getWidth(text)) - ((CUSTOM_FONT) ? 1 : 3), rect.y, TEXT_COLOR);
        if (hovering && leftClicked) {
            progress += mouseX - lastMouseX;
            progress = clamp(progress, 0, rect.width);
            setting.setValue((int) (progress / (rect.width * (setting.getMax() - setting.getMin()) + setting.getMin())));
        }
    }



    public static void drawModeSetting(MatrixStack matrices, Setting.Mode mode, Rectangle rect, boolean hovering, boolean leftClicked, boolean rightClicked) {
        final int COLOR = (hovering) ? BACKGROUND_COLOR_HOVER : BACKGROUND_COLOR;
        drawRect(matrices, rect.x, rect.y - 2, rect.width - 2, rect.height, COLOR);
        drawRect(matrices, rect.x - 2, rect.y - 3, 2, rect.height + 1, GENERAL_COLOR);
        drawText(matrices, mode.getName(), rect.x + 2, rect.y, TEXT_COLOR);
        Text text = new LiteralText(mode.getValueName()).styled(style -> style.withColor(Formatting.GRAY));
        drawText(matrices, text, rect.x + (rect.width - mc.textRenderer.getWidth(text)) - 2, rect.y, TEXT_COLOR);
        if (hovering && leftClicked) mode.increment();
    }

    public static void drawColorSetting(MatrixStack matrices, Setting.ColorSetting color, Rectangle rect, boolean hovering, boolean leftClicked, boolean rightClicked){
        drawRect(matrices, rect.x, rect.y - 2, rect.width - 2, rect.height, color.getColor().getRGB());
        drawRect(matrices, rect.x - 2, rect.y - 3, 2, rect.height + 1, GENERAL_COLOR);
        drawText(matrices, color.getName(), rect.x + 2, rect.y, TEXT_COLOR);
    }

    public static boolean mouseOverRect(double mouseX, double mouseY, Rectangle rect){
        return mouseX >= rect.x && mouseX <= rect.width + rect.x && mouseY >= rect.y && mouseY <= rect.height + rect.y;
    }

    public static int clamp(int value, int min, int max) {
        if (value < min) return min;
        if (value > max) return max;
        return value;
    }

}
