package me.remainingtoast.faxhax.impl.modules.render;

import me.remainingtoast.faxhax.FaxHax;
import me.remainingtoast.faxhax.api.event.WorldRenderEvent;
import me.remainingtoast.faxhax.api.module.Module;
import me.remainingtoast.faxhax.api.setting.Setting;
import me.remainingtoast.faxhax.api.util.FaxColor;
import me.remainingtoast.faxhax.api.util.RenderUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

public class Tracers extends Module {

    Setting.Double width = number("Width", 1, 0.1, 2.5);

    public Tracers() {
        super("Tracers", Category.RENDER);
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
    private void onRender(WorldRenderEvent.Post event){

        assert mc.world != null;
        for (AbstractClientPlayerEntity player : mc.world.getPlayers()){
            if(player == mc.player) continue;
            Vec3d start = player.getPos();

            assert mc.cameraEntity != null;
            Vec3d end = new Vec3d(0, 0, 75)
                    .rotateX(-(float) Math.toRadians(mc.gameRenderer.getCamera().getPitch()))
                    .rotateY(-(float) Math.toRadians(mc.gameRenderer.getCamera().getYaw()))
                    .add(mc.cameraEntity.getPos().add(0, mc.cameraEntity.getEyeHeight(mc.cameraEntity.getPose()), 0));

            FaxColor col = FaxColor.rainbow();

            RenderUtils.drawLine(end.x, end.y, end.z, start.x, start.y, start.z, col.getRed(), col.getGreen(), col.getBlue(), width.getFloatValue());
            RenderUtils.drawLine(start.x, start.y, start.z, start.x, start.y + player.getHeight(), start.z, col.getRed(), col.getGreen(), col.getBlue(), width.getFloatValue());
        }

    }

}
