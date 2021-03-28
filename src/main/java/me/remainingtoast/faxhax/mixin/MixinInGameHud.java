package me.remainingtoast.faxhax.mixin;

import me.remainingtoast.faxhax.api.module.ModuleManager;
import me.remainingtoast.faxhax.impl.modules.render.NoRender;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class MixinInGameHud {

    private final NoRender noRender = (NoRender) ModuleManager.getModule("NoRender");

    @Inject(
         at = {@At("HEAD")},
         method = {"renderPumpkinOverlay"},
         cancellable = true
    )
    private void onRenderPumpkinOverlay(CallbackInfo ci) {
        if(noRender != null && noRender.enabled &&
                noRender.pumpkin.getValue()){
            ci.cancel();
        }
    }

}
