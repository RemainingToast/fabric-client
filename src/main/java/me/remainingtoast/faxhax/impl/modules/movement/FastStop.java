package me.remainingtoast.faxhax.impl.modules.movement;

import me.remainingtoast.faxhax.api.module.Module;
import me.remainingtoast.faxhax.api.setting.Setting;

public class FastStop extends Module {

    Setting.Boolean airStop = bool("Air", true);

    public FastStop() {
        super("FastStop", Category.MOVEMENT);
    }

    @Override
    protected void onTick() {
        assert mc.player != null;
        if(!mc.options.keyForward.isPressed() && !mc.options.keyBack.isPressed() && !mc.options.keyLeft.isPressed() && !mc.options.keyRight.isPressed()) {
            if(mc.player.isOnGround() || airStop.getValue()){
                mc.player.setVelocity(0.0, mc.player.getVelocity().y, 0.0);
            }
        }
    }
}
