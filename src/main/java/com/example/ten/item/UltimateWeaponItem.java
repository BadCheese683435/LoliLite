package com.example.ten.item;

import com.example.ten.Ten;
import com.example.ten.damage.KillDamageType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class UltimateWeaponItem extends PickaxeItem {
    private static final float ABSOLUTE_DAMAGE = Float.MAX_VALUE;
    private static final float KILL_HEALTH = -1000f;
    
    private static final Map<PlayerEntity, Integer> DIG_MODES = new WeakHashMap<>();

    public UltimateWeaponItem(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, net.minecraft.client.item.TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        tooltip.add(Text.of(""));
        tooltip.add(Text.of("§6在主手时："));
        tooltip.add(Text.of("§cT§6R§eE§aE§b(§33§b) §7攻击伤害"));
        tooltip.add(Text.of("§cT§6R§eE§aE§b(§33§b) §7攻击速度"));
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!hasUltimateWeapon(target)) {
            forceKill(target, attacker);
        }
        return super.postHit(stack, target, attacker);
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        if (!world.isClient && miner instanceof PlayerEntity player) {
            int mode = getDigMode(player);
            int range = getRangeFromMode(mode);
            int halfRange = range / 2;

            for (int dx = -halfRange; dx <= halfRange; dx++) {
                for (int dy = -halfRange; dy <= halfRange; dy++) {
                    for (int dz = -halfRange; dz <= halfRange; dz++) {
                        BlockPos targetPos = pos.add(dx, dy, dz);
                        if (!targetPos.equals(pos)) {
                            BlockState targetState = world.getBlockState(targetPos);
                            if (!targetState.isAir()) {
                                try {
                                    world.breakBlock(targetPos, true, player);
                                } catch (Exception e) {
                                    // 忽略错误
                                }
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (!world.isClient) {
            double range = user.isSneaking() ? 100.0 : 16.0;
            Box box = new Box(
                    user.getX() - range, user.getY() - range, user.getZ() - range,
                    user.getX() + range, user.getY() + range, user.getZ() + range
            );
            List<Entity> entities = world.getOtherEntities(user, box, entity -> entity instanceof LivingEntity);
            
            int killCount = 0;
            for (Entity entity : entities) {
                if (entity instanceof LivingEntity livingEntity && livingEntity != user) {
                    if (!hasUltimateWeapon(livingEntity)) {
                        forceKill(livingEntity, user);
                        killCount++;
                    }
                }
            }
            
            // 发送击杀信息到聊天框
            if (user instanceof ServerPlayerEntity serverPlayer) {
                String rangeText = (int)range + "x" + (int)range + "x" + (int)range;
                serverPlayer.sendMessage(Text.literal("§6[氪金萝莉] §a已击杀 §e" + killCount + " §a个生物 (范围" + rangeText + ")"), false);
            }
            
            // 播放自定义范围攻击音效
            world.playSound(
                null,
                user.getX(), user.getY(), user.getZ(),
                ModItems.ULTIMATE_ATTACK_SOUND,
                SoundCategory.PLAYERS,
                1.0f, 1.0f
            );
            
            // 保留经验获取音效
            world.playSound(
                null,
                user.getX(), user.getY(), user.getZ(),
                net.minecraft.sound.SoundEvents.ENTITY_PLAYER_LEVELUP,
                SoundCategory.PLAYERS,
                1.0f, 1.0f
            );
        }
        return TypedActionResult.success(stack);
    }
    
    public static void cycleDigMode(PlayerEntity player) {
        int currentMode = getDigMode(player);
        int newMode = (currentMode + 1) % 3;
        DIG_MODES.put(player, newMode);
        
        String modeName;
        switch (newMode) {
            case 0 -> modeName = "1x1";
            case 1 -> modeName = "3x3";
            case 2 -> modeName = "11x11";
            default -> modeName = "1x1";
        }
        
        if (player instanceof ServerPlayerEntity serverPlayer) {
            serverPlayer.sendMessage(Text.literal("§6[氪金萝莉] §a当前范围挖掘模式: §e" + modeName), false);
        }
    }
    
    public static int getDigMode(PlayerEntity player) {
        return DIG_MODES.getOrDefault(player, 0);
    }
    
    public static int getRangeFromMode(int mode) {
        return switch (mode) {
            case 0 -> 1;
            case 1 -> 3;
            case 2 -> 11;
            default -> 1;
        };
    }
    
    private void forceKill(LivingEntity target, LivingEntity attacker) {
        if (target.getWorld().isClient) {
            return;
        }
        
        DamageSource killDamage = attacker.getDamageSources().create(KillDamageType.KILL);
        
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
    
    private static boolean hasUltimateWeapon(LivingEntity entity) {
        if (entity instanceof PlayerEntity player) {
            return player.getStackInHand(Hand.MAIN_HAND).isOf(ModItems.ULTIMATE_WEAPON)
                    || player.getStackInHand(Hand.OFF_HAND).isOf(ModItems.ULTIMATE_WEAPON)
                    || player.getInventory().contains(ModItems.ULTIMATE_WEAPON.getDefaultStack());
        }
        return false;
    }
}
