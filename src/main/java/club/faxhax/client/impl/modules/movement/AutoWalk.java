package club.faxhax.client.impl.modules.movement;

import club.faxhax.client.api.module.Module;
import club.faxhax.client.api.setting.Setting;

public class AutoWalk extends Module {

    Setting.Mode mode = mode("Mode", "Simple", "Simple", "Baritone")
            .onChanged(mode1 -> {
                setHidden(mode1.getValue().equalsIgnoreCase("Baritone"));
                if(isEnabled()) stop();
            });

    Setting.Mode direction = mode("Direction", "Forward", "Forward", "Backward", "Leftward", "Rightward")
            .onChanged(direction1 -> {
                if(isEnabled()) stop();
            });


    public AutoWalk() {
        super("AutoWalk", Category.MOVEMENT);
    }

    @Override
    protected void onDisable() {
        if(mc.player != null){
            if(mode.getValue().equalsIgnoreCase("Simple")){
                stop();
            }
        }
    }

    @Override
    protected void onTick() {
        assert mc.player != null;
        if(mode.getValue().equalsIgnoreCase("Simple")) {
            switch (direction.getValue()) {
                case "Forward":
                    mc.options.keyForward.setPressed(true);
                    break;
                case "Backward":
                    mc.options.keyBack.setPressed(true);
                    break;
                case "Leftward":
                    mc.options.keyLeft.setPressed(true);
                    break;
                case "Rightward":
                    mc.options.keyRight.setPressed(true);
                    break;
            }
        }
    }

    private void stop(){
        mc.options.keyForward.setPressed(false);
        mc.options.keyBack.setPressed(false);
        mc.options.keyLeft.setPressed(false);
        mc.options.keyRight.setPressed(false);
    }

    private void setHidden(boolean bool){
        direction.setHidden(bool);
    }
}
