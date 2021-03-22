package me.remainingtoast.faxhax.api.gui;

import me.remainingtoast.faxhax.api.module.Module;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;

import java.util.ArrayList;
import java.util.List;

public class ClickGUI extends Screen {

    private final List<Panel> panels = new ArrayList<>();

    private boolean clicked = false;
    private boolean leftClicked = false;
    private boolean rightClicked = false;

    public ClickGUI() {
        super(new LiteralText("ClickGUI"));
    }

    @Override
    protected void init() {
        int x = 20;
        for(Module.Category category : Module.Category.values()){
            panels.add(new Panel(category, x, 20));
            x += 93;
        }
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        for(Panel panel : panels) {
            panel.render(matrices, mouseX, mouseY, leftClicked, rightClicked, delta);
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

}
