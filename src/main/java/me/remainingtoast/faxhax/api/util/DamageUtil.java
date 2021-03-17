package me.remainingtoast.faxhax.api.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.explosion.Explosion;

import java.util.HashMap;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static net.minecraft.util.math.MathHelper.floor;
import static net.minecraft.util.math.MathHelper.sqrt;

public class DamageUtil {

    private static MinecraftClient mc = MinecraftClient.getInstance();
    private static HashMap<Entity, Float> damageCache = new HashMap<>();

    public static float getExplosiveDamage(BlockPos block, LivingEntity target){
        assert mc.world != null;
        if(mc.world.getDifficulty() == Difficulty.PEACEFUL) return 0f;
        if(damageCache.containsKey(target)) return damageCache.get(target);
        Vec3d vec = Vec3d.of(block).add(0.5, 1.0,0.5);
        Explosion explosion = new Explosion(
                mc.world,
                null,
                vec.x,
                vec.y,
                vec.z,
                6f,
                false,
                Explosion.DestructionType.DESTROY
        );
        double power = 13.0;
        if(!mc.world.getOtherEntities(null,
                new Box(
                        floor(vec.x - power),
                        floor(vec.y - power),
                        floor(vec.z - power),
                        floor(vec.x + power),
                        floor(vec.y + power),
                        floor(vec.z + power)
                )).contains(target)){
            damageCache.put(target, 0f);
            return 0f;
        }
        if(!target.isImmuneToExplosion()){
            double distanceSquared = sqrt(target.squaredDistanceTo(vec)) / power;
            if(distanceSquared <= 1.0){
                double posSquared = sqrt(
                        target.getX() - vec.x * target.getX() - vec.x
                        + target.getY() + target.getStandingEyeHeight() - vec.y * target.getY()
                        + target.getStandingEyeHeight() - vec.y
                        + target.getZ() - vec.z * target.getZ() - vec.z);
                if(posSquared != 0.0){
                    double exposure = Explosion.getExposure(vec, target);
                    double diff = (1.0 - distanceSquared) * exposure;
                    float damage = floor((diff * diff + diff) / 2.0 * 7.0 * power);
                    if(target instanceof PlayerEntity){
                        if (mc.world.getDifficulty() == Difficulty.EASY) damage = min(damage / 2.0f + 1.0f, damage);
                        else if (mc.world.getDifficulty() == Difficulty.HARD) damage = damage * 3.0f / 2.0f;
                    }
                    damage = net.minecraft.entity.DamageUtil.getDamageLeft(damage, (float) target.getArmor(),
                            (float) target.getAttributeInstance(EntityAttributes.GENERIC_ARMOR_TOUGHNESS).getValue());

                    if(target.hasStatusEffect(StatusEffects.RESISTANCE)){
                        int resistance = (target.getStatusEffect(StatusEffects.RESISTANCE).getAmplifier() + 1) * 5;
                        int diffResistance = (int) (damage * (25 - resistance));
                        damage = max(diffResistance / 25.0f, 0.0f);
                    }
                    if(damage <= 0.0f) damage = 0.0f;
                    else {
                        int protectionAmount = EnchantmentHelper.getProtectionAmount(
                                target.getArmorItems(),
                                explosion.getDamageSource()
                        );
                        if(protectionAmount > 0)
                            damage = net.minecraft.entity.DamageUtil.getInflictedDamage(
                                damage,
                                (float) protectionAmount);
                    }
                    damageCache.put(target, damage);
                    return damage;
                }
            }
        }
        damageCache.put(target, 0f);
        return 0f;
    }

    public static HashMap<Entity, Float> getDamageCache() {
        return damageCache;
    }
}
