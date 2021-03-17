package me.remainingtoast.faxhax.api.module;

import me.remainingtoast.faxhax.FaxHax;
import me.remainingtoast.faxhax.impl.modules.combat.CrystalAura;
import me.remainingtoast.faxhax.impl.modules.misc.FakePlayer;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {

    public static List<Module> MODS = new ArrayList<>();

    public static void initializeModuleManager(){
        MODS.add(new CrystalAura());
        MODS.add(new FakePlayer());

        FaxHax.LOGGER.info("Successfully loaded " + MODS.size() + " modules");
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
        if(FaxHax.mc.currentScreen != null) return;
        for(Module mod : MODS){
            if(mod.key == InputUtil.fromKeyCode(keyCode, scancode)){
                if(GLFW.glfwGetKey(window, keyCode) == 0){ // Release
                    mod.toggle();
                }
            }
        }
    }
}
