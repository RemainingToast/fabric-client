package club.faxhax.client.impl.modules.player;

import club.faxhax.client.api.module.Module;
import club.faxhax.client.api.setting.Setting;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class NoFall extends Module {

    Setting.Mode mode = mode("Mode", "Packet", "Packet");

    public NoFall() {
        super("NoFall", Category.PLAYER);
    }

    @Override
    protected void onTick() {
        assert mc.player != null;
        if(mode.toggled("Packet")){
            if (mc.player.fallDistance <= 3f) return;
            mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket(true));
        }
    }
}
