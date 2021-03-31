package club.faxhax.client.api.command;

import club.faxhax.client.FaxHax;
import club.faxhax.client.api.module.Module;
import club.faxhax.client.api.module.ModuleManager;
import club.faxhax.client.api.config.ConfigManager;
import club.faxhax.client.impl.commands.HelpCommand;
import club.faxhax.client.impl.commands.PrefixCommand;

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

    public static void initializeCommandManager(){
        long startTime = System.currentTimeMillis();
        COMMANDS.add(new HelpCommand());
        COMMANDS.add(new PrefixCommand());

        for(Module mod : ModuleManager.MODS){
            COMMANDS.add(new Command(mod.name.toLowerCase(Locale.ROOT), "<setting> <value>") {
                @Override
                public void perform(String[] args) {
                    mod.onCommand(args);
                }
            });
        }

        String endTime = (System.currentTimeMillis() - startTime) + "ms";
        int size = COMMANDS.size() - ModuleManager.MODS.size();
        FaxHax.LOGGER.info("Successfully loaded " + size + " commands in "+endTime);
    }
}
