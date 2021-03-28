package me.remainingtoast.faxhax.mixin;

import me.remainingtoast.faxhax.api.module.ModuleManager;
import me.remainingtoast.faxhax.impl.modules.render.NoRender;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.WorldRenderer;
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
    public void renderWeather(LightmapTextureManager manager, float f, double d, double e, double g, CallbackInfo ci){
        if(noRender != null && noRender.enabled &&
                noRender.weather.getValue()){
            ci.cancel();
        }
    }
}
