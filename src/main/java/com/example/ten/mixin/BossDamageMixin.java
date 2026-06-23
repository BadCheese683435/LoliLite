package com.example.ten.mixin;

import com.example.ten.damage.KillDamageType;
import com.example.ten.item.ModItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class BossDamageMixin {
    
    private static final float ABSOLUTE_DAMAGE = Float.MAX_VALUE;
    private static final float KILL_HEALTH = -1000f;

    @Inject(at = @At("HEAD"), method = "damage")
    private void onDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity self = (LivingEntity)(Object)this;
        if (self.getWorld().isClient) {
            return;
        }
        
        Entity attacker = source.getAttacker();
        if (!(attacker instanceof PlayerEntity player)) {
            return;
        }
        
        if (!isHoldingUltimateWeapon(player)) {
            return;
        }
        
        DamageSource killDamage = player.getDamageSources().create(KillDamageType.KILL);
        
        forceKill(self, killDamage);
    }
    
    private void forceKill(LivingEntity target, DamageSource killDamage) {
        if (target.getWorld().isClient) {
            return;
        }
        
        target.damage(killDamage, ABSOLUTE_DAMAGE);
        
        if (!target.isDead()) {
            for (int i = 0; i < 20; i++) {
                target.damage(killDamage, ABSOLUTE_DAMAGE);
                if (target.isDead()) {
                    break;
                }
            }
        }
        
        if (!target.isDead()) {
            target.setHealth(KILL_HEALTH);
            target.damage(target.getDamageSources().starve(), ABSOLUTE_DAMAGE);
        }
        
        if (!target.isDead()) {
            target.onDeath(killDamage);
        }
    }
    
    private boolean isHoldingUltimateWeapon(PlayerEntity player) {
        return player.getStackInHand(Hand.MAIN_HAND).isOf(ModItems.ULTIMATE_WEAPON)
                || player.getStackInHand(Hand.OFF_HAND).isOf(ModItems.ULTIMATE_WEAPON)
                || player.getInventory().contains(ModItems.ULTIMATE_WEAPON.getDefaultStack());
    }
}
