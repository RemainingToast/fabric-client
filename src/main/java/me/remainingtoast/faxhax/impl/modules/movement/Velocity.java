package me.remainingtoast.faxhax.impl.modules.movement;

import me.remainingtoast.faxhax.api.module.Module;
import me.remainingtoast.faxhax.api.setting.Setting;

public class Velocity extends Module {

    Setting.Double horizontal;
    Setting.Double vertical;
    Setting.Double delay;

    public Velocity() {
        super("Velocity", Category.MOVEMENT);
        horizontal = number("Horizontal%", 0.0,0.0,100.0);
        vertical = number("Vertical%",  0.0, 0.0,100.0);
        delay = number("Delay(MS)",  170, 0, 1000);
    }

}
