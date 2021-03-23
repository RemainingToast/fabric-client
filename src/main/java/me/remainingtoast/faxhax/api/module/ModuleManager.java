package me.remainingtoast.faxhax.api.module;

import me.remainingtoast.faxhax.FaxHax;
import me.remainingtoast.faxhax.api.gui.ClickGUI;
import me.remainingtoast.faxhax.impl.modules.client.ClickGUIModule;
import me.remainingtoast.faxhax.impl.modules.client.MainMenu;
import me.remainingtoast.faxhax.impl.modules.combat.AutoTotem;
import me.remainingtoast.faxhax.impl.modules.combat.CrystalAura;
import me.remainingtoast.faxhax.impl.modules.combat.FastUtil;
import me.remainingtoast.faxhax.impl.modules.misc.FakePlayer;
import me.remainingtoast.faxhax.impl.modules.misc.PacketLogger;
import me.remainingtoast.faxhax.impl.modules.player.NoFall;
import me.remainingtoast.faxhax.impl.modules.render.CustomFOV;
import me.remainingtoast.faxhax.impl.modules.render.FullBright;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class ModuleManager {

    public static List<Module> MODS = new ArrayList<>();

    public static void initializeModuleManager(){
        long startTime = System.currentTimeMillis();

        MODS.add(new CrystalAura());
        MODS.add(new FakePlayer());
        MODS.add(new PacketLogger());
        MODS.add(new ClickGUIModule());
        MODS.add(new MainMenu());
        MODS.add(new CustomFOV());
        MODS.add(new FullBright());
        MODS.add(new FastUtil());
        MODS.add(new NoFall());
        MODS.add(new AutoTotem());

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

    public static List<Module> getModulesInCategory(Module.Category category){
        List<Module> output = new ArrayList<>();
        Iterator<Module> iter = MODS.iterator();
        while(iter.hasNext()){
            Module mod = iter.next();
            if(mod.category == category) output.add(mod);
        }
        return output;
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
                            new ClickGUI())) || mod.mc.currentScreen == null
                    ){
                        mod.toggle();
                    }
                }
            }
        }
    }
}
