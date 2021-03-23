package me.remainingtoast.faxhax.api.module;

import me.remainingtoast.faxhax.api.config.ConfigManager;
import me.remainingtoast.faxhax.api.setting.Setting;
import me.remainingtoast.faxhax.api.setting.SettingManager;
import me.remainingtoast.faxhax.api.util.FaxColor;
import me.remainingtoast.faxhax.mixin.IChatHud;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class Module {

    public String PREFIX = Formatting.DARK_GRAY + "[" + Formatting.DARK_AQUA + "FaxHax" + Formatting.DARK_GRAY + "] " + Formatting.GRAY;

    public MinecraftClient mc = MinecraftClient.getInstance();

    public String name;

    public String description;

    public List<String> aliases = new ArrayList<>();

    public Category category;

    public boolean enabled = false;

    public boolean drawn = false;

    public InputUtil.Key key = InputUtil.UNKNOWN_KEY;

    public Module(String name, Category category){
        this.name = name;
        this.category = category;
    }

    protected void onEnable(){

    }

    protected void onDisable(){

    }

    protected void onToggle(){

    }

    protected void onTick(){

    }

    public void onCommand(String[] args){
        if(args.length == 1) {
            toggle();
            message(PREFIX + name + " has been " + ((enabled) ? Formatting.GREEN + "enabled" : Formatting.RED + "disabled"));
        }
        else if(args.length >= 3){
            try {
                switch (args[1]) {
                    case "draw":
                    case "drawn":{
                        setDrawn(Boolean.parseBoolean(args[2]));
                        message(PREFIX + name + " is now being: " + ((drawn) ? Formatting.GREEN + "DRAWN" : Formatting.RED + "HIDDEN"));
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
                        message(PREFIX + name + " has been binded to: " + Formatting.GREEN + keyName);
                    }
                    default: {
                        Setting setting = SettingManager.getSettingByName(args[1]);
                        if(setting != null){
                            switch (setting.getType()){
                                case INTEGER: {
                                    assert setting instanceof Setting.Integer;
                                    Setting.Integer intSetting = (Setting.Integer) setting;
                                    int newValue = Integer.parseInt(args[2]);
                                    intSetting.setValue(newValue);
                                    message(PREFIX + Formatting.GRAY + setting.getName() + " has been set to: " + Formatting.GREEN + newValue);
                                    return;
                                }
                                case DOUBLE: {
                                    assert setting instanceof Setting.Double;
                                    Setting.Double doubleSetting = (Setting.Double) setting;
                                    double newValue = Double.parseDouble(args[2]);
                                    doubleSetting.setValue(newValue);
                                    message(PREFIX + Formatting.GRAY + setting.getName() + " has been set to: " + Formatting.GREEN + newValue);
                                    return;
                                }
                                case BOOLEAN: {
                                    assert setting instanceof Setting.Boolean;
                                    Setting.Boolean boolSetting = (Setting.Boolean) setting;
                                    boolean newValue = Boolean.parseBoolean(args[2]);
                                    boolSetting.setValue(newValue);
                                    message(PREFIX + Formatting.GRAY + setting.getName() + " has been set to: " + Formatting.GREEN + newValue);
                                    return;
                                }
                                case MODE: {
                                    assert setting instanceof Setting.Mode;
                                    Setting.Mode modeSetting = (Setting.Mode) setting;
                                    modeSetting.setValue(Enum.valueOf(modeSetting.getValue().getClass(), args[2]));
//                                    modeSetting.setValue(args[2]);
                                    message(PREFIX + Formatting.GRAY + setting.getName() + " has been set to: " + Formatting.GREEN + args[2]);
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
                                        message(PREFIX + Formatting.GRAY + setting.getName() + " has been set to: " + newValue.toString());
                                    } else message(PREFIX + Formatting.GRAY + "Incorrect Usage! Usage: " + Formatting.GREEN +
                                            ConfigManager.CMD_PREFIX + name + " <setting> <red> <green> <blue>");
                                }
                            }
                        }
                    }
                }
            } catch (NumberFormatException | ClassCastException e) {
                message(PREFIX + Formatting.GRAY + "Incorrect Value!");
            }
        } else message(PREFIX + Formatting.GRAY + "Incorrect Usage! Usage: " + Formatting.GREEN +
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

    public void message(Text text){
        if(mc.player != null) ((IChatHud) mc.inGameHud.getChatHud()).callAddMessage(text, 5932);
    }

    public void message(String str){
        message(new LiteralText(str));
    }

    protected Setting.Integer aInteger(final String name, final int value, final int min, final int max) {
        final Setting.Integer setting = new Setting.Integer(name, this, getCategory(), value, min, max);
        SettingManager.addSetting(setting);
        return setting;
    }

    protected Setting.Double aDouble(final String name, final double value, final double min, final double max) {
        final Setting.Double setting = new Setting.Double(name, this, getCategory(), value, min, max);
        SettingManager.addSetting(setting);
        return setting;
    }

    protected Setting.Boolean aBoolean(final String name, final boolean value) {
        final Setting.Boolean setting = new Setting.Boolean(name, this, getCategory(), value);
        SettingManager.addSetting(setting);
        return setting;
    }

    protected Setting.Mode aMode(final String name, final Enum<?>[] modes, final Enum<?> value) {
        final Setting.Mode setting = new Setting.Mode(name, this, getCategory(), modes, value);
        SettingManager.addSetting(setting);
        return setting;
    }

    protected Setting.ColorSetting aColor(final String name, FaxColor color) {
        final Setting.ColorSetting setting = new Setting.ColorSetting(name, this, getCategory(), false, color);
        SettingManager.addSetting(setting);
        return setting;
    }

    protected Setting.ColorSetting aColor(final String name, FaxColor color, Boolean rainbow) {
        final Setting.ColorSetting setting = new Setting.ColorSetting(name, this, getCategory(), rainbow, color);
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

    public void closeScreen(){
        mc.openScreen(null);
    }
}
