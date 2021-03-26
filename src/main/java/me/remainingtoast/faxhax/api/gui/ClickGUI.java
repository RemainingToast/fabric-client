package me.remainingtoast.faxhax.api.gui;

import me.remainingtoast.faxhax.FaxHax;
import me.remainingtoast.faxhax.api.config.ConfigSave;
import me.remainingtoast.faxhax.api.module.Module;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ClickGUI extends Screen {

    public static final HashMap<Module.Category, Panel> panels = new HashMap<>();

    private boolean clicked = false;
    private boolean leftClicked = false;
    private boolean rightClicked = false;
    private boolean dragging = true;
    private int lastMouseX;
    private int lastMouseY;

    public ClickGUI() {
        super(new LiteralText("ClickGUI"));
    }

    @Override
    protected void init() {
        GuiConfig.loadPanels().forEach(panels::putIfAbsent);
    }

    @Override
    public void onClose() {
        FaxHax.mc.openScreen(null);
        GuiConfig.saveConfig();
        ConfigSave.saveModules();
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        for(Panel panel : panels.values()) {
            panel.render(matrices, mouseX, mouseY, lastMouseX, lastMouseY, leftClicked, rightClicked, delta);
        }
        if(clicked){
            leftClicked = false;
            rightClicked = false;
        }
        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(!clicked){
            if(button == 0) {
                leftClicked = true;
                rightClicked = false;
                clicked = true;
            } else if(button == 1){
                leftClicked = false;
                rightClicked = true;
                clicked = true;
            }
        } else {
            leftClicked = false;
            rightClicked = false;
        }
        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0 || button == 1) {
            leftClicked = false;
            rightClicked = false;
            clicked = false;
        }
        return false;
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        lastMouseX = (int) mouseX;
        lastMouseY = (int) mouseY;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if(keyCode == 256){
            onClose();
            return true;
        }
        return false;
    }
}
