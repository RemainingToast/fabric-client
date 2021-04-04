package club.faxhax.client.impl.modules.combat;

import club.faxhax.client.api.module.Module;
import club.faxhax.client.api.setting.Setting;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.ProtectionEnchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.HashMap;
import java.util.Map;

public class AutoArmour extends Module {

    Setting.Boolean elytraPriority = bool("ElytraPriority", false);
    Setting.Boolean preventBreak = bool("PreventBreak", true);

    public AutoArmour() {
        super("AutoArmour", Category.MOVEMENT);
    }

    private final HashMap<EquipmentSlot, Integer> hashMap = new HashMap<>();

    @Override
    protected void onTick() {
        assert mc.player != null;
        hashMap.put(EquipmentSlot.FEET, getProtectionLevel(mc.player.inventory.getStack(36)));
        hashMap.put(EquipmentSlot.LEGS, getProtectionLevel(mc.player.inventory.getStack(37)));
        hashMap.put(EquipmentSlot.CHEST, getProtectionLevel(mc.player.inventory.getStack(38)));
        hashMap.put(EquipmentSlot.HEAD, getProtectionLevel(mc.player.inventory.getStack(39)));
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
