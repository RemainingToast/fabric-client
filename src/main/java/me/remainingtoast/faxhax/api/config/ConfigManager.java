package me.remainingtoast.faxhax.api.config;

import net.fabricmc.loader.api.FabricLoader;

import java.io.File;

public class ConfigManager {

    public static String CMD_PREFIX = ".";

    public static final File GAME_DIR = new File(FabricLoader.getInstance().getGameDir().toString().replaceAll("\\.", "") + "faxhax/");
    public static final File MODS_DIR = new File(FabricLoader.getInstance().getGameDir().toString().replaceAll("\\.", "") + "faxhax/modules/");
    public static final File MAIN_CONFIG = new File(GAME_DIR,"faxhax.json");
    public static final File GUI_CONFIG = new File(GAME_DIR,"clickgui.json");

    private static final ConfigSave save = new ConfigSave();
    private static final ConfigLoad load = new ConfigLoad();

    public static void initializeConfigManager() {
        load.loadMainConfig();
        load.loadModules();
    }

    public static void shutdown(){
        save.saveModules();
        save.saveMainConfig();
    }

}
