package club.faxhax.client.impl.modules.client;

import club.faxhax.client.api.module.Module;
import club.faxhax.client.api.setting.Setting;
import net.minecraft.util.Identifier;

import java.util.Locale;

public class FontModule extends Module {

    Setting.Mode fontType = mode(
            "Font",
            "Raleway",
            "Raleway", "Minecraft"
    );

    public FontModule() {
        super("Font", Category.CLIENT);
        setEnabled(true);
    }

    public Identifier getFont(){
        if(fontType.getValue().equalsIgnoreCase("minecraft"))
            return new Identifier("minecraft", "default");
        else
            return new Identifier("faxhax", fontType.getValue().toLowerCase(Locale.ROOT));
    }

}
