package me.remainingtoast.faxhax.api.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.remainingtoast.faxhax.FaxHax;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ConfigManager {

    private static final File MAIN_CONFIG = new File("faxhax/faxhax.json");

    public static Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static String CMD_PREFIX = ".";

    public static void initializeConfigManager() {
        saveMainConfig();
    }

    public static void saveMainConfig() {
        MainConfig mainConfig = new MainConfig(
                CMD_PREFIX
//                FaxHax.NUM_VERSION
        );
        String json = GSON.toJson(mainConfig);
        try {
            FileWriter fileWriter = new FileWriter(MAIN_CONFIG);
            fileWriter.write(json);
            fileWriter.close();
        } catch (IOException e) {
            FaxHax.LOGGER.fatal("Main config failed to save!");
        }
    }

    public static class MainConfig {

        private final String commandPrefix;
//        private final double version;

        public MainConfig(String commandPrefix){
            this.commandPrefix = commandPrefix;
//            this.version = version;
        }

        public String getCommandPrefix() {
            return commandPrefix;
        }

    }

}
