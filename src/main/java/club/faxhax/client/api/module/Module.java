package club.faxhax.client.api.module;

import club.faxhax.client.api.config.ConfigManager;
import club.faxhax.client.api.notification.Notification;
import club.faxhax.client.api.notification.NotificationManager;
import club.faxhax.client.api.notification.NotificationType;
import club.faxhax.client.api.setting.Setting;
import club.faxhax.client.api.setting.SettingManager;
import club.faxhax.client.api.util.FaxColor;
import club.faxhax.client.IFaxHax;
import club.faxhax.client.api.util.Util;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class Module implements IFaxHax {

    public String name;

    public String description;

    public List<String> aliases = new ArrayList<>();

    public Category category;

    public boolean enabled = false;

    public boolean drawn = true;

    public InputUtil.Key key = InputUtil.UNKNOWN_KEY;

    public Module(String name, Category category){
        this.name = name;
        this.category = category;
        SettingManager.addSetting(new Setting.KeyBind(this));
    }

    protected void onEnable(){
        EVENT_BUS.subscribe(this);
    }

    protected void onDisable(){
        EVENT_BUS.unsubscribe(this);
    }

    protected void onToggle(){
        NotificationManager.show(new Notification(NotificationType.INFO, name, (enabled) ? "enabled" : "disabled", 1));
    }

    protected void onTick(){

    }

    public void onCommand(String[] args){
        if(args.length == 1) {
            toggle();
            Util.messagePlayer(PREFIX + name + " has been " + ((enabled) ? Formatting.GREEN + "enabled" : Formatting.RED + "disabled"));
        }
        else if(args.length >= 3){
            try {
                switch (args[1]) {
                    case "draw":
                    case "drawn":{
                        setDrawn(Boolean.parseBoolean(args[2]));
                        Util.messagePlayer(PREFIX + name + " is now being: " + ((drawn) ? Formatting.GREEN + "DRAWN" : Formatting.RED + "HIDDEN"));
                    }
                    case "key":
                    case "bind": {
                        String keyName;
                        if(isInteger(args[2])){
                            setKey(InputUtil.fromKeyCode(Integer.parseInt(args[2]), -1));
                            keyName = GLFW.glfwGetKeyName(Integer.parseInt(args[2]), -1);
                        } else {
                            InputUtil.Key newKey = InputUtil.fromTranslationKey("key.keyboard."+args[2].replaceAll("[_\\s]", "\\."));
                            setKey(newKey);
                            keyName = GLFW.glfwGetKeyName(newKey.getCode(), -1);
                        }
                        Util.messagePlayer(PREFIX + name + " has been binded to: " + Formatting.GREEN + keyName);
                    }
                    default: {
                        Setting setting = SettingManager.getSettingByName(args[1]);
                        if(setting != null){
                            switch (setting.getType()){
                                case DOUBLE: {
                                    assert setting instanceof Setting.Double;
                                    Setting.Double doubleSetting = (Setting.Double) setting;
                                    double newValue = Double.parseDouble(args[2]);
                                    doubleSetting.setValue(newValue);
                                    Util.messagePlayer(PREFIX + Formatting.GRAY + setting.getName() + " has been set to: " + Formatting.GREEN + newValue);
                                    return;
                                }
                                case BOOLEAN: {
                                    assert setting instanceof Setting.Boolean;
                                    Setting.Boolean boolSetting = (Setting.Boolean) setting;
                                    boolean newValue = Boolean.parseBoolean(args[2]);
                                    boolSetting.setValue(newValue);
                                    Util.messagePlayer(PREFIX + Formatting.GRAY + setting.getName() + " has been set to: " + Formatting.GREEN + newValue);
                                    return;
                                }
                                case MODE: {
                                    assert setting instanceof Setting.Mode;
                                    Setting.Mode modeSetting = (Setting.Mode) setting;
                                    modeSetting.increment();
                                    Util.messagePlayer(PREFIX + Formatting.GRAY + setting.getName() + " has been incremented, new value: " + Formatting.GREEN + modeSetting.getValueName());
                                    return;
                                }
                                case COLOR: {
                                    if(args.length == 5){
                                        assert setting instanceof Setting.ColorSetting;
                                        Setting.ColorSetting colorSetting = (Setting.ColorSetting) setting;
                                        int red = Integer.parseInt(args[2]);
                                        int green = Integer.parseInt(args[3]);
                                        int blue = Integer.parseInt(args[4]);
                                        FaxColor newValue = new FaxColor(red, green, blue);
                                        colorSetting.setValue(newValue);
                                        Util.messagePlayer(PREFIX + Formatting.GRAY + setting.getName() + " has been set to: " + newValue.toString());
                                    } else Util.messagePlayer(PREFIX + Formatting.GRAY + "Incorrect Usage! Usage: " + Formatting.GREEN +
                                            ConfigManager.CMD_PREFIX + name + " <setting> <red> <green> <blue>");
                                }
                            }
                        }
                    }
                }
            } catch (NumberFormatException | ClassCastException e) {
                Util.messagePlayer(PREFIX + Formatting.GRAY + "Incorrect Value!");
            }
        } else Util.messagePlayer(PREFIX + Formatting.GRAY + "Incorrect Usage! Usage: " + Formatting.GREEN +
                ConfigManager.CMD_PREFIX + name + " <setting> <value>");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public void setAliases(List<String> aliases) {
        this.aliases = aliases;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        if(enabled) onEnable();
        else onDisable();
        this.enabled = enabled;
    }

    public void toggle(){
        setEnabled(!enabled);
        onToggle();
    }

    public void disable(){
        setEnabled(false);
    }

    public void enable(){
        setEnabled(true);
    }

    public boolean isDrawn() {
        return drawn;
    }

    public void setDrawn(boolean drawn) {
        this.drawn = drawn;
    }

    public InputUtil.Key getKey() {
        return key;
    }

    public String getKeyName() {
        return GLFW.glfwGetKeyName(key.getCode(), -1);
    }

    public void setKey(InputUtil.Key key) {
        this.key = key;
    }

    public void setKey(int key, int scancode) {
        this.key = InputUtil.fromKeyCode(key, scancode);
    }

    public enum Category {
        COMBAT,
        PLAYER,
        MOVEMENT,
        RENDER,
        MISC,
        CLIENT
    }

    protected Setting.Group group(final String name, final Setting<?>... settings){
        final Setting.Group setting = new Setting.Group(name, this, settings);
        SettingManager.addSetting(setting);
        return setting;
    }

    protected Setting.Double number(final String name, final double value, final double min, final double max) {
        final Setting.Double setting = new Setting.Double(name, this, value, min, max);
        SettingManager.addSetting(setting);
        return setting;
    }

    protected Setting.Boolean bool(final String name, final boolean value) {
        final Setting.Boolean setting = new Setting.Boolean(name, this, value);
        SettingManager.addSetting(setting);
        return setting;
    }

    protected Setting.Mode mode(final String name, final String value, final String... modes) {
        final Setting.Mode setting = new Setting.Mode(name, this, value, modes);
        SettingManager.addSetting(setting);
        return setting;
    }

    protected Setting.Mode mode(final String name, final String value, final List<String> modes) {
        final Setting.Mode setting = new Setting.Mode(name, this, value, modes);
        SettingManager.addSetting(setting);
        return setting;
    }

    protected Setting.ColorSetting color(final String name, FaxColor color) {
        final Setting.ColorSetting setting = new Setting.ColorSetting(name, this, false, color);
        SettingManager.addSetting(setting);
        return setting;
    }

    protected Setting.ColorSetting color(final String name, FaxColor color, Boolean rainbow) {
        final Setting.ColorSetting setting = new Setting.ColorSetting(name, this, rainbow, color);
        SettingManager.addSetting(setting);
        return setting;
    }

    private boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch(NumberFormatException | NullPointerException e) {
            return false;
        }
        return true;
    }

}
