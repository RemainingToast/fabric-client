package club.faxhax.client.impl.modules.movement;

import club.faxhax.client.api.event.PacketEvent;
import club.faxhax.client.api.module.Module;
import club.faxhax.client.api.setting.Setting;
import club.faxhax.client.mixin.IEntityVelocityUpdateS2CPacket;
import club.faxhax.client.mixin.IExplosionS2CPacket;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;

public class Velocity extends Module {

    Setting.Double horizontal = number("Horizontal%", 0.0,0.0,100.0);
    Setting.Double vertical = number("Vertical%",  0.0, 0.0,100.0);

    public Velocity() {
        super("Velocity", Category.MOVEMENT);
    }

    @EventHandler
    private void onPacket(PacketEvent.Receive event) {
        if(mc.player != null) {
            Packet<?> p = event.getPacket();
            if(p instanceof EntityVelocityUpdateS2CPacket){
                EntityVelocityUpdateS2CPacket packet = (EntityVelocityUpdateS2CPacket) event.getPacket();
                if(packet.getId() == mc.player.getEntityId()){
                    if(horizontal.getValue() == 0 && vertical.getValue() == 0) event.cancel();
                    IEntityVelocityUpdateS2CPacket xyz = (IEntityVelocityUpdateS2CPacket) packet;
                    xyz.setVelocityX((int) (xyz.getVelocityX() * horizontal.getValue()));
                    xyz.setVelocityY((int) (xyz.getVelocityY() * vertical.getValue()));
                    xyz.setVelocityZ((int) (xyz.getVelocityZ() * horizontal.getValue()));
                }
            } else if(p instanceof ExplosionS2CPacket){
                if(horizontal.getValue() == 0 && vertical.getValue() == 0) event.cancel();
                IExplosionS2CPacket xyz = (IExplosionS2CPacket) event.getPacket();
                xyz.setPlayerVelocityX((float) (xyz.getPlayerVelocityX() * horizontal.getValue()));
                xyz.setPlayerVelocityY((float) (xyz.getPlayerVelocityY() * vertical.getValue()));
                xyz.setPlayerVelocityZ((float) (xyz.getPlayerVelocityZ() * horizontal.getValue()));
            }
        }
    }
}
