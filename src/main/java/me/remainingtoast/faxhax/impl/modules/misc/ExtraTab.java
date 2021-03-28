package me.remainingtoast.faxhax.impl.modules.misc;

import me.remainingtoast.faxhax.api.module.Module;
import me.remainingtoast.faxhax.api.setting.Setting;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.scoreboard.Team;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;

public class ExtraTab extends Module {

    public Setting.Double tabSize = number("TabSize", 80, 20, 350);
    public Setting.Boolean highlightFriends = bool("HighlightFriends", true);
    public Setting.Mode color = mode("Color", "green", new ArrayList<>(Formatting.getNames(true, false)));

    public ExtraTab() {
        super("ExtraTab", Category.MISC);
    }

    @Override
    protected void onTick() {
        color.setHidden(!highlightFriends.enabled());
    }

    public Text getName(PlayerListEntry entry){
        Text name = (entry.getDisplayName() != null) ? entry.getDisplayName() : Team.modifyText(entry.getScoreboardTeam(), new LiteralText(entry.getProfile().getName()));
        return (highlightFriends.getValue()) ? name.shallowCopy().formatted(Formatting.byName(color.getValue())) : name;
    }

}
