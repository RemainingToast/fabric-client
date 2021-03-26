package me.remainingtoast.faxhax.api.events;

import net.minecraft.network.Packet;

public class ReceivePacketEvent extends Event {

    private final Packet<?> packet;

    public ReceivePacketEvent(Packet<?> packet){
        this.packet = packet;
    }

    public Packet<?> getPacket() {
        return packet;
    }

}
