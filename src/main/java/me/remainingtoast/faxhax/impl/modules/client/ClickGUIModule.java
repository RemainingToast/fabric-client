package me.remainingtoast.faxhax.impl.modules.client;

import me.remainingtoast.faxhax.api.gui.ClickGUI;
import me.remainingtoast.faxhax.api.module.Module;
import me.remainingtoast.faxhax.api.setting.Setting;
import net.minecraft.util.Identifier;

public class ClickGUIModule extends Module {

    private static final ClickGUI GUI = new ClickGUI();

    private static Setting.Mode fontType;

    public ClickGUIModule() {
        super("ClickGUI", Category.CLIENT);
        fontType = mode("Font", "Raleway", "Raleway", "Minecraft");
        setKey(344, -1);
    }

    @Override
    protected void onToggle() {
        if(enabled && (mc.currentScreen == null || mc.currentScreen.equals(GUI))) {
            if(mc.currentScreen != null && mc.currentScreen.equals(GUI)){
                mc.openScreen(null);
            } else {
                mc.openScreen(GUI);
            }
            disable();
        } else {
            GUI.onClose();
        }
    }

    public static ClickGUI getGUI() {
        return GUI;
    }

    public static Identifier getFont(){
        switch(fontType.getValue()){
            case "Raleway": {
                return new Identifier("faxhax", "raleway");
            }
            case "Minecraft": {
               return new Identifier("minecraft", "default");
            }
        }
        return new Identifier("minecraft", "default");
    }
}
