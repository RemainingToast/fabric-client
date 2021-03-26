package me.remainingtoast.faxhax.impl.modules.misc;

import me.remainingtoast.faxhax.FaxHax;
import me.remainingtoast.faxhax.api.module.Module;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;

public class PacketLogger extends Module {
    public PacketLogger() {
        super("PacketLogger", Category.MISC);
    }

    @Override
    protected void onToggle() {

    }

}
