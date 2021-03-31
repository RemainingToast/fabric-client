package club.faxhax.client.api.config;

import club.faxhax.client.FaxHax;
import club.faxhax.client.api.module.Module;
import club.faxhax.client.api.module.ModuleManager;
import club.faxhax.client.api.setting.Setting;
import club.faxhax.client.api.setting.SettingManager;
import com.google.gson.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class ConfigSave {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static void saveModules(){
        for(Module mod : ModuleManager.MODS){
            saveModuleDirect(mod);
        }
    }

    public static boolean saveMainConfig() {
        MainConfig mainConfig = new MainConfig(
                ConfigManager.CMD_PREFIX
        );
        String json = GSON.toJson(mainConfig);
        try {
            OutputStreamWriter output = new OutputStreamWriter(new FileOutputStream(ConfigManager.MAIN_CONFIG), StandardCharsets.UTF_8);
            output.write(json);
            output.close();
            return true;
        } catch (IOException e) {
            FaxHax.LOGGER.fatal("Main config failed to save!");
            e.printStackTrace();
            return false;
        }
    }

    public static void saveModuleDirect(Module module) {
        try {
            if (!ConfigManager.MODS_DIR.exists()) {
                ConfigManager.MODS_DIR.mkdirs();
            }
            OutputStreamWriter fileOutputStreamWriter = new OutputStreamWriter(new FileOutputStream(ConfigManager.MODS_DIR + "/" + module.getName().toLowerCase() + ".json"), StandardCharsets.UTF_8);
            JsonObject moduleObject = new JsonObject();
            JsonObject settingObject = new JsonObject();
            moduleObject.add("Module", new JsonPrimitive(module.getName()));
            moduleObject.add("Category", new JsonPrimitive(module.category.toString()));
            for (Setting setting : SettingManager.getSettingsForMod(module)) {
                switch (setting.getType()) {
                    case BOOLEAN: {
                        settingObject.add(setting.getConfigName(), new JsonPrimitive(((Setting.Boolean) setting).getValue()));
                        break;
                    }
                    case DOUBLE: {
                        settingObject.add(setting.getConfigName(), new JsonPrimitive(((Setting.Double) setting).getValue()));
                        break;
                    }
                    case COLOR: {
                        settingObject.add(setting.getConfigName(), new JsonPrimitive(((Setting.ColorSetting) setting).toInteger()));
                        break;
                    }
                    case MODE: {
                        settingObject.add(setting.getConfigName(), new JsonPrimitive(((Setting.Mode) setting).getValueName()));
                        break;
                    }
                }
            }
            moduleObject.add("Settings", settingObject);
            moduleObject.add("Enabled", new JsonPrimitive(module.enabled));
            moduleObject.add("Drawn", new JsonPrimitive(module.drawn));
            moduleObject.add("Bind", new JsonPrimitive(String.valueOf(module.key)));
            String jsonString = GSON.toJson(new JsonParser().parse(moduleObject.toString()));
            fileOutputStreamWriter.write(jsonString);
            fileOutputStreamWriter.close();
        } catch(IOException e){

        }
    }
}
