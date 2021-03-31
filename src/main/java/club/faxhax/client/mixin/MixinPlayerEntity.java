package club.faxhax.client.mixin;

import club.faxhax.client.api.module.ModuleManager;
import club.faxhax.client.impl.modules.movement.SafeWalk;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class MixinPlayerEntity {

    private final SafeWalk safeWalk = (SafeWalk) ModuleManager.getModule("SafeWalk");

    @Inject(
            at = {@At("HEAD")},
            method = {"clipAtLedge"},
            cancellable = true
    )
    public void onClipAtLedge(CallbackInfoReturnable<Boolean> cir) {
        if(safeWalk != null && safeWalk.enabled) cir.setReturnValue(true);
    }

}
