package me.remainingtoast.faxhax.impl.modules.client;

import me.remainingtoast.faxhax.api.module.Module;
import me.remainingtoast.faxhax.api.setting.Setting;

public class MainMenu extends Module {

    Setting.Mode mainMenuMode;

    public MainMenu() {
        super("MainMenu", Category.CLIENT);

        mainMenuMode = mode("Shader", "FaxHax", "FaxHax");

        setEnabled(true);
    }
}
