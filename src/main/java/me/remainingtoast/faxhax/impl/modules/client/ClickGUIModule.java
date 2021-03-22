package me.remainingtoast.faxhax.impl.modules.client;

import me.remainingtoast.faxhax.api.gui.ClickGUI;
import me.remainingtoast.faxhax.api.module.Module;

import java.util.Objects;

public class ClickGUIModule extends Module {

    public ClickGUIModule() {
        super("ClickGUI", Category.CLIENT);
        setKey(344, -1);
    }

    @Override
    protected void onToggle() {
        ClickGUI gui = new ClickGUI();
        if(enabled && (mc.currentScreen == null || Objects.equals(mc.currentScreen, gui))) {
            mc.openScreen(gui);
        }
        else closeScreen();
    }
}
