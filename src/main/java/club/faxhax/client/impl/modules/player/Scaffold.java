package club.faxhax.client.impl.modules.player;

import club.faxhax.client.api.module.Module;
import net.minecraft.item.BlockItem;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.Map;

public class Scaffold extends Module {

    public Scaffold() {
        super("Scaffold", Category.PLAYER);
    }

    private int slot = -1;
    private int previousSlot = -1;

    private HashMap<BlockPos, Integer> lastPlaced = new HashMap<>();

    @Override
    protected void onTick() {
        for(Map.Entry<BlockPos, Integer> entry : lastPlaced.entrySet()) {
            if(entry.getValue() > 0) lastPlaced.replace(entry.getKey(), entry.getValue() - 1);
            else lastPlaced.remove(entry.getKey());
        }
        findBestSlot();

    }

    private void findBestSlot(){
        assert mc.player != null;
        slot = -1;
        previousSlot = mc.player.inventory.selectedSlot;
        if(mc.player.inventory.getMainHandStack().getItem() instanceof BlockItem){
            slot = mc.player.inventory.selectedSlot;
        } else for(int i = 0; i < 8; i++){
            if(mc.player.inventory.getStack(i).getItem() instanceof BlockItem){
                slot = i;
                break;
            }
        }
        if(slot == -1) return;
        mc.player.inventory.selectedSlot = slot;
    }

}
