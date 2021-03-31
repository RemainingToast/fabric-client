package club.faxhax.client.impl.modules.combat;

import club.faxhax.client.api.module.Module;
import club.faxhax.client.api.setting.Setting;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;

public class AutoTotem extends Module {

    private int totems = 0;

    private boolean moving = false;

    Setting.Boolean inGUIS = bool("WorkInsideGUI", true);

    public AutoTotem() {
        super("AutoTotem", Category.COMBAT);
    }

    public int getTotems() {
        return totems;
    }

    @Override
    protected void onTick() {
        assert mc.player != null;
        assert mc.interactionManager != null;
        totems = mc.player.inventory.count(Items.TOTEM_OF_UNDYING);
        if(mc.player.getOffHandStack().getItem().equals(Items.TOTEM_OF_UNDYING)) totems++;
        if(!mc.player.getOffHandStack().isEmpty()
                || !inGUIS.getValue()
                && mc.currentScreen instanceof InventoryScreen)
            return;
        if(moving) {
            mc.interactionManager.clickSlot(0, 45, 0, SlotActionType.PICKUP, mc.player);
            moving = false;
            return;
        }
        if(!mc.player.inventory.isEmpty()){
            if(totems == 0) return;
            int totemSlot = 0;
            for(int i = 0; i < 44; i++){
                Item item = mc.player.inventory.getStack(i).getItem();
                if(item.equals(Items.TOTEM_OF_UNDYING)) {
                    totemSlot = i;
                    break;
                }
            }
            mc.interactionManager.clickSlot(
                    0,
                    (totemSlot < 9) ? totemSlot + 36 : totemSlot,
                    0,
                    SlotActionType.PICKUP,
                    mc.player
            );
            moving = true;
        }
    }
}
