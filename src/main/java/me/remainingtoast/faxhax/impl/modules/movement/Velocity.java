package me.remainingtoast.faxhax.impl.modules.movement;

import me.remainingtoast.faxhax.FaxHax;
import me.remainingtoast.faxhax.api.event.PacketEvent;
import me.remainingtoast.faxhax.api.module.Module;
import me.remainingtoast.faxhax.api.setting.Setting;
import me.remainingtoast.faxhax.mixin.IEntityVelocityUpdateS2CPacket;
import me.remainingtoast.faxhax.mixin.IExplosionS2CPacket;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;

public class Velocity extends Module {

    Setting.Double horizontal;
    Setting.Double vertical;

    public Velocity() {
        super("Velocity", Category.MOVEMENT);
        horizontal = number("Horizontal%", 0.0,0.0,100.0);
        vertical = number("Vertical%",  0.0, 0.0,100.0);
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
    private void onPacket(PacketEvent.Receive event) {
        if(mc.player != null) {
            Packet<?> p = event.getPacket();
            if(p instanceof EntityVelocityUpdateS2CPacket){
                EntityVelocityUpdateS2CPacket packet = (EntityVelocityUpdateS2CPacket) event.getPacket();
                if(packet.getId() == mc.player.getEntityId()){
                    System.out.println("Velocity Packet");
                    if(horizontal.getValue() == 0 && vertical.getValue() == 0) event.cancel();
                    IEntityVelocityUpdateS2CPacket xyz = (IEntityVelocityUpdateS2CPacket) packet;
                    xyz.setVelocityX((int) (xyz.getVelocityX() * horizontal.getValue()));
                    xyz.setVelocityY((int) (xyz.getVelocityY() * vertical.getValue()));
                    xyz.setVelocityZ((int) (xyz.getVelocityZ() * horizontal.getValue()));
                }
            } else if(p instanceof ExplosionS2CPacket){
                System.out.println("Explosion Packet");
                if(horizontal.getValue() == 0 && vertical.getValue() == 0) event.cancel();
                IExplosionS2CPacket xyz = (IExplosionS2CPacket) event.getPacket();
                xyz.setPlayerVelocityX((float) (xyz.getPlayerVelocityX() * horizontal.getValue()));
                xyz.setPlayerVelocityY((float) (xyz.getPlayerVelocityY() * vertical.getValue()));
                xyz.setPlayerVelocityZ((float) (xyz.getPlayerVelocityZ() * horizontal.getValue()));
            }
        }
    }
}
