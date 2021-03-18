package me.remainingtoast.faxhax;

import me.remainingtoast.faxhax.api.command.CommandManager;
import me.remainingtoast.faxhax.api.config.ConfigManager;
import me.remainingtoast.faxhax.api.module.ModuleManager;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.options.ServerList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

public class FaxHax implements ModInitializer {

    public static MinecraftClient mc;

    public static final String VERSION = "v1.0";

    public static Logger LOGGER = LogManager.getLogger("FaxHax");

    private static final boolean is2b2tUpdated = false;

    @Override
    public void onInitialize() {
        LOGGER.info("Welcome to FaxHax " + VERSION);

        // Minecraft
        mc = MinecraftClient.getInstance();

        // Modules
        ModuleManager.initializeModuleManager();

        // Commands
        CommandManager.initialiseCommandManager();

        //Config
//        ConfigManager.initializeConfigManager();

        // 2b2t Australia
        addServer();

        LOGGER.info("Initialization has now completed.");
    }

    public static void addServer() {
        ServerList servers = new ServerList(mc);
        servers.loadFile();

        boolean contains = false;
        for (int i = 0; i < servers.size(); i++) {
            ServerInfo server = servers.get(i);

            if (server.address.contains((is2b2tUpdated) ? "2b2t.com.au" : "test.2b2t.org")) {
                contains = true;
                break;
            }
        }

        if (!contains) {
            if (is2b2tUpdated) servers.add(new ServerInfo("2b2t Australia", "2b2t.com.au", false));
            else servers.add(new ServerInfo("2b2t 1.16", "test.2b2t.org", false));
            servers.saveFile();
        }
    }
}
