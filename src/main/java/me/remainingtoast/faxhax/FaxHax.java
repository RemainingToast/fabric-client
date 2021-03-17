package me.remainingtoast.faxhax;

import me.remainingtoast.faxhax.api.command.CommandManager;
import me.remainingtoast.faxhax.api.config.ConfigManager;
import me.remainingtoast.faxhax.api.module.ModuleManager;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FaxHax implements ModInitializer {

    public static MinecraftClient mc;

    public static final String VERSION = "v1.0";

    public static Logger LOGGER = LogManager.getLogger("FaxHax");

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

        LOGGER.info("Initialization has now completed.");
    }
}
