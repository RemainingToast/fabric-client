package me.remainingtoast.faxhax.mixin;

import me.remainingtoast.faxhax.api.module.ModuleManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    @Shadow @Nullable public ClientPlayerEntity player;

    @Shadow @Nullable public ClientWorld world;

    @Inject(
            at = {@At("INVOKE")},
            method = {"tick"},
            cancellable = true
    )
    public void tick(CallbackInfo callbackInfo){
        if(player != null && world != null) ModuleManager.onTick();
    }

}
