package club.faxhax.client.api.config;

import club.faxhax.client.FaxHax;
import club.faxhax.client.api.module.Module;
import club.faxhax.client.api.module.ModuleManager;
import club.faxhax.client.api.setting.Setting;
import club.faxhax.client.api.setting.SettingManager;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import net.minecraft.client.util.InputUtil;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ConfigLoad {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static boolean loadMainConfig() {
        if (!ConfigManager.GAME_DIR.exists()) {
            ConfigManager.GAME_DIR.mkdirs();
        }
        try {
            if(ConfigManager.MAIN_CONFIG.exists()){
                FileReader reader = new FileReader(ConfigManager.MAIN_CONFIG);
                Type type = new TypeToken<MainConfig>() {}.getType();
                MainConfig mainConfig = GSON.fromJson(reader, type);
                ConfigManager.CMD_PREFIX = mainConfig.getPrefix();
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            FaxHax.LOGGER.fatal("Main config failed to load!");
            return false;
        }
    }

    public static void loadModules(){
        for(Module module : ModuleManager.MODS){
            loadModuleDirect(module);
        }
    }

    public static void loadModuleDirect(Module module) {
        try {
            if (!Files.exists(Paths.get(ConfigManager.MODS_DIR + "/" + module.getName().toLowerCase() + ".json"))) {
                return;
            }

            InputStream inputStream = Files.newInputStream(Paths.get(ConfigManager.MODS_DIR + "/" + module.getName().toLowerCase() + ".json"));
            JsonObject moduleObject = new JsonParser().parse(new InputStreamReader(inputStream)).getAsJsonObject();

            if (moduleObject.get("Module") == null) {
                return;
            }

            JsonObject settingObject = moduleObject.get("Settings").getAsJsonObject();
            for (Setting setting : SettingManager.getSettingsForMod(module)) {
                JsonElement dataObject = settingObject.get(setting.getConfigName());
                try {
                    if (dataObject != null && dataObject.isJsonPrimitive()) {
                        switch (setting.getType()) {
                            case BOOLEAN:
                                ((Setting.Boolean) setting).setValue(dataObject.getAsBoolean());
                                break;
                            case DOUBLE:
                                ((Setting.Double) setting).setValue(dataObject.getAsDouble());
                                break;
                            case COLOR:
                                ((Setting.ColorSetting) setting).fromInteger(dataObject.getAsInt());
                                break;
                            case MODE:
                                ((Setting.Mode) setting).setValue(dataObject.getAsString());
                                break;
                        }
                    }
                } catch (java.lang.NumberFormatException e) {
                    System.out.println(setting.getConfigName() + " " + module.getName());
                    System.out.println(dataObject);
                }
            }

            module.setEnabled(moduleObject.get("Enabled").getAsBoolean());
            module.setDrawn(moduleObject.get("Drawn").getAsBoolean());
            module.setKey(InputUtil.fromTranslationKey(moduleObject.get("Bind").getAsString()));

            inputStream.close();
        } catch(IOException e) {
            FaxHax.LOGGER.fatal("Error loading config file for: " + module.getName());
            e.printStackTrace();
        }
    }
}
