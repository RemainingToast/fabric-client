package me.remainingtoast.faxhax.api.events;

import net.minecraft.network.Packet;

public class SendPacketEvent extends Event {

    private final Packet<?> packet;

    public SendPacketEvent(Packet<?> packet){
        this.packet = packet;
    }

    public Packet<?> getPacket() {
        return packet;
    }

}
