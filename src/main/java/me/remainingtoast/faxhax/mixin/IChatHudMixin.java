package me.remainingtoast.faxhax.mixin;

import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ChatHud.class)
public interface IChatHudMixin {

    @Invoker
    void callAddMessage(Text message, int messageId);

}
