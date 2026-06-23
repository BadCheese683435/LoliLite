package com.example.ten.command;

import com.example.ten.item.ModItems;
import com.example.ten.item.UltimateWeaponItem;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class UltimateWeaponCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        var breakCommand = CommandManager.literal("break")
            .then(CommandManager.argument("x", IntegerArgumentType.integer())
                .then(CommandManager.argument("y", IntegerArgumentType.integer())
                    .then(CommandManager.argument("z", IntegerArgumentType.integer())
                        .executes(UltimateWeaponCommand::breakBlock))));

        var switchModeCommand = CommandManager.literal("switchmode")
            .executes(UltimateWeaponCommand::switchMode);

        dispatcher.register(CommandManager.literal("ultimateweapon")
            .then(breakCommand)
            .then(switchModeCommand));
    }

    private static int breakBlock(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();
        if (player == null) {
            return 0;
        }

        ItemStack mainHand = player.getStackInHand(net.minecraft.util.Hand.MAIN_HAND);
        ItemStack offHand = player.getStackInHand(net.minecraft.util.Hand.OFF_HAND);
        
        if (!mainHand.isOf(ModItems.ULTIMATE_WEAPON) && !offHand.isOf(ModItems.ULTIMATE_WEAPON)) {
            return 0;
        }

        int x = IntegerArgumentType.getInteger(context, "x");
        int y = IntegerArgumentType.getInteger(context, "y");
        int z = IntegerArgumentType.getInteger(context, "z");
        BlockPos centerPos = new BlockPos(x, y, z);

        ServerWorld world = player.getServerWorld();
        
        int mode = UltimateWeaponItem.getDigMode(player);
        int range = UltimateWeaponItem.getRangeFromMode(mode);
        int halfRange = range / 2;

        for (int dx = -halfRange; dx <= halfRange; dx++) {
            for (int dy = -halfRange; dy <= halfRange; dy++) {
                for (int dz = -halfRange; dz <= halfRange; dz++) {
                    BlockPos targetPos = centerPos.add(dx, dy, dz);
                    breakBlockAt(world, player, targetPos);
                }
            }
        }

        return 1;
    }

    private static void breakBlockAt(ServerWorld world, ServerPlayerEntity player, BlockPos pos) {
        try {
            BlockState state = world.getBlockState(pos);
            Block block = state.getBlock();

            if (block == Blocks.AIR) {
                return;
            }

            List<ItemStack> drops = block.getDroppedStacks(state, world, pos, null, player, ItemStack.EMPTY);

            world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);

            for (ItemStack drop : drops) {
                if (!player.getInventory().insertStack(drop)) {
                    player.dropItem(drop, false);
                }
            }

            world.syncWorldEvent(net.minecraft.world.WorldEvents.BLOCK_BROKEN, pos, Block.getRawIdFromState(state));
        } catch (Exception e) {
            // 忽略挖掘错误，防止崩溃
        }
    }

    private static int switchMode(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();
        if (player == null) {
            return 0;
        }

        UltimateWeaponItem.cycleDigMode(player);
        return 1;
    }
}
