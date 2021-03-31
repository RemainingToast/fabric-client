package club.faxhax.client.api.setting;

import club.faxhax.client.api.module.Module;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SettingManager {

    private static final List<Setting> settings = new ArrayList<>();

    public static List<Setting> getSettings() {
        return settings;
    }

    public static void addSetting(final Setting setting) {
        settings.add(setting);
    }

    public static Setting getSettingByNameAndMod(final String name, final Module parent) {
        return settings.stream().filter(s -> s.getParent().equals(parent)).filter(s -> s.getConfigName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public static Setting getSettingByNameAndModConfig(final String configName, final Module parent) {
        return settings.stream().filter(s -> s.getParent().equals(parent)).filter(s -> s.getConfigName().equalsIgnoreCase(configName)).findFirst().orElse(null);
    }

    public static List<Setting> getSettingsForMod(final Module parent) {
        return settings.stream().filter(s -> s.getParent().equals(parent)).collect(Collectors.toList());
    }

    public static List<Setting> getSettingsByCategory(final Module.Category faxCategory) {
        return settings.stream().filter(s -> s.getCategory().equals(faxCategory)).collect(Collectors.toList());
    }

    public static Setting getSettingByName(String name) {
        for (Setting set : getSettings()) {
            if (set.getName().equalsIgnoreCase(name)) {
                return set;
            }
        }
        return null;
    }

}
