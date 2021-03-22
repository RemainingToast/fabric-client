package me.remainingtoast.faxhax.impl.modules.client;

import me.remainingtoast.faxhax.api.module.Module;
import me.remainingtoast.faxhax.api.setting.Setting;

import java.util.ArrayList;
import java.util.List;

public class MainMenu extends Module {

    Setting.Mode mainMenuMode;

    public MainMenu() {
        super("MainMenu", Category.CLIENT);

        List<String> modes = new ArrayList<>();

        modes.add("FaxHax");

        mainMenuMode = aMode("Shader", modes,"FaxHax");

        setEnabled(true);
    }
}
