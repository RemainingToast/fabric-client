package club.faxhax.client.api.module;

import club.faxhax.client.FaxHax;
import club.faxhax.client.impl.modules.client.ClickGUIModule;
import club.faxhax.client.impl.modules.client.FontModule;
import club.faxhax.client.impl.modules.client.MainMenu;
import club.faxhax.client.impl.modules.combat.AutoTotem;
import club.faxhax.client.impl.modules.combat.CrystalAura;
import club.faxhax.client.impl.modules.combat.FastUtil;
import club.faxhax.client.impl.modules.misc.ExtraTab;
import club.faxhax.client.impl.modules.misc.FakePlayer;
import club.faxhax.client.impl.modules.misc.PacketLogger;
import club.faxhax.client.impl.modules.movement.AutoWalk;
import club.faxhax.client.impl.modules.movement.FastStop;
import club.faxhax.client.impl.modules.movement.SafeWalk;
import club.faxhax.client.impl.modules.movement.Velocity;
import club.faxhax.client.impl.modules.player.AntiHunger;
import club.faxhax.client.impl.modules.player.NoFall;
import club.faxhax.client.impl.modules.render.CustomFOV;
import club.faxhax.client.impl.modules.render.FullBright;
import club.faxhax.client.impl.modules.render.NoRender;
import club.faxhax.client.impl.modules.render.Tracers;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import java.util.*;

public class ModuleManager {

    public static List<Module> MODS = new ArrayList<>();

    public static void initializeModuleManager(){
        long startTime = System.currentTimeMillis();

        add(new CrystalAura(), new FakePlayer(), new PacketLogger(), new ClickGUIModule(),
            new MainMenu(), new CustomFOV(), new FullBright(), new FastUtil(), new NoFall(),
            new AutoTotem(), new Velocity(), new FontModule(), new SafeWalk(), new NoRender(),
            new AutoWalk(), new ExtraTab(), new Tracers(), new AntiHunger(), new FastStop()
        );

        MODS.sort(Comparator.comparing(Module::getName));

        String endTime = (System.currentTimeMillis() - startTime) + "ms";
        FaxHax.LOGGER.info("Successfully loaded " + MODS.size() + " modules in "+endTime);
    }

    private static void add(Module... modules){
        MODS.addAll(Arrays.asList(modules));
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
                if(GLFW.glfwGetKey(window, keyCode) == 0){ // onRelease
                    if((mod.name.equals("ClickGUI") && Objects.equals(mod.mc.currentScreen,
                            ClickGUIModule.getGUI())) || mod.mc.currentScreen == null
                    ){
                        mod.toggle();
                    }
                }
            }
        }
    }
}
