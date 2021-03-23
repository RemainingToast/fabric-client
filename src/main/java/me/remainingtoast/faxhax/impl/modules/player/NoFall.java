package me.remainingtoast.faxhax.impl.modules.player;

import me.remainingtoast.faxhax.api.module.Module;
import me.remainingtoast.faxhax.api.setting.Setting;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class NoFall extends Module {

    Setting.Mode mode;

    private enum ModeEnum {
        Packet
    }

    public NoFall() {
        super("NoFall", Category.PLAYER);
        mode = mode("Mode", ModeEnum.values(), ModeEnum.Packet);
    }

    @Override
    protected void onTick() {
        assert mc.player != null;
        if(mode.getValue() == ModeEnum.Packet){
            if (mc.player.fallDistance <= 3f) return;
            mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket(true));
        }
    }
}
