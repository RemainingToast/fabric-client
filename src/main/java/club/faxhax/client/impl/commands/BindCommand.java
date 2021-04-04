package club.faxhax.client.impl.commands;

import club.faxhax.client.api.module.Module;
import club.faxhax.client.api.module.ModuleManager;
import club.faxhax.client.api.command.Command;
import club.faxhax.client.api.util.Util;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;

public class BindCommand extends Command {

    public BindCommand() {
        super("bind", "<module> <key>");
    }

    @Override
    public void perform(String[] args) {
        if(!(args.length >= 3)) {
            Util.messagePlayer(getWrongUsageMsg());
        } else {
            Module module = ModuleManager.getModule(args[1]);
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
                Util.messagePlayer(PREFIX + module.name + " has been binded to: " + Formatting.GREEN + key);
            } else Util.messagePlayer(PREFIX + Formatting.GRAY + "Module: \"" + args[1] + "\" doesn't exist!");
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
