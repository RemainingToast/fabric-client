package club.faxhax.client.impl.modules.misc;

import club.faxhax.client.FaxHax;
import club.faxhax.client.api.event.PacketEvent;
import club.faxhax.client.api.module.Module;
import club.faxhax.client.api.setting.Setting;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.network.packet.s2c.play.*;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;

import java.util.Arrays;
import java.util.List;

public class PacketLogger extends Module {

    Setting.Boolean verbose = bool("Verbose", true);
    Setting.Boolean incoming = bool("Incoming", true);
    Setting.Boolean outgoing = bool("Outgoing", true);
    Setting.Boolean hideCommon = bool("HideCommon", true);

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
        if(mc.player != null && incoming.enabled()){
            if(hideCommon.enabled()){
                if(COMMON_PACKETS.contains(event.getPacket().getClass())) return;
            }
            message(packetText(event.getPacket(), true));
            if(verbose.enabled()) System.out.println("Incoming Packet : " + event.getPacket().getClass().getSimpleName());
        }
    }

    @EventHandler
    private void onPacket(PacketEvent.Send event){
        if(mc.player != null && outgoing.enabled()){
            if(hideCommon.enabled()){
                if(COMMON_PACKETS.contains(event.getPacket().getClass())) return;
            }
            message(packetText(event.getPacket(), false));
            if(verbose.enabled()) System.out.println("Outgoing Packet : " + event.getPacket().getClass().getSimpleName());
        }
    }

    private MutableText packetText(Packet<?> packet, boolean incoming){
        MutableText text = new LiteralText(PREFIX);
        text.append(new LiteralText(Formatting.GRAY + "Packet " + ((incoming) ? "Received" : "Sent"))
                .setStyle(Style.EMPTY
                        .withHoverEvent(
                                new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                        new LiteralText(Formatting.DARK_AQUA +
                                                "Packet: " +
                                                Formatting.GREEN +
                                                packet.getClass().getSimpleName()
                                        )
                                )
                        )
                )
        );
        return text;
    }

    private static final List<Class> COMMON_PACKETS = Arrays.asList(new Class[] {
            WorldTimeUpdateS2CPacket.class,
            ChunkDataS2CPacket.class,
            PlayerListHeaderS2CPacket.class,
            ChatMessageC2SPacket.class,
            UnloadChunkS2CPacket.class,
            EntityAttributesS2CPacket.class,
            PlayerListS2CPacket.class,
            EntityS2CPacket.RotateAndMoveRelative.class,
            EntityS2CPacket.MoveRelative.class,
            EntityS2CPacket.Rotate.class,
            EntityS2CPacket.class,
            EntitySetHeadYawS2CPacket.class,
            EntityVelocityUpdateS2CPacket.class
        }
    );
}
