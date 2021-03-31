package club.faxhax.client.impl.modules.client;

import club.faxhax.client.api.module.Module;
import club.faxhax.client.api.setting.Setting;

public class MainMenu extends Module {

    Setting.Mode mainMenuMode = mode("Shader", "FaxHax", "FaxHax");

    public MainMenu() {
        super("MainMenu", Category.CLIENT);
        setEnabled(true);
    }
}
