package club.faxhax.client.mixin;

import club.faxhax.client.api.module.ModuleManager;
import club.faxhax.client.api.notification.NotificationManager;
import club.faxhax.client.impl.modules.render.NoRender;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
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


    @Inject(
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;getArmorStack(I)Lnet/minecraft/item/ItemStack;"),
            method = {"render"}
    )
    private void onRender(MatrixStack matrixStack, float tickDelta, CallbackInfo info) {
        NotificationManager.render(matrixStack);
    }
}
