package me.remainingtoast.faxhax.impl.modules.render;

import me.remainingtoast.faxhax.api.module.Module;
import me.remainingtoast.faxhax.api.setting.Setting;

public class CustomFOV extends Module {

    public Setting.Double fov = number("FOV", 110, 10, 179);
    public Setting.Boolean itemFov = bool("Items", true);

    public Setting.Double scaleX = number("X", 0.75, -1, 2);
    public Setting.Double scaleY = number("Y", 0.60, -1, 2);
    public Setting.Double scaleZ = number("Z", 1, -1, 2);
    Setting.Group scaleGroup = group("Scale", scaleX, scaleY, scaleZ);

    public Setting.Double posX = number("X", 0, -3, 3);
    public Setting.Double posY = number("Y", 0, -3, 3);
    public Setting.Double posZ = number("Z", -0.10, -3, 3);
    Setting.Group posGroup = group("Position", posX, posY, posZ);

    public Setting.Double rotationX = number("X", 0, -0.25, 0.25);
    public Setting.Double rotationY = number("Y", 0, -0.25, 0.25);
    public Setting.Double rotationZ = number("Z", 0, -0.25, 0.25);
    Setting.Group rotationGroup = group("Rotation", rotationX, rotationY, rotationZ);

    private int oldFov;

    public CustomFOV() {
        super("CustomFOV", Category.RENDER);
    }

    @Override
    protected void onToggle() {
        if(enabled) {
            oldFov = (int) mc.options.fov;
            updateFov();
        } else {
            mc.options.fov = oldFov;
        }
    }

    @Override
    protected void onTick() {
        updateFov();
    }

    private void updateFov(){
        if(mc.options.fov != fov.getValue()){
            mc.options.fov = fov.getValue();
        }
    }
}
