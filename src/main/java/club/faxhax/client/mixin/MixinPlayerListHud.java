package club.faxhax.client.mixin;

import club.faxhax.client.api.module.ModuleManager;
import club.faxhax.client.impl.modules.misc.ExtraTab;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(PlayerListHud.class)
public class MixinPlayerListHud {

    private final ExtraTab extraTab = (ExtraTab) ModuleManager.getModule("ExtraTab");

    @Redirect(
            at = @At(value = "INVOKE", target = "Ljava/util/List;subList(II)Ljava/util/List;"),
            method = {"render"}
    )
    public <E> List<E> render(List<E> list, int fromIndex, int toIndex) {
        return list.subList(fromIndex, extraTab != null && extraTab.enabled ? Math.min(extraTab.tabSize.getIntValue(), list.size()) : toIndex);
    }

    @Inject(
            at = {@At("HEAD")},
            method = {"getPlayerName"},
            cancellable = true
    )
    public void getPlayerName(PlayerListEntry entry, CallbackInfoReturnable<Text> cir){
        if(extraTab != null && extraTab.enabled){
            cir.cancel();
            cir.setReturnValue(extraTab.getName(entry));
        }
    }
}
