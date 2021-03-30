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

    public int x, y;
    private int level;
    private final int width = 90;
    private final int height = 12;

    public Module.Category category;
    public boolean categoryExpanded;

    public HashMap<Module, Boolean> modsExpanded = new HashMap<>();

    public Panel(Module.Category category, int x, int y){
        this.category = category;
        this.x = x;
        this.y = y;
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY, int lastMouseX, int lastMouseY, boolean leftClicked, boolean rightClicked, float delta){
        boolean hovering = mouseOverRect(mouseX, mouseY, new Rectangle(x, y, width, height));
        drawCategory(matrices, category, mouseX, mouseY, lastMouseX, lastMouseY, hovering, leftClicked, rightClicked);
    }

    public void drawCategory(MatrixStack matrices, Module.Category category, int mouseX, int mouseY, int lastMouseX, int lastMouseY, boolean hovering, boolean leftClicked, boolean rightClicked){
        level = 1;
        drawCenteredTextBox(
                matrices,
                category.name(),
                new Rectangle(
                        x,
                        y,
                        width,
                        height
                ),
                (hovering) ? 0x9900FF00 : 0x8000FF00,
                0xFFFFFFFF
        );
        if(hovering){
            if(rightClicked) {
                categoryExpanded = !categoryExpanded;
            }
        }
        if(categoryExpanded){
            for(Module mod : ModuleManager.getModulesInCategory(category)){
                drawModule(matrices, mod, mouseX, mouseY, lastMouseX, lastMouseY, leftClicked, rightClicked);
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

    public void drawModule(MatrixStack matrices, Module mod, int mouseX, int mouseY, int lastMouseX, int lastMouseY, boolean leftClicked, boolean rightClicked){
        modsExpanded.putIfAbsent(mod, false);
        boolean mouseOverModRect = mouseOverRect(mouseX, mouseY, iteratedRect(new Rectangle(
                x,
                y,
                width,
                height
        ), level));
        if(mouseOverModRect) {
            if(leftClicked) mod.toggle();
            if(rightClicked) modsExpanded.put(mod, !modsExpanded.get(mod));
        }
        drawTextBox(
                matrices,
                mod.name,
                iteratedRect(new Rectangle(
                        x,
                        y,
                        width,
                        height
                ), level),
                (mod.enabled) ? (mouseOverModRect) ? 0x9900FF00 : 0x8000FF00 : (mouseOverModRect) ? 0x80000000 : 0x50000000,
                0xFFFFFFFF
        );
        level++;
        for(Map.Entry<Module, Boolean> entry : modsExpanded.entrySet()){
            if(entry.getValue()){
                for(Setting<?> setting : SettingManager.getSettingsForMod(entry.getKey())){
                    if(setting.getParent() == mod && !setting.isHidden()){
                        if(setting.getType() == Setting.Type.GROUP){
                            drawGroupSettings(
                                    matrices,
                                    (Setting.Group) setting,
                                    iteratedRect(new Rectangle(x, y, width, height), level),
                                    mouseX,
                                    mouseY,
                                    lastMouseX,
                                    lastMouseY,
                                    leftClicked,
                                    rightClicked
                            );
                            level++;
                            if(((Setting.Group) setting).isExpanded()){
                                for(Setting<?> set : ((Setting.Group) setting).getSettings()){
                                    drawSetting(
                                            matrices,
                                            set,
                                            iteratedRect(new Rectangle(x, y, width, height), level),
                                            mouseX,
                                            mouseY,
                                            lastMouseX,
                                            lastMouseY,
                                            leftClicked,
                                            rightClicked
                                    );
                                    level++;
                                }
                            }
                        } else if(!setting.isGrouped()) {
                            drawSetting(
                                    matrices,
                                    setting,
                                    iteratedRect(new Rectangle(
                                            x,
                                            y,
                                            width,
                                            height
                                    ), level),
                                    mouseX,
                                    mouseY,
                                    lastMouseX,
                                    lastMouseY,
                                    leftClicked,
                                    rightClicked
                            );
                            level++;
                        }
                    }
                }
            }
        }
    }
}
