package me.remainingtoast.faxhax.impl.modules.movement;

import me.remainingtoast.faxhax.FaxHax;
import me.remainingtoast.faxhax.api.events.Event;
import me.remainingtoast.faxhax.api.events.ReceivePacketEvent;
import me.remainingtoast.faxhax.api.module.Module;
import me.remainingtoast.faxhax.api.setting.Setting;
import me.remainingtoast.faxhax.mixin.IEntityVelocityUpdateS2CPacket;
import me.remainingtoast.faxhax.mixin.IExplosionS2CPacket;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import net.minecraft.util.math.Vec3d;

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

    @Override
    protected void onToggle() {
        if(enabled) FaxHax.EVENTS.subscribe(packetEvent);
        else FaxHax.EVENTS.unsubscribe(packetEvent);
    }

    @EventHandler
    public Listener<ReceivePacketEvent> packetEvent = new Listener<>(event -> {
        if(mc.player != null){
            System.out.println(event.getPacket().getClass().getSimpleName());
            if(event.getEra() == Event.Era.PRE){
                Vec3d oldVelocity = new Vec3d(0, 0, 0);
                if(event.getPacket() instanceof EntityVelocityUpdateS2CPacket){
                    EntityVelocityUpdateS2CPacket packet = (EntityVelocityUpdateS2CPacket) event.getPacket();
                    if(packet.getId() == mc.player.getEntityId()){
                        System.out.println("Velocity Packet");
                        if(horizontal.getValue() == 0 && vertical.getValue() == 0) event.cancel();
                        IEntityVelocityUpdateS2CPacket xyz = (IEntityVelocityUpdateS2CPacket) packet;
                        if(delay.getValue() > 0){
                            oldVelocity = new Vec3d(
                                    (xyz.getVelocityX() * horizontal.getValue()) / 8000.0,
                                    (xyz.getVelocityY() * vertical.getValue()) / 8000.0,
                                    (xyz.getVelocityZ() * horizontal.getValue()) / 8000.0
                            );
                            event.cancel();
                        } else {
                            xyz.setVelocityX((int) (xyz.getVelocityX() * horizontal.getValue()));
                            xyz.setVelocityY((int) (xyz.getVelocityY() * vertical.getValue()));
                            xyz.setVelocityZ((int) (xyz.getVelocityZ() * horizontal.getValue()));
                        }
                    }
                } else if(event.getPacket() instanceof ExplosionS2CPacket){
                    System.out.println("Explosion Packet");
                    if(horizontal.getValue() == 0 && vertical.getValue() == 0) event.cancel();
                    IExplosionS2CPacket xyz = (IExplosionS2CPacket) event.getPacket();
                    if(delay.getValue() > 0) {
                        oldVelocity = new Vec3d(
                                xyz.getPlayerVelocityX(),
                                xyz.getPlayerVelocityY(),
                                xyz.getPlayerVelocityZ()
                        );
                        event.cancel();
                    } else {
                        xyz.setPlayerVelocityX((float) (xyz.getPlayerVelocityX() * horizontal.getValue()));
                        xyz.setPlayerVelocityY((float) (xyz.getPlayerVelocityY() * vertical.getValue()));
                        xyz.setPlayerVelocityZ((float) (xyz.getPlayerVelocityZ() * horizontal.getValue()));
                    }
                }
                if(delay.getValue() > 0 && oldVelocity.x != 0 || oldVelocity.y != 0 || oldVelocity.z != 0){
                    mc.player.setVelocity(mc.player.getVelocity().add(oldVelocity));
                }
            }
        }
    });
}
