package me.remainingtoast.faxhax.impl.modules.movement;

import me.remainingtoast.faxhax.api.module.Module;
import me.remainingtoast.faxhax.api.setting.Setting;

public class AutoWalk extends Module {

    Setting.Mode mode = mode("Mode", "Simple", "Simple", "Baritone");
    Setting.Mode direction = mode("Direction", "Forward", "Forward", "Backward", "Leftward", "Rightward");

    public AutoWalk() {
        super("AutoWalk", Category.MOVEMENT);
    }

    @Override
    protected void onDisable() {
        if(mode.getValue().equalsIgnoreCase("Simple")){
            stop();
        }
    }

    @Override
    protected void onTick() {
        if(mode.getValue().equalsIgnoreCase("Simple")) {
            direction.setHidden(false);
            switch (direction.getValue()) {
                case "Forward":
                    stop();
                    mc.options.keyForward.setPressed(true);
                    break;
                case "Backward":
                    stop();
                    mc.options.keyBack.setPressed(true);
                    break;
                case "Leftward":
                    stop();
                    mc.options.keyLeft.setPressed(true);
                    break;
                case "Rightward":
                    stop();
                    mc.options.keyRight.setPressed(true);
                    break;
            }
        } else {
            direction.setHidden(true);
            stop();
        }
    }

    private void stop(){
        mc.options.keyForward.setPressed(false);
        mc.options.keyBack.setPressed(false);
        mc.options.keyLeft.setPressed(false);
        mc.options.keyRight.setPressed(false);
    }
}
