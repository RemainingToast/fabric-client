package me.remainingtoast.faxhax.mixin;

import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MinecraftClient.class)
public interface IMinecraftClientMixin {

    @Accessor("itemUseCooldown")
    void setItemUseCooldown(int itemUseCooldown);

}
