package me.remainingtoast.faxhax.api.gui;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import me.remainingtoast.faxhax.api.config.ConfigManager;
import me.remainingtoast.faxhax.api.module.Module;
import me.remainingtoast.faxhax.api.module.ModuleManager;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class GuiConfig {

    private static final File DIR = ConfigManager.GUI_CONFIG;
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static JsonObject panelObject = new JsonObject();

    public static void loadConfig(){
        if (!Files.exists(DIR.toPath())) {
            return;
        }
        try {
            InputStream inputStream;
            inputStream = Files.newInputStream(DIR.toPath());
            JsonObject mainObject = new JsonParser().parse(new InputStreamReader(inputStream)).getAsJsonObject();
            if (mainObject.get("Panels") == null) {
                return;
            }
            panelObject = mainObject.get("Panels").getAsJsonObject();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveConfig(){
        try {
            savePanels();
            OutputStreamWriter fileOutputStreamWriter = new OutputStreamWriter(new FileOutputStream(DIR), StandardCharsets.UTF_8);
            JsonObject mainObject = new JsonObject();
            mainObject.add("Panels", panelObject);
            String jsonString = gson.toJson(new JsonParser().parse(mainObject.toString()));
            fileOutputStreamWriter.write(jsonString);
            fileOutputStreamWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addPanel(Panel panel){
        JsonObject valueObject = new JsonObject();
        JsonObject moduleObject = new JsonObject();
        valueObject.add("x", new JsonPrimitive(panel.x));
        valueObject.add("y", new JsonPrimitive(panel.y));
        valueObject.add("expanded", new JsonPrimitive(panel.categoryExpanded));
        for(Map.Entry<Module, Boolean> entry : panel.modsExpanded.entrySet()){
            if(!entry.getValue()) continue;
            moduleObject.add(entry.getKey().name, new JsonPrimitive(true));
        }
        valueObject.add("expandedModules", moduleObject);
        panelObject.add(panel.category.name(), valueObject);
    }

    public static Panel loadPanelDirect(Module.Category category){
        JsonObject valueObject = panelObject.get(category.name()).getAsJsonObject();
        JsonObject moduleObject = valueObject.get("expandedModules").getAsJsonObject();
        Type type = new TypeToken<HashMap<String, Boolean>>(){}.getType();

        int x = valueObject.get("x").getAsInt();
        int y = valueObject.get("y").getAsInt();
        boolean expanded = valueObject.get("expanded").getAsBoolean();
        HashMap<String, Boolean> modsExpanded = gson.fromJson(moduleObject, type);

        Panel panel = new Panel(category, x, y);
        panel.categoryExpanded = expanded;
        for (Map.Entry<String, Boolean> entry : modsExpanded.entrySet()){
            panel.modsExpanded.putIfAbsent(ModuleManager.getModule(entry.getKey()), entry.getValue());
        }

        return panel;
    }

    public static HashMap<Module.Category, Panel> loadPanels(){
        HashMap<Module.Category, Panel> panels = new HashMap<>();
        try {
            for(Module.Category category : Module.Category.values()){
                panels.putIfAbsent(category, loadPanelDirect(category));
            }
        } catch (NullPointerException exception){
            int x = 20;
            for(Module.Category category : Module.Category.values()){
                panels.putIfAbsent(category, new Panel(category, x, 20));
                x += 93;
            }
        }
        return panels;
    }

    public static void savePanels(){
        for(Panel panel : ClickGUI.panels.values()){
            addPanel(panel);
        }
    }
}
