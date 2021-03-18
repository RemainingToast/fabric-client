package me.remainingtoast.faxhax.impl.modules.misc;

import com.mojang.authlib.GameProfile;
import me.remainingtoast.faxhax.api.module.Module;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.world.ServerWorld;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class FakePlayer extends Module {

    public FakePlayer() {
        super("FakePlayer", Category.MISC);
    }

    @Override
    protected void onToggle() {
        super.onToggle();
    }

    @Override
    protected void onEnable() {
        if(mc.world != null && mc.player != null){
            OtherClientPlayerEntity player = new OtherClientPlayerEntity(mc.world, new GameProfile(UUID.fromString("6e7514e8-78a9-4cfd-80de-d400b97fece4"), "FaxMachine5781"));
            player.copyPositionAndRotation(mc.player);
            player.setHeadYaw(mc.player.headYaw);
            addMaxArmour(player);
            mc.world.addEntity(-5781, player);
        }
    }

    @Override
    protected void onDisable() {
        if(mc.world != null) mc.world.removeEntity(-5781);
    }

    private void addMaxArmour(PlayerEntity player){
        PlayerInventory inventory = player.inventory;
        inventory.armor.set(3, addMaxEnchantments(new ItemStack(Items.DIAMOND_HELMET), Arrays.asList(
                Enchantments.PROTECTION,
                Enchantments.UNBREAKING,
                Enchantments.RESPIRATION,
                Enchantments.AQUA_AFFINITY,
                Enchantments.MENDING
        )));
        inventory.armor.set(2, addMaxEnchantments(new ItemStack(Items.DIAMOND_CHESTPLATE), Arrays.asList(
                Enchantments.PROTECTION,
                Enchantments.UNBREAKING,
                Enchantments.MENDING
        )));
        inventory.armor.set(1, addMaxEnchantments(new ItemStack(Items.DIAMOND_LEGGINGS), Arrays.asList(
                Enchantments.BLAST_PROTECTION,
                Enchantments.UNBREAKING,
                Enchantments.MENDING
        )));
        inventory.armor.set(0, addMaxEnchantments(new ItemStack(Items.DIAMOND_BOOTS), Arrays.asList(
                Enchantments.PROTECTION,
                Enchantments.UNBREAKING,
                Enchantments.MENDING,
                Enchantments.FEATHER_FALLING,
                Enchantments.DEPTH_STRIDER
        )));
    }

    private ItemStack addMaxEnchantments(ItemStack item, List<Enchantment> enchantments){
        for(Enchantment e : enchantments){
            item.addEnchantment(e,e.getMaxLevel());
        }
        return item;
    }
}
