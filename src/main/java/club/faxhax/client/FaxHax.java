package club.faxhax.client;

import club.faxhax.client.api.module.ModuleManager;
import club.faxhax.client.api.command.CommandManager;
import club.faxhax.client.api.config.ConfigManager;
import club.faxhax.client.api.util.AuthUtil;
import meteordevelopment.orbit.EventBus;
import meteordevelopment.orbit.IEventBus;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.options.ServerList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FaxHax implements ModInitializer {

    public static MinecraftClient mc;

    public static final String VERSION = "v1.0";

    public static Logger LOGGER = LogManager.getLogger("FaxHax");

    public static IEventBus EVENT_BUS = new EventBus();

    @Override
    public void onInitialize() {
        long startTime = System.currentTimeMillis();

        // Minecraft
        mc = MinecraftClient.getInstance();

        addServer();

        // Auth
        AuthUtil.initializeAuth();

        if(!AuthUtil.isLicensed()) {
            LOGGER.fatal("[AUTH] This computer (" + AuthUtil.getHardwareUUID() + ") is not licensed!");
            LOGGER.fatal("[AUTH] Forcing Shutdown!");
            mc.scheduleStop();
            return;
        }

        LOGGER.info("Welcome to FaxHax " + VERSION);

        // Modules
        ModuleManager.initializeModuleManager();

        // Commands
        CommandManager.initializeCommandManager();

        // Config
        ConfigManager.initializeConfigManager();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            ConfigManager.shutdown();
            LOGGER.info("Successfully saved config and shutdown client!");
        }));

        String endTime = (System.currentTimeMillis() - startTime) + "ms";

        LOGGER.info("FaxHax has successfully loaded in " + endTime);
    }

    public static void addServer() {
        ServerList servers = new ServerList(mc);
        servers.loadFile();

        boolean contains = false;
        for (int i = 0; i < servers.size(); i++) {
            ServerInfo server = servers.get(i);

            if (server.address.contains("2b2t.com.au")) {
                contains = true;
                break;
            }
        }

        if (!contains) {
            servers.add(new ServerInfo("2b2t Queue Skip", "2b2t.com.au", false));
            servers.saveFile();
        }
    }
}