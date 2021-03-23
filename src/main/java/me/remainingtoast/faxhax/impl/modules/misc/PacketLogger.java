package me.remainingtoast.faxhax.impl.modules.misc;

import me.remainingtoast.faxhax.FaxHax;
import me.remainingtoast.faxhax.api.events.PacketEvent;
import me.remainingtoast.faxhax.api.module.Module;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;

public class PacketLogger extends Module {
    public PacketLogger() {
        super("PacketLogger", Category.MISC);
    }

    @Override
    protected void onToggle() {
        if(enabled) {
            System.out.println("on");
            FaxHax.EVENTS.subscribeAll(receivePacket, sendPacket);
        } else {
            FaxHax.EVENTS.unsubscribeAll(receivePacket, sendPacket);
            System.out.println("off");
        }
    }

    @EventHandler
    public Listener<PacketEvent.Receive> receivePacket = new Listener<>(event -> {
        System.out.println("in: " + event.getPacket().toString());
    });

    // TODO PACKET LOGGER w/ SETTINGS

    @EventHandler
    public Listener<PacketEvent.Send> sendPacket = new Listener<>(event -> {
        System.out.println("out: " + event.getPacket().toString());
    });
}
