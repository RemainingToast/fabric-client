package me.remainingtoast.faxhax.mixin;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import me.remainingtoast.faxhax.FaxHax;
import me.remainingtoast.faxhax.api.events.PacketEvent;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.PacketListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConnection.class)
public class MixinClientConnection {

    @Inject(
            at = {@At("HEAD")},
            method = {"handlePacket"},
            cancellable = true
    )
    private static void handlePacket(Packet<?> packet, PacketListener listener, CallbackInfo ci){
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
        PacketEvent.Send send = new PacketEvent.Send(packet);
        FaxHax.EVENTS.post(send);
        if(send.isCancelled()) ci.cancel();
    }

}