package me.remainingtoast.faxhax.impl.modules.client;

import me.remainingtoast.faxhax.api.gui.GuiScreen;
import me.remainingtoast.faxhax.api.module.Module;
import me.remainingtoast.faxhax.api.setting.Setting;
import me.remainingtoast.faxhax.api.util.FaxColor;

import java.util.Objects;

public class ClickGUI extends Module {

    public ClickGUI() {
        super("ClickGUI", Category.CLIENT);
        setKey(344, -1);
    }

    @Override
    protected void onToggle() {
        GuiScreen gui = new GuiScreen();
        if(enabled && (mc.currentScreen == null || Objects.equals(mc.currentScreen, gui))) {
            mc.openScreen(gui);
        }
        else closeScreen();
    }
}
