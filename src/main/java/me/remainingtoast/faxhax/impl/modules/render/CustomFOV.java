package me.remainingtoast.faxhax.impl.modules.render;

import me.remainingtoast.faxhax.api.module.Module;
import me.remainingtoast.faxhax.api.setting.Setting;

public class CustomFOV extends Module {

    public Setting.Boolean itemFov;
    public Setting.Double fov;
    public Setting.Double scaleX;
    public Setting.Double scaleY;
    public Setting.Double scaleZ;
    public Setting.Double posX;
    public Setting.Double posY;
    public Setting.Double posZ;
    public Setting.Double rotationX;
    public Setting.Double rotationY;
    public Setting.Double rotationZ;

    private int oldFov;

    public CustomFOV() {
        super("CustomFOV", Category.RENDER);
        fov = number("FOV", 110, 10, 179);
        itemFov = bool("Items", true);
        scaleX = number("ScaleX", 0.75, -1, 2);
        scaleY = number("ScaleY", 0.60, -1, 2);
        scaleZ = number("ScaleZ", 1, -1, 2);
        posX = number("PosX", 0, -3, 3);
        posY = number("PosY", 0, -3, 3);
        posZ = number("PosZ", -0.10, -3, 3);
        rotationX = number("RotationX", 0, -0.25, 0.25);
        rotationY = number("RotationY", 0, -0.25, 0.25);
        rotationZ = number("RotationZ", 0, -0.25, 0.25);
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
