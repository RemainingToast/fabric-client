package club.faxhax.client.mixin;

import club.faxhax.client.FaxHax;
import club.faxhax.client.api.module.ModuleManager;
import club.faxhax.client.impl.modules.render.NoRender;
import club.faxhax.client.api.event.WorldRenderEvent;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class MixinWorldRenderer {

    private final NoRender noRender = (NoRender) ModuleManager.getModule("NoRender");

    @Inject(
            at = {@At("HEAD")},
            method = {"renderWeather"},
            cancellable = true
    )
    private void renderWeather(LightmapTextureManager manager, float f, double d, double e, double g, CallbackInfo ci){
        if(noRender != null && noRender.enabled &&
                noRender.weather.getValue()){
            ci.cancel();
        }
    }

    @Inject(
            at = {@At("HEAD")},
            method = {"render"},
            cancellable = true
    )
    private void renderAtHead(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f, CallbackInfo ci){
        WorldRenderEvent.Pre event = new WorldRenderEvent.Pre(tickDelta);
        FaxHax.EVENT_BUS.post(event);
        if(event.isCancelled()) ci.cancel();
    }

    @Inject(
            at = {@At("RETURN")},
            method = {"render"}
    )
    private void renderAtReturn(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f, CallbackInfo ci){
        WorldRenderEvent.Post event = new WorldRenderEvent.Post(tickDelta);
        FaxHax.EVENT_BUS.post(event);
    }
}
