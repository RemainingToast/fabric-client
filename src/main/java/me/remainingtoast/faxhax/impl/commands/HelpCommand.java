package me.remainingtoast.faxhax.impl.commands;

import me.remainingtoast.faxhax.api.command.Command;
import me.remainingtoast.faxhax.api.command.CommandManager;
import me.remainingtoast.faxhax.api.config.ConfigManager;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HelpCommand extends Command {

    public HelpCommand() {
        super("help", "");
    }

    @Override
    public void perform(String[] args) {
        if(mc.player != null){

            Text prefix = new LiteralText(PREFIX).setStyle(Style.EMPTY
                    .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new LiteralText(Formatting.DARK_AQUA + "Commands: " + Formatting.GREEN + CommandManager.COMMANDS.size())))
                    .withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.2b2t.com.au/")));

            MutableText text = new LiteralText("").append(prefix);

            for(Text txt : commandsText())
                text.append(txt);

            message(text);
        }
    }

    private List<Text> commandsText(){
        List<Text> output = new ArrayList<>();
        for(Command c : CommandManager.COMMANDS){
            boolean lastIndex = CommandManager.COMMANDS.indexOf(c) == CommandManager.COMMANDS.size()-1;
            Text command = new LiteralText(Formatting.GRAY + c.name.toLowerCase(Locale.ROOT) + ((lastIndex) ? ". " : ", ")).setStyle(Style.EMPTY
                    .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                            new LiteralText(Formatting.DARK_AQUA + "Usage: " + Formatting.GREEN +
                                    ConfigManager.CMD_PREFIX + c.name + " " + c.getUsage())))
                    .withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,
                            CommandManager.getPrefix() + c.name.toLowerCase(Locale.ROOT) + " ")));
            output.add(command);
        }
        return output;
    }
}
