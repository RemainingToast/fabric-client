package club.faxhax.client.api.util;

import club.faxhax.client.api.module.ModuleManager;
import club.faxhax.client.api.setting.Setting;
import club.faxhax.client.impl.modules.client.FontModule;
import net.minecraft.client.MinecraftClient;
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

    private static boolean CUSTOM_FONT;

    private static final FontModule FONT_MODULE = (FontModule) ModuleManager.getModule("Font");

    private static final Identifier MINECRAFT_FONT = new Identifier("minecraft", "default");

    public static void drawText(MatrixStack matrices, String text, int x, int y, int color){
        mc.textRenderer.drawWithShadow(
                matrices,
                formatText(text),
                (CUSTOM_FONT) ? x - 1 : x,
                (CUSTOM_FONT) ? y + 2 : y,
                color);
        matrices.push();
        matrices.pop();
    }

    public static void drawValueText(MatrixStack matrices, String text, int x, int y, int color){
        mc.textRenderer.drawWithShadow(
                matrices,
                formatValueText(text),
                x,
                (CUSTOM_FONT) ? y + 2 : y,
                color);
        matrices.push();
        matrices.pop();
    }

    public static void drawValueText(MatrixStack matrices, double value, int x, int y, int color){
        drawValueText(matrices, String.valueOf(value), x, y, color);
    }

    public static void drawCenteredText(MatrixStack matrices, Text text, int centerX, int y, int color) {
        mc.textRenderer.drawWithShadow(
                matrices,
                text,
                (float) (centerX - mc.textRenderer.getWidth(text) / 2) + ((CUSTOM_FONT) ? - 2 : 0),
                (float) y + ((CUSTOM_FONT) ? + 2 : 0),
                color);
    }

    private static MutableText formatText(String text){
        Identifier newFont = (FONT_MODULE != null && FONT_MODULE.enabled) ? FONT_MODULE.getFont() : MINECRAFT_FONT;
        CUSTOM_FONT = !newFont.equals(MINECRAFT_FONT);
        return new LiteralText(text)
                .styled(style -> style
                .withFont(newFont));
    }

    private static MutableText formatValueText(double value){
        return formatValueText(String.valueOf(value));
    }

    private static MutableText formatValueText(String value){
        Identifier newFont = (FONT_MODULE != null && FONT_MODULE.enabled) ? FONT_MODULE.getFont() : MINECRAFT_FONT;
        CUSTOM_FONT = !newFont.equals(MINECRAFT_FONT);
        return new LiteralText(value)
                .styled(style -> style
                .withFont(newFont)
                .withColor(Formatting.GRAY));
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
                formatText(text),
                (int) rect.getCenterX(),
                rect.y,
                textColor
        );
    }

    public static void drawTextBox(MatrixStack matrices, String text, Rectangle rect, int bgColor, int textColor){
        drawRect(matrices, rect.x - 2, rect.y - 2, rect.width, rect.height, bgColor);
        drawText(matrices, text, rect.x, rect.y, textColor);
    }

    public static void drawGroupSettings(MatrixStack matrices, Setting.Group group, Rectangle rect, int mouseX, int mouseY, int lastMouseX, int lastMouseY, boolean leftClicked, boolean rightClicked){
        final boolean hovering = mouseOverRect(mouseX, mouseY, rect);
        final String valueText = (group.isExpanded()) ? "_" : "...";
        drawRect(matrices, rect.x, rect.y - 2, rect.width - 2, rect.height, (hovering) ? GENERAL_COLOR_HOVER : GENERAL_COLOR);
        drawRect(matrices, rect.x - 2, rect.y - 3, 2, rect.height + 1, GENERAL_COLOR);
        drawText(matrices, group.getName(), rect.x + 2, rect.y, TEXT_COLOR);
        drawText(matrices, valueText, rect.x + (rect.width - mc.textRenderer.getWidth(formatValueText(valueText))) - 5, rect.y, TEXT_COLOR);
        if(hovering && rightClicked) group.setExpanded(!group.isExpanded());
    }

    public static void drawSetting(MatrixStack matrices, Setting<?> setting, Rectangle rect, int mouseX, int mouseY, int lastMouseX, int lastMouseY, boolean leftClicked, boolean rightClicked) {
        final boolean hovering = mouseOverRect(mouseX, mouseY, rect);
        switch (setting.getType()){
            case BOOLEAN: {
                Setting.Boolean bool = (Setting.Boolean) setting;
                drawBooleanSetting(matrices, bool, rect, hovering, leftClicked, rightClicked);
                break;
            }
            case DOUBLE: {
                assert setting instanceof Setting.Double;
                drawNumberSetting(matrices, (Setting.Double) setting, rect, mouseX, mouseY, lastMouseX, lastMouseY, hovering, leftClicked, rightClicked);
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
            case BIND: {
                assert setting instanceof Setting.KeyBind;
                drawKeybind(matrices, (Setting.KeyBind) setting, rect, mouseX, mouseY);
                break;
            }
        }
    }

    public static void drawBooleanSetting(MatrixStack matrices, Setting.Boolean bool, Rectangle rect, boolean hovering, boolean leftClicked, boolean rightClicked){
        final int COLOR = (bool.getValue()) ? (hovering) ? GENERAL_COLOR_HOVER : GENERAL_COLOR : (hovering) ? BACKGROUND_COLOR_HOVER : BACKGROUND_COLOR;
        drawRect(matrices, rect.x, rect.y - 2, rect.width - 2, rect.height, COLOR);
        drawRect(matrices, rect.x - 2, rect.y - 3, 2, rect.height + 1, GENERAL_COLOR);
        drawText(matrices, bool.getName(), rect.x + 2, rect.y, TEXT_COLOR);
        if (hovering && leftClicked) bool.setValue(!bool.getValue());
    }

    public static void drawNumberSetting(MatrixStack matrices, Setting.Double setting, Rectangle rect, int mouseX, int mouseY, int lastMouseX, int lastMouseY, boolean hovering, boolean leftClicked, boolean rightClicked){
        double percentage = (setting.getValue() - setting.getMin()) / (setting.getMax() - setting.getMin());
        int progress = (int) (percentage * rect.width);

        if(setting.getValue() != setting.getMin())
            drawRect(matrices, rect.x, rect.y - 2, progress - 2, rect.height, GENERAL_COLOR);

        drawRect(matrices, rect.x - 2 + progress, rect.y - 2, rect.width - progress, rect.height, (hovering) ? BACKGROUND_COLOR_HOVER : BACKGROUND_COLOR);
        drawRect(matrices, rect.x - 2, rect.y - 3, 2, rect.height + 1, (hovering) ? GENERAL_COLOR_HOVER : GENERAL_COLOR);
        drawText(matrices, setting.getName(), rect.x + 2, rect.y, TEXT_COLOR);
        drawValueText(matrices, setting.getValue(), rect.x + (rect.width - mc.textRenderer.getWidth(formatValueText(setting.getValue()))) - 5, rect.y, TEXT_COLOR);

        if(mouseOverRect(mouseX, mouseY, rect) && leftClicked){
                progress += mouseX - lastMouseX;
                progress = Math.min(progress, Math.max(0, rect.width));
            double value = ((double) (progress / rect.width)) *
                    (setting.getMax() - setting.getMin()) + setting.getMin();
            setting.setValue(value);
        }
    }

    public static void drawModeSetting(MatrixStack matrices, Setting.Mode mode, Rectangle rect, boolean hovering, boolean leftClicked, boolean rightClicked) {
        drawRect(matrices, rect.x, rect.y - 2, rect.width - 2, rect.height, (hovering) ? BACKGROUND_COLOR_HOVER : BACKGROUND_COLOR);
        drawRect(matrices, rect.x - 2, rect.y - 3, 2, rect.height + 1, (hovering) ? GENERAL_COLOR_HOVER : GENERAL_COLOR);
        drawText(matrices, mode.getName(), rect.x + 2, rect.y, TEXT_COLOR);
        drawValueText(matrices, mode.getValueName(), rect.x + (rect.width - mc.textRenderer.getWidth(formatValueText(mode.getValueName()))) - 5, rect.y, TEXT_COLOR);
        if (hovering && leftClicked) mode.increment();
    }

    public static void drawColorSetting(MatrixStack matrices, Setting.ColorSetting color, Rectangle rect, boolean hovering, boolean leftClicked, boolean rightClicked){
        drawRect(matrices, rect.x, rect.y - 2, rect.width - 2, rect.height, color.getColor().getRGB());
        drawRect(matrices, rect.x - 2, rect.y - 3, 2, rect.height + 1, GENERAL_COLOR);
        drawText(matrices, color.getName(), rect.x + 2, rect.y, TEXT_COLOR);
    }

    public static void drawKeybind(MatrixStack matrices, Setting.KeyBind keyBind, Rectangle rect, int mouseX, int mouseY){
        final boolean hovering = mouseOverRect(mouseX, mouseY, rect);
        drawRect(matrices, rect.x, rect.y - 2, rect.width - 2, rect.height, (hovering) ? BACKGROUND_COLOR_HOVER : BACKGROUND_COLOR);
        drawRect(matrices, rect.x - 2, rect.y - 3, 2, rect.height + 1, (hovering) ? GENERAL_COLOR_HOVER : GENERAL_COLOR);
        drawText(matrices, "Bind", rect.x + 2, rect.y, TEXT_COLOR);
        drawValueText(matrices, keyBind.getKeyName(), rect.x + (rect.width - mc.textRenderer.getWidth(formatValueText(keyBind.getKeyName()))) - 5, rect.y, TEXT_COLOR);
    }

    public static boolean mouseOverRect(double mouseX, double mouseY, Rectangle rect){
        return mouseX >= rect.x && mouseX <= rect.width + rect.x && mouseY >= rect.y && mouseY <= rect.height + rect.y;
    }

    public static int iteratedY(int y, int height, int iteration){
        return y + iteration + (height * iteration);
    }

    public static Rectangle iteratedRect(Rectangle rect, int iteration){
        return new Rectangle(
                rect.x + 1,
                iteratedY(rect.y, rect.height, iteration),
                rect.width - 2,
                rect.height
        );
    }
}
