package me.remainingtoast.faxhax.mixin;

import me.remainingtoast.faxhax.api.util.CapeUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayerEntity.class)
public class AbstractClientPlayerEntityMixin {

    @Inject(
            at = {@At("RETURN")},
            method = {"getCapeTexture()Lnet/minecraft/util/Identifier;"},
            cancellable = true
    )
    public void getCapeTexture(CallbackInfoReturnable<Identifier> cir){
        PlayerEntity player = (AbstractClientPlayerEntity) (Object) this;
        if(cir.getReturnValue() == null) {
            cir.setReturnValue(CapeUtil.fromPlayer(player));
            cir.cancel();
        }
    }

    @Inject(
            at = {@At("RETURN")},
            method = {"getElytraTexture()Lnet/minecraft/util/Identifier;"},
            cancellable = true
    )
    public void getElytraTexture(CallbackInfoReturnable<Identifier> cir){
        PlayerEntity player = (AbstractClientPlayerEntity) (Object) this;
        if(cir.getReturnValue() == null) {
            cir.setReturnValue(CapeUtil.fromPlayer(player));
            cir.cancel();
        }
    }

}
