package me.remainingtoast.faxhax.impl.modules.client;

import me.remainingtoast.faxhax.api.module.Module;
import me.remainingtoast.faxhax.api.setting.Setting;
import net.minecraft.util.Identifier;

import java.util.Locale;

public class FontModule extends Module {

    Setting.Mode fontType;
    Setting.Mode size;

    public FontModule() {
        super("Font", Category.CLIENT);
        fontType = mode(
                "Font",
                "Raleway",
                "Raleway", "Minecraft"
        );
        size = mode(
                "Size",
                "10",
                "10", "12"
        );
    }

    public Identifier getFont(){
        if(fontType.getValue().equalsIgnoreCase("minecraft"))
            return new Identifier("minecraft", "default");
        else
            return new Identifier("faxhax", fontType.getValue().toLowerCase(Locale.ROOT) + size.getValue());
    }

    public int getSize(){
        return Integer.parseInt(size.getValue());
    }
}
