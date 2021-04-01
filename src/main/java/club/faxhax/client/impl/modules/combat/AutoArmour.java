package club.faxhax.client.impl.modules.combat;

import club.faxhax.client.api.module.Module;
import club.faxhax.client.api.setting.Setting;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.ProtectionEnchantment;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.Map;

public class AutoArmour extends Module {

    Setting.Boolean elytraPriority = bool("ElytraPriority", false);
    Setting.Boolean preventBreak = bool("PreventBreak", true);

    public AutoArmour() {
        super("AutoArmour", Category.MOVEMENT);
    }
    
    @Override
    protected void onTick() {


    }

    private int getProtectionLevel(ItemStack stack) {
        if(stack.getItem() instanceof ArmorItem || stack.getItem() == Items.ELYTRA){
            int protection = 0;
            if(stack.getItem() instanceof ElytraItem) {
                if(!ElytraItem.isUsable(stack)) return 0;
                protection = (elytraPriority.getValue()) ? 32767 : 1;
            } else if (stack.getMaxDamage() - stack.getDamage() < 7 && preventBreak.getValue()) {
                return 0;
            }
            if(stack.hasEnchantments()) {
                for(Map.Entry<Enchantment, Integer> map : EnchantmentHelper.get(stack).entrySet()){
                    if(map.getKey() instanceof ProtectionEnchantment) protection += map.getValue();
                }
            }
            return ((stack.getItem() instanceof ArmorItem) ? (((ArmorItem) stack.getItem()).getProtection()) : 0) + protection;
        } else if(!stack.isEmpty()) {
            return 0;
        }
        return -1;
    }
}
