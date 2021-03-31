package club.faxhax.client.mixin;

import club.faxhax.client.api.command.CommandManager;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public abstract class MixinClientPlayerEntity {

    @Inject(
            at = {@At("HEAD")},
            method = {"sendChatMessage"},
            cancellable = true
    )
    public void onChatMessage(String message, CallbackInfo ci){
        if(CommandManager.messagePrefixed(message)) {
            CommandManager.onChatMessage(message);
            ci.cancel();
        }
    }

}
