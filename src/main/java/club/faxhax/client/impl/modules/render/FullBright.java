package club.faxhax.client.impl.modules.render;

import club.faxhax.client.api.module.Module;
import club.faxhax.client.api.setting.Setting;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

public class FullBright extends Module {

    Setting.Mode mode = mode("Mode", "Gamma", "Gamma", "Potion");

    public FullBright() {
        super("FullBright", Category.RENDER);
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
        if(mode.toggled("Gamma")){
            mc.options.gamma = 57810;
            mc.player.removeStatusEffect(StatusEffects.NIGHT_VISION);
        } else if(mode.toggled("Potion")){
            mc.player.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, Integer.MAX_VALUE, Integer.MAX_VALUE));
            mc.options.gamma = oldGamma;
        }
    }
}
