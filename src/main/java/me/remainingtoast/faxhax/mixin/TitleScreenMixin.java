package me.remainingtoast.faxhax.mixin;

import me.remainingtoast.faxhax.api.module.ModuleManager;
import me.remainingtoast.faxhax.impl.modules.client.MainMenu;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.RotatingCubeMapRenderer;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sun.applet.Main;

import static net.minecraft.client.gui.DrawableHelper.drawTexture;

@Mixin(TitleScreen.class)
public class TitleScreenMixin {

    private final MinecraftClient client = MinecraftClient.getInstance();
    private final MainMenu mainMenuMod = (MainMenu) ModuleManager.getModule("MainMenu");

    @Redirect(
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/RotatingCubeMapRenderer;render(FF)V"),
            method = {"render"}
    )
    public void render(RotatingCubeMapRenderer rotatingCubeMapRenderer, float delta, float alpha){
        if(mainMenuMod == null || !mainMenuMod.enabled){
            rotatingCubeMapRenderer.render(delta, alpha);
        }
    }

    @Inject(
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/RotatingCubeMapRenderer;render(FF)V"),
            method = {"render"}
    )
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci){
        if(mainMenuMod != null && mainMenuMod.enabled){
            client.getTextureManager().bindTexture(new Identifier("faxhax", "gui/titlescreen.png"));
            assert client.currentScreen != null; // Shouldn't ever be null, as we are literally rendering screen
            int width = client.currentScreen.width;
            int height = client.currentScreen.height;
            drawTexture(
                    matrices,
                    0,
                    0,
                    width,
                    height,
                    0.0F,
                    0.0F,
                    width,
                    height,
                    width,
                    height
            );
        }
    }
}
