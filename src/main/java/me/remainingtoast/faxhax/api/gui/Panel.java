package me.remainingtoast.faxhax.api.gui;

import me.remainingtoast.faxhax.api.module.Module;
import me.remainingtoast.faxhax.api.module.ModuleManager;
import me.remainingtoast.faxhax.api.setting.Setting;
import me.remainingtoast.faxhax.api.setting.SettingManager;
import me.remainingtoast.faxhax.api.util.TwoDRenderUtil;
import net.minecraft.client.util.math.MatrixStack;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Panel extends TwoDRenderUtil {

    private int x, y, level;
    private final int width = 90;
    private final int height = 12;

    private Module.Category category;
    private boolean categoryExpanded;

    private HashMap<Module, Boolean> modsExpanded = new HashMap<>();

    public Panel(Module.Category category, int x, int y){
        this.category = category;
        this.x = x;
        this.y = y;
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY, boolean leftClicked, boolean rightClicked, float delta){
        drawCategory(matrices, category, mouseX, mouseY, leftClicked, rightClicked);
    }

    public void drawCategory(MatrixStack matrices, Module.Category category, int mouseX, int mouseY, boolean leftClicked, boolean rightClicked){
        level = 1;
        boolean mouseOverMod = mouseOverRect(mouseX, mouseY, new Rectangle(x, y, width, height));
        drawCenteredTextBox(
                matrices,
                category.name(),
                new Rectangle(
                        x,
                        y,
                        width,
                        height
                ),
                (mouseOverMod) ? 0x9900FF00 : 0x8000FF00,
                0xFFFFFFFF
        );
        if(mouseOverMod){
            if(rightClicked) {
                categoryExpanded = !categoryExpanded;
            }
        }
        if(categoryExpanded){
            for(Module mod : ModuleManager.getModulesInCategory(category)){
                drawModule(matrices, mod, mouseX, mouseY, leftClicked, rightClicked);
            }
        }
        drawHollowRect(
                matrices,
                x - 2,
                y - 2,
                width,
                level + (height * level),
                1,
                0x8000FF00
        );
    }

    public void drawModule(MatrixStack matrices, Module mod, int mouseX, int mouseY, boolean leftClicked, boolean rightClicked){
        modsExpanded.putIfAbsent(mod, false);
        boolean mouseOverModRect = mouseOverRect(mouseX, mouseY, iteratedRect(level));
        if(mouseOverModRect) {
            if(leftClicked) mod.toggle();
            if(rightClicked) modsExpanded.put(mod, !modsExpanded.get(mod));
        }
        drawTextBox(
                matrices,
                mod.name,
                iteratedRect(level),
                (mod.enabled) ? (mouseOverModRect) ? 0x9900FF00 : 0x8000FF00 : (mouseOverModRect) ? 0x80000000 : 0x50000000,
                0xFFFFFFFF
        );
        level++;
        for(Map.Entry<Module, Boolean> entry : modsExpanded.entrySet()){
            if(entry.getValue()){
                for(Setting setting : SettingManager.getSettingsForMod(entry.getKey())){
                    if(setting.getParent() == mod){
                        drawSetting(matrices, setting, mouseX, mouseY, leftClicked,rightClicked);
                    }
                }
            }
        }
    }

    public void drawSetting(MatrixStack matrices, Setting setting, int mouseX, int mouseY, boolean leftClicked, boolean rightClicked){
        boolean mouseOverSetting = mouseOverRect(mouseX, mouseY, iteratedRect(level));
        if (setting.getType().equals(Setting.Type.BOOLEAN)) {
            Setting.Boolean set = (Setting.Boolean) setting;
            drawSettingTextBox(
                    matrices,
                    setting.getName(),
                    iteratedRect(level),
                    0x8000FF00,
                    (set.getValue()) ? (mouseOverSetting) ? 0x9900FF00 : 0x8000FF00 : (mouseOverSetting) ? 0x80000000 : 0x50000000,
                    0xFFFFFFFF
            );
            if (mouseOverSetting && leftClicked) set.setValue(!set.getValue());
        } else if (setting.getType().equals(Setting.Type.DOUBLE)) {
            Setting.Double set = (Setting.Double) setting;
            drawSettingTextBox(
                    matrices,
                    setting.getName(),
                    String.valueOf(set.getValue()),
                    iteratedRect(level),
                    0x8000FF00,
                    (mouseOverSetting) ? 0x80000000 : 0x50000000,
                    0xFFFFFFFF
            );
        } else if (setting.getType().equals(Setting.Type.INTEGER)) {
            Setting.Integer set = (Setting.Integer) setting;
            drawSettingTextBox(
                    matrices,
                    setting.getName(),
                    String.valueOf(set.getValue()),
                    iteratedRect(level),
                    0x8000FF00,
                    (mouseOverSetting) ? 0x80000000 : 0x50000000,
                    0xFFFFFFFF
            );
        } else if (setting.getType().equals(Setting.Type.MODE)) {
            Setting.Mode set = (Setting.Mode) setting;
            drawSettingTextBox(
                    matrices,
                    setting.getName(),
                    set.getValue(),
                    iteratedRect(level),
                    0x8000FF00,
                    (mouseOverSetting) ? 0x80000000 : 0x50000000,
                    0xFFFFFFFF
            );
            if (mouseOverSetting && leftClicked) set.increment();
        } else {
            drawSettingTextBox(
                    matrices,
                    setting.getName(),
                    iteratedRect(level),
                    0x8000FF00,
                    (mouseOverSetting) ? 0x80000000 : 0x50000000,
                    0xFFFFFFFF
            );
        }
        level++;
    }

    private int iteratedY(int iteration){
        return y + iteration + (height * iteration);
    }

    private Rectangle iteratedRect(int iteration){
        return new Rectangle(
                x + 1,
                iteratedY(iteration),
                width - 2,
                height
        );
    }
}
