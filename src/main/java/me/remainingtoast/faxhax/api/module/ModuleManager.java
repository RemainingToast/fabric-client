package me.remainingtoast.faxhax.api.module;

import me.remainingtoast.faxhax.FaxHax;
import me.remainingtoast.faxhax.api.gui.GuiScreen;
import me.remainingtoast.faxhax.impl.modules.client.ClickGUI;
import me.remainingtoast.faxhax.impl.modules.combat.CrystalAura;
import me.remainingtoast.faxhax.impl.modules.misc.FakePlayer;
import me.remainingtoast.faxhax.impl.modules.misc.PacketLogger;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ModuleManager {

    public static List<Module> MODS = new ArrayList<>();

    public static void initializeModuleManager(){
        long startTime = System.currentTimeMillis();

        MODS.add(new CrystalAura());
        MODS.add(new FakePlayer());
        MODS.add(new PacketLogger());
        MODS.add(new ClickGUI());

        String endTime = (System.currentTimeMillis() - startTime) + "ms";
        FaxHax.LOGGER.info("Successfully loaded " + MODS.size() + " modules in "+endTime);
    }

    public static Module getModule(String name){
        for(Module mod : MODS){
            if(mod.aliases.contains(name)
                    || mod.name.equalsIgnoreCase(name))
                return mod;
        }
        return null;
    }

    public static void onTick(){
        for(Module mod : MODS){
            if(mod.enabled) mod.onTick();
        }
    }

    public static void onKey(long window, int keyCode, int scancode){
        for(Module mod : MODS){
            if(mod.key == InputUtil.fromKeyCode(keyCode, scancode)){
                if(GLFW.glfwGetKey(window, keyCode) == 0){ // Release
                    if((mod.name.equals("ClickGUI")
                            && Objects.equals(mod.mc.currentScreen,
                            new GuiScreen())) || mod.mc.currentScreen == null
                    ){
                        mod.toggle();
                    }
                }
            }
        }
    }
}
