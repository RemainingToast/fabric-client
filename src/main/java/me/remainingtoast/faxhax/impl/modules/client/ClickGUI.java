package me.remainingtoast.faxhax.impl.modules.client;

import me.remainingtoast.faxhax.api.gui.GuiScreen;
import me.remainingtoast.faxhax.api.module.Module;

public class ClickGUI extends Module {

    public ClickGUI() {
        super("ClickGUI", Category.CLIENT);
        setKey(344, -1);
    }

    @Override
    protected void onToggle() {
        if(enabled) openScreen(new GuiScreen());
        else closeScreen();
    }
}
