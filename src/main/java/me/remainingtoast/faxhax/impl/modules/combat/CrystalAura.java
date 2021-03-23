package me.remainingtoast.faxhax.impl.modules.combat;

import me.remainingtoast.faxhax.FaxHax;
import me.remainingtoast.faxhax.api.events.PacketEvent;
import me.remainingtoast.faxhax.api.module.Module;
import me.remainingtoast.faxhax.api.setting.Setting;
import me.remainingtoast.faxhax.api.util.DamageUtil;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import static java.lang.Math.atan2;
import static java.lang.Math.sqrt;

public class CrystalAura extends Module {

    Setting.Boolean placeBool;
    Setting.Double placeRange;
    Setting.Double minDamage;
    Setting.Boolean breakBool;
    Setting.Double breakRange;
    Setting.Double maxBreaks;
    Setting.Double maxSelfDamage;
    Setting.Boolean antiSuicide;
    Setting.Boolean players;
    Setting.Boolean hostile;
    Setting.Boolean passive;
    Setting.Boolean announce;

    private final List<Entity> entities = new ArrayList<>();
    private final List<EndCrystalEntity> crystals = new ArrayList<>();
    private final HashMap<BlockPos, Integer> blacklist = new HashMap<>();
    private final HashMap<Entity, BlockPos> bestBlocks = new HashMap<>();
    private final HashMap<BlockPos, Double> bestDamage = new HashMap<>();

    private int breaks = 0;

    public CrystalAura() {
        super("CrystalAura", Category.COMBAT);
        placeBool = bool("Place", true);
        placeRange = number("PlaceRange", 10.0,0.0,10.0);
        minDamage = number("MinDamage", 0.0,0.0, 36);
        breakBool = bool("Break", true);
        breakRange = number("BreakRange", 4.0,0.0,10.0);
        maxBreaks = number("MaxBreaks", 2,0,20);
        maxSelfDamage = number("MaxSelfDamage", 10,0,36);
        antiSuicide = bool("AntiSuicide", true);
        players = bool("Players", true);
        hostile = bool("Hostile", true);
        passive = bool("Passive", false);
        announce = bool("Announce", true);
    }

    @Override
    protected void onTick() {
        clearCache();
        findCrystals();
        findValidEntities(); // To Attack >:)
        placeCrystals();
        breakCrystals();
    }

    @Override
    protected void onToggle() {
        if(announce.getValue()) message(toggleMessage());
    }

    private void clearCache(){
        bestBlocks.clear();
        crystals.clear();
        entities.clear();
        blacklist.clear();
    }

    private void findValidEntities(){
        assert mc.world != null;
        assert mc.player != null;
        for(Entity entity : mc.world.getEntities()){
            if(entity == null || entity.removed || !entity.isAlive()) {
                entities.remove(entity);
                continue;
            }
            if(players.getValue() && entity instanceof PlayerEntity
                    && entity != mc.player && !((PlayerEntity) entity).isCreative() && !entity.isSpectator()
                    && mc.player.distanceTo(entity) <= 10){
                entities.add(entity);
            }
            if(hostile.getValue() && entity instanceof ZombieEntity && mc.player.distanceTo(entity) <= 10){
                entities.add(entity);
            }
        }
    }

    private HashMap<Entity, BlockPos> findBestBlock(Entity entity){
        if(entity instanceof LivingEntity){
            LivingEntity target = (LivingEntity) entity;
            BlockPos.findClosest(
                    target.getBlockPos(),
                    (int) placeRange.getValue(),
                    (int) placeRange.getValue(),
                    (blockPos1 -> canPlace(blockPos1) && !blacklist.containsKey(blockPos1)
                )
            ).ifPresent(blockPos -> {
                double damage = DamageUtil.getExplosiveDamage(blockPos.add(0.5, 1.0, 0.5), target);
                bestBlocks.putIfAbsent(target, blockPos);
                bestDamage.putIfAbsent(blockPos, damage);
                while (bestDamage.get(blockPos) < damage && damage < maxSelfDamage.getValue()){
                    bestBlocks.putIfAbsent(target, blockPos);
                    bestDamage.putIfAbsent(blockPos, damage);
                }
            });
        }
        return bestBlocks;
    }

    private void placeCrystals(){
        for(Entity entity : entities){
            for(Entity entry : findBestBlock(entity).keySet()){
                if(entry == entity) {
                    BlockPos pos = bestBlocks.get(entry);
                    double damage = bestDamage.get(pos);
                    if(!bestBlocks.isEmpty() && damage >= minDamage.getValue()) {
                        placeCrystal(pos);
                    }
                }
            }
        }
    }

    private void placeCrystal(BlockPos pos){
        assert mc.player != null;
        assert mc.interactionManager != null;
        if((mc.player.getMainHandStack().getItem() == Items.END_CRYSTAL
         || mc.player.getOffHandStack().getItem() == Items.END_CRYSTAL) && pos != null) {
            boolean offhand = mc.player.getOffHandStack().getItem() == Items.END_CRYSTAL;
            BlockPos bop = pos.add(0.5, 0.5, 0.5);
            Vec3d vec = new Vec3d(bop.getX(), bop.getY(), bop.getZ());
            float yaw = getYaw(vec);
            float pitch = getPitch(vec);
            mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.LookOnly(
                    yaw,
                    pitch,
                    mc.player.isOnGround()
            ));
            mc.interactionManager.interactBlock(
                    mc.player,
                    mc.world,
                    (offhand) ? Hand.OFF_HAND : Hand.MAIN_HAND,
                    new BlockHitResult(new Vec3d(
                            pos.getX(),
                            pos.getY(),
                            pos.getZ()
                    ),
                    Direction.UP,
                    pos,
                    false
            ));
            mc.player.swingHand((offhand) ? Hand.OFF_HAND : Hand.MAIN_HAND);
            mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.LookOnly(
                    mc.player.yaw,
                    mc.player.pitch,
                    mc.player.isOnGround()
            ));
        }
    }

    private float getYaw(Vec3d vec){
        assert mc.player != null;
        return (float) (mc.player.yaw + MathHelper.wrapDegrees(
                Math.toDegrees(atan2(vec.z - mc.player.getZ(),
                vec.x - mc.player.getX())) - 90f - mc.player.yaw)
        );
    }

    private float getPitch(Vec3d vec){
        assert mc.player != null;
        double diffX = vec.x - mc.player.getX();
        double diffY = vec.y - (mc.player.getY() + mc.player.getEyeHeight(mc.player.getPose()));
        double diffZ = vec.z - mc.player.getZ();
        double diffXZ = sqrt(diffX * diffX + diffZ * diffZ);
        return (float) (mc.player.pitch + MathHelper.wrapDegrees(
                (-Math.toDegrees(atan2(diffY, diffXZ))) - mc.player.pitch));
    }

    private void breakCrystals(){
        assert mc.player != null;
        for(EndCrystalEntity crystal : crystals){
            if(canExplode(crystal)) {
                if(mc.interactionManager == null) return;
                if(breaks >= maxBreaks.getValue()) {
                    breaks = 0;
                    return;
                }
                mc.interactionManager.attackEntity(mc.player, crystal);
                mc.player.swingHand(
                        (mc.player.getOffHandStack().getItem() == Items.END_CRYSTAL)
                        ? Hand.OFF_HAND : Hand.MAIN_HAND
                );
                ++breaks;
            }
        }
    }

    private void findCrystals(){
        assert mc.world != null;
        assert mc.player != null;
        Double d = null;
        for(Entity entity : mc.world.getEntities()){
            if(entity == null || entity.removed) continue;
            if(entity instanceof EndCrystalEntity){
                BlockPos down = entity.getBlockPos().down();
                if(blacklist.containsKey(down)) {
                    if(blacklist.get(down) > 0) blacklist.replace(down, blacklist.get(down) - 1);
                    else blacklist.remove(down);
                }
                float distance = mc.player.distanceTo(entity);
                if(canReach(mc.player.getPos().add(0.0, mc.player.getEyeHeight(mc.player.getPose()), 0.0),
                        entity.getBoundingBox(), breakRange.getValue()) && (d == null || distance < d)){
                    d = (double) distance;
                    crystals.add((EndCrystalEntity) entity);
                }
            }
        }
    }

    private boolean canExplode(EndCrystalEntity entity){
        if(entity == null) return false;
        assert mc.player != null;
        boolean damageSafe = (antiSuicide.getValue()
                && (mc.player.getHealth() + mc.player.getAbsorptionAmount()) >= maxSelfDamage.getValue())
                || mc.player.isSpectator() || mc.player.isCreative() || mc.player.isInvulnerable();
        return damageSafe && breakBool.getValue() && canReach(mc.player.getPos().add(0.0,
                mc.player.getEyeHeight(mc.player.getPose()), 0.0), entity.getBoundingBox(), breakRange.getValue());
    }

    private boolean canReach(Vec3d point, Box aabb, Double maxRange) {
        return aabb.expand(maxRange).contains(point);
    }

    private boolean canPlace(BlockPos pos){
        assert mc.world != null;
        Block block = mc.world.getBlockState(pos).getBlock();
        return (block == Blocks.BEDROCK || block == Blocks.OBSIDIAN) && isEmpty(pos.add(0, 1,0));
    }

    private boolean isEmpty(BlockPos pos){
        assert mc.world != null;
        return mc.world.isAir(pos) && mc.world.getOtherEntities(null, new Box(
                pos.getX(),
                pos.getY(),
                pos.getZ(),
                pos.getX() + 1.0,
                pos.getY() + 2.0,
                pos.getZ() + 1.0
        )).isEmpty();
    }

    private String toggleMessage(){
        final Random random = new Random();
        final List<String> ENABLED = new ArrayList<>();
        final List<String> DISABLED = new ArrayList<>();
        ENABLED.add(PREFIX + "we " + Formatting.GREEN + "gaming " + Formatting.GRAY + "now");
        ENABLED.add(PREFIX + "nerd destroyer " + Formatting.GREEN + "activated");
        ENABLED.add(PREFIX + "gaming chair turned " + Formatting.GREEN + "on");
        DISABLED.add(PREFIX + "we aint " + Formatting.RED + "gaming " + Formatting.GRAY + "no more");
        DISABLED.add(PREFIX + "nerd destroyer " + Formatting.RED + "deactivated");
        DISABLED.add(PREFIX + "gaming chair turned " + Formatting.RED + "off");
        if(enabled) return ENABLED.get(random.nextInt(ENABLED.size()));
        else return DISABLED.get(random.nextInt(DISABLED.size()));
    }

}
