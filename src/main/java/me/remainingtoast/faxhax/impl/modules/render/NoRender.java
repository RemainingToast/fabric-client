package me.remainingtoast.faxhax.impl.modules.render;

import me.remainingtoast.faxhax.api.module.Module;
import me.remainingtoast.faxhax.api.setting.Setting;

public class NoRender extends Module {

//    public Setting.Boolean explosions = bool("Explosions", true);
    public Setting.Boolean totem = bool("Totem", true);
    public Setting.Boolean fire = bool("Fire", true);
    public Setting.Boolean underwater = bool("Underwater", true);
    public Setting.Boolean nausea = bool("Nausea", true);
    public Setting.Boolean bossbar = bool("Bossbar", true);
    public Setting.Boolean pumpkin = bool("Pumpkin", true);
    public Setting.Boolean blindness = bool("Blindness", true);
    public Setting.Boolean hurtcam = bool("HurtCam", true);
//    public Setting.Boolean skylight = bool("Skylight", true);
    public Setting.Boolean fog = bool("Fog", true);
//    public Setting.Boolean xp = bool("XP Orbs", true);

    // TODO SETUP PACKET EVENT FOR COMMENTED SETTINGS

    public NoRender() {
        super("NoRender", Category.RENDER);
    }
    
}
