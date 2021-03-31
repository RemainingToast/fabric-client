package club.faxhax.client.mixin;

import club.faxhax.client.api.module.ModuleManager;
import club.faxhax.client.impl.modules.render.NoRender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameOverlayRenderer.class)
public class MixinInGameOverlayRenderer {

    private static final NoRender noRender = (NoRender) ModuleManager.getModule("NoRender");

    @Inject(
            at = @At("HEAD"),
            method = {"renderFireOverlay"},
            cancellable = true
    )
    private static void onRenderFireOverlay(MinecraftClient minecraftClient, MatrixStack matrixStack, CallbackInfo ci) {
        if(noRender != null && noRender.enabled &&
                noRender.fire.getValue()){
            ci.cancel();
        }
    }

    @Inject(
            at = {@At("HEAD")},
            method = {"renderUnderwaterOverlay"},
            cancellable = true
    )
    private static void onRenderUnderwaterOverlay(MinecraftClient minecraftClient, MatrixStack matrixStack, CallbackInfo ci) {
        if(noRender != null && noRender.enabled &&
                noRender.underwater.getValue()){
            ci.cancel();
        }
    }
}
