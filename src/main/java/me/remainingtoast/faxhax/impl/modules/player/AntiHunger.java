package me.remainingtoast.faxhax.impl.modules.player;

import me.remainingtoast.faxhax.FaxHax;
import me.remainingtoast.faxhax.api.event.PacketEvent;
import me.remainingtoast.faxhax.api.module.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class AntiHunger extends Module {

    public AntiHunger() {
        super("AntiHunger", Category.PLAYER);
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
        if (mc.player != null && event.getPacket() instanceof PlayerMoveC2SPacket) event.cancel();
    }
}
