package me.remainingtoast.faxhax;

import me.remainingtoast.faxhax.api.command.CommandManager;
import me.remainingtoast.faxhax.api.config.ConfigManager;
import me.remainingtoast.faxhax.api.events.ReceivePacketEvent;
import me.remainingtoast.faxhax.api.events.SendPacketEvent;
import me.remainingtoast.faxhax.api.module.ModuleManager;
import me.remainingtoast.faxhax.api.util.AuthUtil;
import me.zero.alpine.bus.EventBus;
import me.zero.alpine.bus.EventManager;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
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

    public static EventBus EVENTS = new EventManager();

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

        // Modules
        ModuleManager.initializeModuleManager();

        // Commands
        CommandManager.initializeCommandManager();

        // Config
        ConfigManager.initializeConfigManager();

        ClientLifecycleEvents.CLIENT_STOPPING.register(client -> {
            ConfigManager.shutdown();
            LOGGER.info("Sucessfully saved and shutdown!");
        });

        // 2b2t Australia
        addServer();

        FaxHax.EVENTS.subscribeAll(receivePacket, sendPacket);

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

    @EventHandler
    public Listener<ReceivePacketEvent> receivePacket = new Listener<>(event -> {
        System.out.println("in: " + event.getPacket().toString());
    });

    @EventHandler
    public Listener<SendPacketEvent> sendPacket = new Listener<>(event -> {
        System.out.println("out: " + event.getPacket().toString());
    });
}
