package me.remainingtoast.faxhax.impl.modules.misc;

import me.remainingtoast.faxhax.api.module.Module;
import me.remainingtoast.faxhax.api.setting.Setting;
import me.remainingtoast.faxhax.api.util.FaxColor;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.scoreboard.Team;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;

public class ExtraTab extends Module {

    public Setting.Double tabSize = number("TabSize", 80, 20, 350);
    public Setting.Boolean highlightFriends = bool("HighlightFriends", true);

    public ExtraTab() {
        super("ExtraTab", Category.MISC);
    }

    // TODO Check if Friend
    public Text getName(PlayerListEntry entry){
        Text name = (entry.getDisplayName() != null) ? entry.getDisplayName() : Team.modifyText(entry.getScoreboardTeam(), new LiteralText(entry.getProfile().getName()));
        return (highlightFriends.getValue()) ? name.shallowCopy().setStyle(Style.EMPTY.withColor(TextColor.fromRgb(FaxColor.rainbow().getRGB()))) : name;
    }

}
