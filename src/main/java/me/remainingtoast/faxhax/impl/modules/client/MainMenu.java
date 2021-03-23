package me.remainingtoast.faxhax.impl.modules.client;

import com.sun.org.apache.xpath.internal.operations.Mod;
import me.remainingtoast.faxhax.api.module.Module;
import me.remainingtoast.faxhax.api.setting.Setting;

import java.util.ArrayList;
import java.util.List;

public class MainMenu extends Module {

    Setting.Mode mainMenuMode;

    private enum ModeEnum {
        FaxHax
    }

    public MainMenu() {
        super("MainMenu", Category.CLIENT);

        mainMenuMode = mode("Shader", ModeEnum.values(), ModeEnum.FaxHax);

        setEnabled(true);
    }
}
