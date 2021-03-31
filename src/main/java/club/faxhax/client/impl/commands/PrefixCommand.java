package club.faxhax.client.impl.commands;

import club.faxhax.client.api.command.Command;
import club.faxhax.client.api.command.CommandManager;
import club.faxhax.client.api.config.ConfigManager;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;

public class PrefixCommand extends Command {
    public PrefixCommand() {
        super("prefix", "<prefix>");
    }

    @Override
    public void perform(String[] args) {
        if(args.length == 1){
            Text text = new LiteralText(getWrongUsageMsg())
                    .setStyle(Style.EMPTY
                    .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new LiteralText(Formatting.DARK_AQUA + "Prefix: " + Formatting.GREEN + ConfigManager.CMD_PREFIX)))
                    .withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, CommandManager.getPrefix() + name + " ")));
            message(text);
        } else {
            CommandManager.setPrefix(args[1]);
            message(PREFIX + Formatting.GRAY + "Updated command prefix is: " +
                    Formatting.GREEN + CommandManager.getPrefix());
        }
    }
}
