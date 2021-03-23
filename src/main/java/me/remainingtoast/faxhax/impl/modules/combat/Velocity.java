package me.remainingtoast.faxhax.impl.modules.combat;

import me.remainingtoast.faxhax.FaxHax;
import me.remainingtoast.faxhax.api.events.Event;
import me.remainingtoast.faxhax.api.events.PacketEvent;
import me.remainingtoast.faxhax.api.module.Module;
import me.remainingtoast.faxhax.api.setting.Setting;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.EventHook;
import me.zero.alpine.listener.Listener;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.util.math.Vec3d;

public class Velocity extends Module {

    Setting.Double horizontal;
    Setting.Double vertical;
    Setting.Integer delay;

    public Velocity() {
        super("Velocity", Category.COMBAT);
        horizontal = aDouble("Horizontal%", 0.0,0.0,100.0);
        vertical = aDouble("Vertical%",  0.0, 0.0,100.0);
        delay = aInteger("Delay(MS)",  170, 0, 1000);
    }

    @Override
    protected void onEnable() {
        FaxHax.EVENTS.subscribe(packetEvent);
    }

    @Override
    protected void onDisable() {
        FaxHax.EVENTS.unsubscribe(packetEvent);
    }

    private Vec3d oldVelocity = new Vec3d(0,0,0);

    @EventHandler
    private Listener<PacketEvent.Receive> packetEvent = new Listener<>(event -> {
        assert mc.player != null;
        if(event.getEra() == Event.Era.PRE){
            oldVelocity = new Vec3d(0,0,0);
            if(event.getPacket() instanceof EntityVelocityUpdateS2CPacket){
                EntityVelocityUpdateS2CPacket packet = (EntityVelocityUpdateS2CPacket) event.getPacket();
                if(packet.getId() == mc.player.getEntityId()){
                    if(horizontal.getValue() == 0 && vertical.getValue() == 0) event.cancel();

                }
            }
        }
    });

}
