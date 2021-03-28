package me.remainingtoast.faxhax.impl.modules.misc;

import me.remainingtoast.faxhax.FaxHax;
import me.remainingtoast.faxhax.api.event.PacketEvent;
import me.remainingtoast.faxhax.api.module.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.s2c.play.*;

import java.util.Arrays;
import java.util.List;

public class PacketLogger extends Module {
    public PacketLogger() {
        super("PacketLogger", Category.MISC);
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
        if(!COMMON_PACKETS.contains(event.getPacket().getClass())){
            System.out.println(event.getPacket());
        }
    }

    private static final List<Class> COMMON_PACKETS = Arrays.asList(new Class[] {
            WorldTimeUpdateS2CPacket.class,
            ChunkDataS2CPacket.class,
            PlayerListHeaderS2CPacket.class,
            ChatMessageC2SPacket.class,
            UnloadChunkS2CPacket.class,
            EntityAttributesS2CPacket.class,
            PlayerListS2CPacket.class,
            EntityS2CPacket.class,
            EntitySetHeadYawS2CPacket.class,
            EntityVelocityUpdateS2CPacket.class,
        }
    );
}
