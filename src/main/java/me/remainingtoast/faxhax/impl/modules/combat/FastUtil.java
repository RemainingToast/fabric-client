package me.remainingtoast.faxhax.impl.modules.combat;

import me.remainingtoast.faxhax.api.module.Module;
import me.remainingtoast.faxhax.api.setting.Setting;
import me.remainingtoast.faxhax.mixin.IMinecraftClient;
import net.minecraft.item.Items;

public class FastUtil extends Module {

    Setting.Boolean fastExp = bool("EXP Bottles", true);
    Setting.Boolean fastCrystal = bool("Crystals", true);
    Setting.Boolean fastBreak = bool("Break", true);
    Setting.Boolean fastPlace = bool("Place",true);

    public FastUtil() {
        super("FastUtil", Category.COMBAT);
    }

    @Override
    protected void onTick() {
        if(mc.player == null) return;
        boolean mainExp = mc.player.getMainHandStack().getItem().equals(Items.EXPERIENCE_BOTTLE);
        boolean offExp = mc.player.getOffHandStack().getItem().equals(Items.EXPERIENCE_BOTTLE);
        boolean mainCry = mc.player.getMainHandStack().getItem().equals(Items.END_CRYSTAL);
        boolean offCry = mc.player.getOffHandStack().getItem().equals(Items.END_CRYSTAL);

        if((mainExp || offExp) && fastExp.getValue()) ((IMinecraftClient) mc).setItemUseCooldown(0);
        if((mainCry || offCry) && fastCrystal.getValue()) ((IMinecraftClient) mc).setItemUseCooldown(0);
        if(!(mainExp || offExp || mainCry || offCry) && fastPlace.getValue()) ((IMinecraftClient) mc).setItemUseCooldown(0);
        if(fastBreak.getValue()) ((IMinecraftClient) mc).setItemUseCooldown(0);
    }
}
