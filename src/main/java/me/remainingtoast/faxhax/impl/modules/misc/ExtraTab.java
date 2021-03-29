package me.remainingtoast.faxhax.impl.modules.misc;

import me.remainingtoast.faxhax.api.friend.FriendManager;
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

    public Setting.Double tabSize = number("TabSize", 150, 20, 500);
    public Setting.Boolean highlightFriends = bool("HighlightFriends", true);

    public ExtraTab() {
        super("ExtraTab", Category.MISC);
    }

    public Text getName(PlayerListEntry entry){
        assert mc.player != null;
        Text name = (entry.getDisplayName() != null) ? entry.getDisplayName()
                : Team.modifyText(entry.getScoreboardTeam(), new LiteralText(entry.getProfile().getName()));

        boolean bool = highlightFriends.getValue() && FriendManager.isFriend(entry.getProfile());

        return (bool) ? name.shallowCopy()
                .setStyle(Style.EMPTY.withColor(TextColor
                .fromRgb(FaxColor.rainbow().getRGB()))) : name;
    }

}
