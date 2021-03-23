package me.remainingtoast.faxhax.impl.modules.client;

import me.remainingtoast.faxhax.api.gui.ClickGUI;
import me.remainingtoast.faxhax.api.module.Module;

public class ClickGUIModule extends Module {

    private final ClickGUI GUI = new ClickGUI();

    public ClickGUIModule() {
        super("ClickGUI", Category.CLIENT);
        setKey(344, -1);
    }

    @Override
    protected void onToggle() {
        if(enabled && (mc.currentScreen == null || mc.currentScreen.equals(GUI))) {
            mc.openScreen(GUI);
            disable();
        } else {
            GUI.onClose();
        }
    }
}
