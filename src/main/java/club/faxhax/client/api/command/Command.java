package club.faxhax.client.api.command;

import club.faxhax.client.api.config.ConfigManager;
import club.faxhax.client.mixin.IChatHud;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public abstract class Command {

    public MinecraftClient mc = MinecraftClient.getInstance();

    public static String PREFIX = Formatting.DARK_GRAY + "[" +
            Formatting.DARK_AQUA + "FaxHax" + Formatting.DARK_GRAY + "] " + Formatting.GRAY;

    public String name; // prefix + name (.prefix)

    public String usage;

    public Command(String name, String usage){
        this.name = name;
        this.usage = usage;
    }

    public abstract void perform(String[] args);

    public String getUsage() {
        return usage;
    }

    public String getWrongUsageMsg() {
        return PREFIX + Formatting.GRAY + "Incorrect Usage! Usage: " + Formatting.GREEN +
                ConfigManager.CMD_PREFIX + name + " " + getUsage();
    }

    public void message(Text text){
        if(mc.player != null) ((IChatHud) mc.inGameHud.getChatHud()).callAddMessage(text, 5932);
    }

    public void message(String str){
        message(new LiteralText(str));
    }

}