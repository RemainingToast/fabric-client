package club.faxhax.client;

import club.faxhax.client.api.command.CommandManager;
import club.faxhax.client.api.config.ConfigManager;
import club.faxhax.client.api.module.ModuleManager;
import club.faxhax.client.api.util.AuthUtil;
import club.faxhax.client.api.util.Util;
import net.fabricmc.api.ModInitializer;

public class FaxHax implements ModInitializer, IFaxHax {

    @Override
    public void onInitialize() {
        long startTime = System.currentTimeMillis();

        // Auth
        AuthUtil.initializeAuth();

        if(!AuthUtil.isLicensed()) {
            LOGGER.fatal("[AUTH] This computer (" + AuthUtil.getHardwareUUID() + ") is not licensed!");
            LOGGER.fatal("[AUTH] Forcing Shutdown!");
            mc.scheduleStop();
            return;
        }

        LOGGER.info("Welcome to FaxHax " + VERSION);

        // Queue Skip
        Util.addServer();

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


}
