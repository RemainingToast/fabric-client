package me.remainingtoast.faxhax.api.gui;

import me.remainingtoast.faxhax.api.module.Module;
import me.remainingtoast.faxhax.api.module.ModuleManager;
import me.remainingtoast.faxhax.api.util.TwoDRenderUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GuiScreen extends Screen {

    public static final MinecraftClient mc = MinecraftClient.getInstance();

    private final List<Panel> panels = new ArrayList<>();
    private boolean clicked = false;
    private boolean leftClicked = false;
    private boolean rightClicked = false;

    public GuiScreen() {
        super(new LiteralText("ClickGUI"));
    }

    @Override
    protected void init() {
        int x = 20;
        for(Module.Category panel : Module.Category.values()){
            panels.add(new Panel(panel, x, 20));
            x += 90;
        }
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        for(Panel panel : panels){
            panel.render(matrices, mouseX, mouseY, delta, leftClicked, rightClicked);
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
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }

    public static class Panel extends TwoDRenderUtil {

        private int x;
        private int y;

        private final int width = 80;
        private final int height = 12;

        private final Module.Category category;
        private boolean expanded = true;

        public Panel(Module.Category category, int x, int y){
            this.category = category;
            this.x = x;
            this.y = y;
        }

        public void render(MatrixStack matrices, int mouseX, int mouseY, float delta, boolean leftClicked, boolean rightClicked){
            int level = 1;
            drawCenteredTextBox(
                    matrices,
                    category.name(),
                    new Rectangle(
                            x,
                            y,
                            width,
                            height
                    ),
                    0x8000FF00,
                    0xFFFFFFFF
            );
            if(mouseOverRect(mouseX, mouseY, new Rectangle(x, y, width, height))){
                if(rightClicked) {
                    expanded = !expanded;
                }
            }
            if(expanded){
                for(Module mod : ModuleManager.getModulesInCategory(category)){
                    if(mouseOverRect(mouseX, mouseY, moduleRect(level)) && leftClicked) mod.toggle();
                    drawTextBox(
                            matrices,
                            mod.name,
                            moduleRect(level),
                            (mod.enabled) ? 0x8000FF00 : 0x50000000,
                            0xFFFFFFFF
                    );
                    level++;
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

        private int yIteration(int iteration){
            return y + iteration + (height * iteration);
        }

        private Rectangle moduleRect(int iteration){
            return new Rectangle(
                    x + 1,
                    yIteration(iteration),
                    width - 2,
                    height
            );
        }
    }
}
