package me.remainingtoast.faxhax.mixin;

import me.remainingtoast.faxhax.api.module.ModuleManager;
import me.remainingtoast.faxhax.impl.modules.render.NoRender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class MixinGameRenderer {

    private final NoRender noRender = (NoRender) ModuleManager.getModule("NoRender");

    @Inject(
            at = {@At("HEAD")},
            method = {"bobViewWhenHurt"},
            cancellable = true
    )
    private void bobViewWhenHurt(MatrixStack matrixStack, float f, CallbackInfo ci){
        if(noRender != null && noRender.enabled &&
                noRender.hurtcam.getValue() &&
                !(MinecraftClient.getInstance().world == null)){
            ci.cancel();
        }
    }

    @Inject(
            at = {@At("HEAD")},
            method = {"showFloatingItem"},
            cancellable = true
    )
    private void showFloatingItem(ItemStack itemStack, CallbackInfo ci) {
        if(noRender != null && noRender.enabled &&
                noRender.totem.getValue() &&
                itemStack.getItem() == Items.TOTEM_OF_UNDYING){
            ci.cancel();
        }
    }

    @Redirect(
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;lerp(FFF)F", ordinal = 0),
            method = {"renderWorld(FJLnet/minecraft/client/util/math/MatrixStack;)V"}
    )
    private float nauseaWobble(float delta, float first, float second) {
        if(noRender != null && noRender.enabled &&
                noRender.nausea.getValue()){
            return 0;
        }
        return MathHelper.lerp(delta, first, second);
    }



}
