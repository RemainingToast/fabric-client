package club.faxhax.client.api.config;

import club.faxhax.client.api.gui.GuiConfig;
import club.faxhax.client.api.friend.FriendManager;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;

public class ConfigManager {

    public static String CMD_PREFIX = ".";

    public static final File GAME_DIR = new File(FabricLoader.getInstance().getGameDir().toString().replaceAll("\\.", "") + "faxhax/");
    public static final File MODS_DIR = new File(FabricLoader.getInstance().getGameDir().toString().replaceAll("\\.", "") + "faxhax/modules/");
    public static final File MAIN_CONFIG = new File(GAME_DIR,"faxhax.json");
    public static final File GUI_CONFIG = new File(GAME_DIR,"clickgui.json");
    public static final File FRIEND_CONFIG = new File(GAME_DIR,"friends.json");

    public static void initializeConfigManager() {
        ConfigLoad.loadMainConfig();
        ConfigLoad.loadModules();
        GuiConfig.loadConfig();
        FriendManager.load();
    }

    public static void shutdown(){
        ConfigSave.saveModules();
        ConfigSave.saveMainConfig();
        GuiConfig.saveConfig();
        FriendManager.save();
    }

}
