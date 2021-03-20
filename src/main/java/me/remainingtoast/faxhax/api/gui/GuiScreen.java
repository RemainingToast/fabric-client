package me.remainingtoast.faxhax.api.gui;

import me.remainingtoast.faxhax.api.module.Module;
import me.remainingtoast.faxhax.api.util.FaxColor;
import me.remainingtoast.faxhax.api.util.TwoDRenderUtil;
import me.remainingtoast.faxhax.impl.modules.client.ClickGUI;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;

import java.awt.*;
import java.util.HashMap;

public class GuiScreen extends Screen {

    public static final MinecraftClient mc = MinecraftClient.getInstance();

    private final HashMap<Module.Category, Panel> panels = new HashMap<>();

    public GuiScreen() {
        super(new LiteralText("ClickGUI"));
    }

    @Override
    protected void init() {
        int x = 10;
        for(Module.Category category : Module.Category.values()){
            panels.putIfAbsent(category, new Panel(category, x, 10));
            x += 80;
        }
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        for(Panel panel : panels.values()){
            panel.render(matrices, mouseX, mouseY, delta);
        }
        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }

    public static class Panel {

        private int x;
        private int y;
        private final Module.Category category;

        public Panel(Module.Category category, int x, int y){
            this.category = category;
            this.x = x;
            this.y = y;
        }

        public void render(MatrixStack matrices, int mouseX, int mouseY, float delta){
            TwoDRenderUtil.drawTextBox(
                    matrices,
                    category.name(),
                    new Rectangle(
                            x,
                            y,
                            70,
                            10
                    ),
                    -1,
                    0,
                    -1
            );
        }
    }
}
