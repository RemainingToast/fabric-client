package club.faxhax.client;

import meteordevelopment.orbit.EventBus;
import meteordevelopment.orbit.IEventBus;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Formatting;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public interface IFaxHax {

    MinecraftClient mc = MinecraftClient.getInstance();

    String PREFIX = Formatting.DARK_GRAY + "[" + Formatting.DARK_AQUA + "FaxHax"
            + Formatting.DARK_GRAY + "] " + Formatting.GRAY;

    String VERSION = "v1.0.1";

    Logger LOGGER = LogManager.getLogger("FaxHax");

    IEventBus EVENT_BUS = new EventBus();

}
