package com.example.ten.event;

import com.example.ten.item.ModItems;
import com.example.ten.item.UltimateWeaponItem;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class BlockBreakListener {
    
    public static void register() {
        AttackBlockCallback.EVENT.register((player, world, hand, pos, direction) -> {
            ItemStack mainHand = player.getStackInHand(hand);
            
            if (mainHand.isOf(ModItems.ULTIMATE_WEAPON)) {
                if (world instanceof ServerWorld serverWorld) {
                    breakBlocksInstantly(serverWorld, player, pos);
                    return ActionResult.SUCCESS;
                }
            }
            return ActionResult.PASS;
        });
    }
    
    private static void breakBlocksInstantly(ServerWorld world, PlayerEntity player, BlockPos centerPos) {
        int mode = UltimateWeaponItem.getDigMode(player);
        int range = UltimateWeaponItem.getRangeFromMode(mode);
        int halfRange = range / 2;

        for (int dx = -halfRange; dx <= halfRange; dx++) {
            for (int dy = -halfRange; dy <= halfRange; dy++) {
                for (int dz = -halfRange; dz <= halfRange; dz++) {
                    BlockPos targetPos = centerPos.add(dx, dy, dz);
                    if (world.isInBuildLimit(targetPos)) {
                        breakBlockInstantly(world, player, targetPos);
                    }
                }
            }
        }
    }
    
    private static void breakBlockInstantly(ServerWorld world, PlayerEntity player, BlockPos pos) {
        try {
            BlockState state = world.getBlockState(pos);
            Block block = state.getBlock();

            if (block == Blocks.AIR) {
                return;
            }

            List<net.minecraft.item.ItemStack> drops;
            
            if (block == Blocks.BEDROCK) {
                drops = List.of(new net.minecraft.item.ItemStack(Blocks.BEDROCK));
            } else {
                drops = block.getDroppedStacks(state, world, pos, null, player, net.minecraft.item.ItemStack.EMPTY);
            }

            net.minecraft.block.entity.BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity != null) {
                blockEntity.markRemoved();
            }
            
            world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
            world.updateNeighbors(pos, Blocks.AIR);

            for (net.minecraft.item.ItemStack drop : drops) {
                if (drop != null && !drop.isEmpty()) {
                    if (!player.getInventory().insertStack(drop)) {
                        player.dropItem(drop, false);
                    }
                }
            }
        } catch (Exception e) {
            // 忽略错误
        }
    }
}
