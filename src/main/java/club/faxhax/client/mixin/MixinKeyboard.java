package club.faxhax.client.mixin;

import club.faxhax.client.FaxHax;
import club.faxhax.client.api.module.ModuleManager;
import club.faxhax.client.api.command.CommandManager;
import net.minecraft.client.gui.screen.ChatScreen;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(net.minecraft.client.Keyboard.class)
public class MixinKeyboard {

    @Inject(
            at = {@At("HEAD")},
            method = {"onKey"},
            cancellable = true
    )
    public void onKey(long window, int keyInt, int scancode, int i, int j, CallbackInfo ci){
        String key = GLFW.glfwGetKeyName(keyInt, scancode);
        if(key != null && key.equals(CommandManager.getPrefix())){
            if(FaxHax.mc.currentScreen == null) FaxHax.mc.openScreen(new ChatScreen(CommandManager.getPrefix()));
        } else ModuleManager.onKey(window, keyInt, scancode);
    }

}
