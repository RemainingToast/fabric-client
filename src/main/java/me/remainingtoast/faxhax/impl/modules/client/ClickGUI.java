package me.remainingtoast.faxhax.impl.modules.client;

import me.remainingtoast.faxhax.api.gui.GuiScreen;
import me.remainingtoast.faxhax.api.module.Module;
import me.remainingtoast.faxhax.api.setting.Setting;
import me.remainingtoast.faxhax.api.util.FaxColor;

import java.util.Objects;

public class ClickGUI extends Module {

//    public static Setting.ColorSetting backgroundColor;

    public ClickGUI() {
        super("ClickGUI", Category.CLIENT);

//        backgroundColor = aColor("Background", new FaxColor(0, 0, 0));

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
