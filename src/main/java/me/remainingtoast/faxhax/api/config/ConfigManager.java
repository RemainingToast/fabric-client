package me.remainingtoast.faxhax.api.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import me.remainingtoast.faxhax.FaxHax;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

public class ConfigManager {

    private static final File DIR = new File(FabricLoader.getInstance().getGameDir().toString().replaceAll("\\.", "") + "faxhax/");

    private static final File MAIN_CONFIG = new File(DIR,"faxhax.json");

    public static Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static String CMD_PREFIX = ".";

    public static void initializeConfigManager() {
        loadMainConfig();
    }

    public static void shutdown(){
        saveMainConfig();
    }

    public static boolean saveMainConfig() {
        MainConfig mainConfig = new MainConfig(
                CMD_PREFIX
        );
        String json = GSON.toJson(mainConfig);
        try {
            OutputStreamWriter output = new OutputStreamWriter(new FileOutputStream(MAIN_CONFIG), StandardCharsets.UTF_8);
            output.write(json);
            output.close();
            FaxHax.LOGGER.info("Main config saved");
            return true;
        } catch (IOException e) {
            FaxHax.LOGGER.fatal("Main config failed to save!");
            e.printStackTrace();
            return false;
        }
    }

    public static boolean loadMainConfig() {
        if (!DIR.exists()) {
            DIR.mkdirs();
        }
        try {
            if(MAIN_CONFIG.exists()){
                FileReader reader = new FileReader(MAIN_CONFIG);
                Type type = new TypeToken<MainConfig>() {}.getType();
                MainConfig mainConfig = GSON.fromJson(reader, type);
                CMD_PREFIX = mainConfig.getPrefix();
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            if(!saveMainConfig()){
                FaxHax.LOGGER.fatal("Main config failed to load!");
            }
            return false;
        }
    }

    public static class MainConfig {

        private final String prefix;

        public MainConfig(String prefix){
            this.prefix = prefix;
        }

        public String getPrefix() {
            return prefix;
        }

    }

}
