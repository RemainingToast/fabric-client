package me.remainingtoast.faxhax.api.config;

import me.remainingtoast.faxhax.api.gui.GuiConfig;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;

public class ConfigManager {

    public static String CMD_PREFIX = ".";

    public static final File GAME_DIR = new File(FabricLoader.getInstance().getGameDir().toString().replaceAll("\\.", "") + "faxhax/");
    public static final File MODS_DIR = new File(FabricLoader.getInstance().getGameDir().toString().replaceAll("\\.", "") + "faxhax/modules/");
    public static final File MAIN_CONFIG = new File(GAME_DIR,"faxhax.json");
    public static final File GUI_CONFIG = new File(GAME_DIR,"clickgui.json");

    public static void initializeConfigManager() {
        ConfigLoad.loadMainConfig();
        ConfigLoad.loadModules();
        GuiConfig.loadConfig();
    }

    public static void shutdown(){
        ConfigSave.saveModules();
        ConfigSave.saveMainConfig();
        GuiConfig.saveConfig();
    }

}
