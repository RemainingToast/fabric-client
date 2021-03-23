package me.remainingtoast.faxhax.mixin;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import me.remainingtoast.faxhax.FaxHax;
import me.remainingtoast.faxhax.api.events.PacketEvent;
import me.remainingtoast.faxhax.api.module.Module;
import me.remainingtoast.faxhax.api.module.ModuleManager;
import me.remainingtoast.faxhax.api.util.TickRateCalculator;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.s2c.login.LoginSuccessS2CPacket;
import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(ClientConnection.class)
public class MixinClientConnection {

    @Inject(
            at = {@At("HEAD")},
            method = {"handlePacket"},
            cancellable = true
    )
    private static void handlePacket(Packet<?> packet, PacketListener listener, CallbackInfo ci){
        final Module packetLogger = ModuleManager.getModule("PacketLogger");
        if(packetLogger != null && packetLogger.enabled) FaxHax.LOGGER.info("PacketLogger: Receive: { " + packet + " }");

        if(packet.getClass() == WorldTimeUpdateS2CPacket.class) TickRateCalculator.reportReceivedPacket();
        else if(packet.getClass() == LoginSuccessS2CPacket.class) TickRateCalculator.reset();

        PacketEvent.Receive receive = new PacketEvent.Receive(packet);
        FaxHax.EVENTS.post(receive);
        if(receive.isCancelled()) ci.cancel();
    }

    @Inject(
            at = {@At("HEAD")},
            method = {"sendImmediately"},
            cancellable = true
    )
    private void sendImmediately(Packet<?> packet, GenericFutureListener<? extends Future<? super Void>> callback, CallbackInfo ci){
        final Module packetLogger = ModuleManager.getModule("PacketLogger");
        if(packetLogger != null && packetLogger.enabled) FaxHax.LOGGER.info("PacketLogger: Send: { " + packet + " }");

        PacketEvent.Send send = new PacketEvent.Send(packet);
        FaxHax.EVENTS.post(send);
        if(send.isCancelled()) ci.cancel();
    }

}
