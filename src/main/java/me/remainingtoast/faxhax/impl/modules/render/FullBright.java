package me.remainingtoast.faxhax.impl.modules.render;

import me.remainingtoast.faxhax.api.module.Module;
import me.remainingtoast.faxhax.api.setting.Setting;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

public class FullBright extends Module {

    Setting.Mode mode;

    private enum ModeEnum {
        Gamma,
        Potion
    }

    public FullBright() {
        super("FullBright", Category.RENDER);
        mode = mode("Mode", ModeEnum.values(), ModeEnum.Gamma);
    }

    private double oldGamma;

    @Override
    protected void onToggle() {
        if(enabled) oldGamma = mc.options.gamma;
        else {
            assert mc.player != null;
            mc.options.gamma = oldGamma;
            mc.player.removeStatusEffect(StatusEffects.NIGHT_VISION);
        }
    }

    @Override
    protected void onTick() {
        updateGamma();
    }

    private void updateGamma() {
        assert mc.player != null;
        if(mode.getValue() == ModeEnum.Gamma){
            mc.options.gamma = 5781;
            mc.player.removeStatusEffect(StatusEffects.NIGHT_VISION);
        } else if(mode.getValue() == ModeEnum.Potion){
            mc.player.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, Integer.MAX_VALUE, 5781));
        }

    }
}
