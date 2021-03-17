package me.remainingtoast.faxhax.impl.commands;

import me.remainingtoast.faxhax.api.command.Command;
import me.remainingtoast.faxhax.api.module.Module;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;

import static me.remainingtoast.faxhax.api.module.ModuleManager.getModule;

public class BindCommand extends Command {

    public BindCommand() {
        super("bind", "<module> <key>");
    }

    @Override
    public void perform(String[] args) {
        if(!(args.length >= 3)) {
            message(getWrongUsageMsg());
        } else {
            Module module = getModule(args[1]);
            String key;
            if(module != null) {
                if(isInteger(args[2])){
                    module.setKey(InputUtil.fromKeyCode(Integer.parseInt(args[2]), -1));
                    key = GLFW.glfwGetKeyName(Integer.parseInt(args[2]), -1);
                } else {
                    InputUtil.Key key1 = InputUtil.fromTranslationKey("key.keyboard."+args[2].replaceAll("[_\\s]", "\\."));
                    module.setKey(key1);
                    key = GLFW.glfwGetKeyName(key1.getCode(), -1);
                }
                message(PREFIX + module.name + " has been binded to: " + Formatting.GREEN + key);
            } else message(PREFIX + Formatting.GRAY + "Module: \"" + args[1] + "\" doesn't exist!");
        }
    }

    private boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch(NumberFormatException | NullPointerException e) {
            return false;
        }
        return true;
    }

}
