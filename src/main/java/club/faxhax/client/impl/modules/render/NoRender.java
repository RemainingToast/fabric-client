package club.faxhax.client.impl.modules.render;

import club.faxhax.client.FaxHax;
import club.faxhax.client.api.event.PacketEvent;
import club.faxhax.client.api.module.Module;
import club.faxhax.client.api.setting.Setting;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.ExperienceOrbSpawnS2CPacket;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import net.minecraft.network.packet.s2c.play.LightUpdateS2CPacket;

public class NoRender extends Module {

    public Setting.Boolean explosions = bool("Explosions", true);
    public Setting.Boolean totem = bool("Totem", true);
    public Setting.Boolean fire = bool("Fire", true);
    public Setting.Boolean underwater = bool("Underwater", true);
    public Setting.Boolean nausea = bool("Nausea", true);
    public Setting.Boolean bossbar = bool("Bossbar", true);
    public Setting.Boolean pumpkin = bool("Pumpkin", true);
    public Setting.Boolean blindness = bool("Blindness", true);
    public Setting.Boolean hurtcam = bool("HurtCam", true);
    public Setting.Boolean skylight = bool("Skylight", true);
    public Setting.Boolean weather = bool("Weather", true);
    public Setting.Boolean fog = bool("Fog", true);
    public Setting.Boolean xp = bool("XP Orbs", true);

    public NoRender() {
        super("NoRender", Category.RENDER);
    }

    @Override
    protected void onEnable() {
        FaxHax.EVENT_BUS.subscribe(this);
    }

    @Override
    protected void onDisable() {
        FaxHax.EVENT_BUS.unsubscribe(this);
    }

    @EventHandler
    private void onPacket(PacketEvent.Receive event){
        Packet<?> packet = event.getPacket();
        if(packet instanceof ExplosionS2CPacket && explosions.getValue()) event.cancel();
        else if(packet instanceof LightUpdateS2CPacket && skylight.getValue()) event.cancel();
        else if(packet instanceof ExperienceOrbSpawnS2CPacket && xp.getValue()) event.cancel();
    }
}
