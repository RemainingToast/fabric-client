package me.remainingtoast.faxhax;

import me.remainingtoast.faxhax.api.command.CommandManager;
import me.remainingtoast.faxhax.api.config.ConfigManager;
import me.remainingtoast.faxhax.api.module.ModuleManager;
import me.remainingtoast.faxhax.api.util.AuthUtil;
import me.zero.alpine.bus.EventManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.options.ServerList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FaxHax implements ModInitializer {

    public static MinecraftClient mc;

    public static final String VERSION = "v1.0";

    public static Logger LOGGER = LogManager.getLogger("FaxHax");

    public static EventManager EVENTS = new EventManager();

    @Override
    public void onInitialize() {
        long startTime = System.currentTimeMillis();

        // Minecraft
        mc = MinecraftClient.getInstance();

        // Auth
        AuthUtil.initializeAuth();

        if(!AuthUtil.isLicensed()) {
            LOGGER.fatal("[AUTH] This computer (" + AuthUtil.getHardwareUUID() + ") is not licensed!");
            LOGGER.fatal("[AUTH] Forcing Shutdown!");
            mc.scheduleStop();
            return;
        }

        LOGGER.info("Welcome to FaxHax " + VERSION);

        // Config
        ConfigManager.initializeConfigManager();

        ClientLifecycleEvents.CLIENT_STOPPING.register(client -> ConfigManager.shutdown());

        // Modules
        ModuleManager.initializeModuleManager();

        // Commands
        CommandManager.initializeCommandManager();

        // 2b2t Australia
        addServer();

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
