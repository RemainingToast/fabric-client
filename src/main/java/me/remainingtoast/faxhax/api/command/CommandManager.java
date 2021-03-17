package me.remainingtoast.faxhax.api.command;

import me.remainingtoast.faxhax.FaxHax;
import me.remainingtoast.faxhax.api.config.ConfigManager;
import me.remainingtoast.faxhax.api.module.Module;
import me.remainingtoast.faxhax.api.module.ModuleManager;
import me.remainingtoast.faxhax.impl.commands.BindCommand;
import me.remainingtoast.faxhax.impl.commands.HelpCommand;
import me.remainingtoast.faxhax.impl.commands.PrefixCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CommandManager {

    public static List<Command> COMMANDS = new ArrayList<>();

    public static boolean messagePrefixed(String str){
        return str.startsWith(getPrefix());
    }

    public static String getPrefix(){
        return ConfigManager.CMD_PREFIX;
    }

    public static void setPrefix(String prefix){
        ConfigManager.CMD_PREFIX = prefix;
    }

    public static void onChatMessage(String message){
       String[] messages = message.replaceFirst(ConfigManager.CMD_PREFIX, "").split("\\s+");
       for(Command c : COMMANDS) {
           if(messages[0].equalsIgnoreCase(c.name)) {
                c.perform(messages);
                return;
           }
       }
    }

    public static void initialiseCommandManager(){
        COMMANDS.add(new HelpCommand());
        COMMANDS.add(new PrefixCommand());

        for(Module mod : ModuleManager.MODS){
            COMMANDS.add(new Command(mod.name.toLowerCase(Locale.ROOT), "<setting> <value>") {
                @Override
                public void perform(String[] args) { mod.onCommand(args); }
            });
        }

        FaxHax.LOGGER.info("Successfully loaded " + COMMANDS.size() + " commands");
    }
}
