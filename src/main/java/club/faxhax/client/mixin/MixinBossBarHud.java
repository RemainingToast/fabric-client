package club.faxhax.client.mixin;

import club.faxhax.client.api.module.ModuleManager;
import club.faxhax.client.impl.modules.render.NoRender;
import net.minecraft.client.gui.hud.BossBarHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BossBarHud.class)
public class MixinBossBarHud {

    private final NoRender noRender = (NoRender) ModuleManager.getModule("NoRender");

    @Inject(
            at = {@At("HEAD")},
            method = {"render"},
            cancellable = true
    )
    private void render(CallbackInfo ci) {
        if(noRender != null && noRender.enabled &&
                noRender.bossbar.getValue()){
            ci.cancel();
        }
    }

}
