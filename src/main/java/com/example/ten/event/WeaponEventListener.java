package com.example.ten.event;

import com.example.ten.Ten;
import com.example.ten.damage.KillDamageType;
import com.example.ten.inventory.LoliInventoryManager;
import com.example.ten.item.ModItems;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class WeaponEventListener {
    private static final float ABSOLUTE_DAMAGE = Float.MAX_VALUE;
    private static final float KILL_HEALTH = -1000f;
    private static final Set<UUID> HAS_WEAPON_CACHE = ConcurrentHashMap.newKeySet();

    public static void register() {
        Ten.LOGGER.info("WeaponEventListener registering events...");

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            server.getPlayerManager().getPlayerList().forEach(player -> {
                boolean hasWeapon = hasUltimateWeapon(player);
                
                if (hasWeapon) {
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 20, 4, false, false));
                    
                    if (!player.getAbilities().allowFlying) {
                        player.getAbilities().allowFlying = true;
                        player.getAbilities().flying = false;
                        ((ServerPlayerEntity) player).sendAbilitiesUpdate();
                    }
                } else {
                    if (!player.isCreative() && !player.isSpectator()) {
                        if (player.getAbilities().allowFlying) {
                            player.getAbilities().allowFlying = false;
                            player.getAbilities().flying = false;
                            ((ServerPlayerEntity) player).sendAbilitiesUpdate();
                        }
                    }
                }
            });
        });

        ServerLivingEntityEvents.ALLOW_DAMAGE.register((entity, source, amount) -> {
            if (entity instanceof PlayerEntity player && hasUltimateWeapon(player)) {
                if (source.getAttacker() instanceof LivingEntity attacker) {
                    if (!hasUltimateWeapon(attacker)) {
                        DamageSource killDamage = attacker.getDamageSources().create(KillDamageType.KILL);
                        attacker.damage(killDamage, ABSOLUTE_DAMAGE);
                    }
                }
                return false;
            }
            return true;
        });

        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            ItemStack stack = player.getStackInHand(hand);
            if (stack.isOf(ModItems.ULTIMATE_WEAPON)) {
                if (entity instanceof LivingEntity target) {
                    if (!hasUltimateWeapon(target)) {
                        forceKill(target, player);
                    }
                }
            }
            return ActionResult.PASS;
        });
        
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            PlayerEntity player = handler.getPlayer();
            LoliInventoryManager.saveInventoryToNbt(player);
        });
    }

    private static void forceKill(LivingEntity target, LivingEntity attacker) {
        if (target.getWorld().isClient) {
            return;
        }

        DamageSource killDamage = attacker.getDamageSources().create(KillDamageType.KILL);

        target.damage(killDamage, ABSOLUTE_DAMAGE);

        if (!target.isDead()) {
            for (int i = 0; i < 10; i++) {
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

    private static boolean hasUltimateWeapon(LivingEntity entity) {
        if (entity instanceof PlayerEntity player) {
            return player.getStackInHand(Hand.MAIN_HAND).isOf(ModItems.ULTIMATE_WEAPON)
                    || player.getStackInHand(Hand.OFF_HAND).isOf(ModItems.ULTIMATE_WEAPON)
                    || player.getInventory().contains(ModItems.ULTIMATE_WEAPON.getDefaultStack());
        }
        return false;
    }
}