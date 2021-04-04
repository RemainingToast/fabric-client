package club.faxhax.client.api.command;

import club.faxhax.client.FaxHax;
import club.faxhax.client.api.config.ConfigManager;
import club.faxhax.client.IFaxHax;
import net.minecraft.util.Formatting;

public abstract class Command implements IFaxHax {

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
        return FaxHax.PREFIX + Formatting.GRAY + "Incorrect Usage! Usage: " + Formatting.GREEN +
                ConfigManager.CMD_PREFIX + name + " " + getUsage();
    }

}
